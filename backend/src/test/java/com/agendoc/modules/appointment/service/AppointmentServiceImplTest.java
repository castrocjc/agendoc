package com.agendoc.modules.appointment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.common.exception.ConflictException;
import com.agendoc.common.exception.BadRequestException;
import com.agendoc.common.exception.ResourceNotFoundException;
import com.agendoc.modules.agenda.entity.AgendaBlockEntity;
import com.agendoc.modules.agenda.entity.MedicalAgendaEntity;
import com.agendoc.modules.agenda.repository.AgendaBlockRepository;
import com.agendoc.modules.appointment.dto.AppointmentResponse;
import com.agendoc.modules.appointment.dto.CreateAppointmentRequest;
import com.agendoc.modules.appointment.dto.AppointmentAgendaResponse;
import com.agendoc.modules.appointment.dto.CancelAppointmentRequest;
import com.agendoc.modules.appointment.dto.RescheduleAppointmentRequest;
import com.agendoc.modules.appointment.entity.AppointmentEntity;
import com.agendoc.modules.appointment.entity.AppointmentStatusCode;
import com.agendoc.modules.appointment.entity.AppointmentStatusEntity;
import com.agendoc.modules.appointment.entity.AppointmentRescheduleHistoryEntity;
import com.agendoc.modules.appointment.repository.AppointmentRepository;
import com.agendoc.modules.appointment.repository.AppointmentRescheduleHistoryRepository;
import com.agendoc.modules.appointment.repository.AppointmentStatusRepository;
import com.agendoc.modules.appointment.repository.AppointmentRescheduleHistoryRepository;
import com.agendoc.modules.clinic.entity.ClinicEntity;
import com.agendoc.modules.clinic.repository.ClinicRepository;
import com.agendoc.modules.doctor.entity.DoctorEntity;
import com.agendoc.modules.doctor.repository.DoctorRepository;
import com.agendoc.modules.patient.entity.PatientEntity;
import com.agendoc.modules.patient.repository.PatientRepository;
import com.agendoc.modules.doctor.entity.MedicalSpecialtyEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceImplTest {

        @Mock
        private AppointmentRepository appointmentRepository;

        @Mock
        private AppointmentStatusRepository appointmentStatusRepository;

        @Mock
        private AgendaBlockRepository agendaBlockRepository;

        @Mock
        private PatientRepository patientRepository;

        @Mock
        private DoctorRepository doctorRepository;

        @Mock
        private ClinicRepository clinicRepository;

        @Mock
        private AppointmentRescheduleHistoryRepository appointmentRescheduleHistoryRepository;

        private AppointmentServiceImpl appointmentService;

        @BeforeEach
        void setUp() {
                appointmentService = new AppointmentServiceImpl(
                                appointmentRepository,
                                appointmentRescheduleHistoryRepository,
                                appointmentStatusRepository,
                                agendaBlockRepository,
                                patientRepository,
                                doctorRepository,
                                clinicRepository);
        }

        @Test
        void shouldCreateAppointmentWhenRequestIsValid() {

                CreateAppointmentRequest request = new CreateAppointmentRequest(
                                1L,
                                2L,
                                12L,
                                "Consulta médica general",
                                "Primera cita del paciente");

                ClinicEntity clinic = createClinic();
                PatientEntity patient = createPatient(clinic);
                DoctorEntity doctor = createDoctor(clinic);
                AgendaBlockEntity agendaBlock = createAgendaBlock(clinic, doctor);
                AppointmentStatusEntity appointmentStatus = createAppointmentStatus();

                when(clinicRepository
                                .findFirstByRecordStatusOrderByIdAsc(
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(clinic));

                when(patientRepository.findByIdAndRecordStatus(
                                1L,
                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(patient));

                when(doctorRepository.findByIdAndRecordStatus(
                                2L,
                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(doctor));

                when(agendaBlockRepository
                                .findByIdAndRecordStatusForUpdate(
                                                12L,
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(agendaBlock));

                when(appointmentRepository
                                .existsPatientScheduleConflict(
                                                any(),
                                                any(),
                                                any(),
                                                any(),
                                                any(),
                                                any()))
                                .thenReturn(false);

                when(appointmentStatusRepository
                                .findByCodeAndRecordStatus(
                                                AppointmentStatusCode.PROGRAMADA.name(),
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(appointmentStatus));

                when(appointmentRepository.save(
                                any(AppointmentEntity.class)))
                                .thenAnswer(invocation -> {
                                        AppointmentEntity appointment = invocation.getArgument(0);

                                        appointment.setId(20L);

                                        return appointment;
                                });

                AppointmentResponse response = appointmentService.createAppointment(request);

                assertThat(response.id()).isEqualTo(20L);
                assertThat(response.clinicId()).isEqualTo(1L);

                assertThat(response.patientId()).isEqualTo(1L);
                assertThat(response.patientFirstName()).isEqualTo("María");
                assertThat(response.patientLastName()).isEqualTo("González");

                assertThat(response.doctorId()).isEqualTo(2L);
                assertThat(response.doctorFirstName()).isEqualTo("Ana");
                assertThat(response.doctorLastName()).isEqualTo("Torres");

                assertThat(response.agendaBlockId()).isEqualTo(12L);
                assertThat(response.appointmentDate())
                                .isEqualTo(LocalDate.now().plusDays(1));
                assertThat(response.startTime())
                                .isEqualTo(LocalTime.of(9, 0));
                assertThat(response.endTime())
                                .isEqualTo(LocalTime.of(9, 30));

                assertThat(response.statusCode())
                                .isEqualTo("PROGRAMADA");
                assertThat(response.statusName())
                                .isEqualTo("Programada");

                assertThat(response.reason())
                                .isEqualTo("Consulta médica general");
                assertThat(response.notes())
                                .isEqualTo("Primera cita del paciente");
                assertThat(response.recordStatus())
                                .isEqualTo("ACTIVE");

                ArgumentCaptor<AppointmentEntity> appointmentCaptor = ArgumentCaptor.forClass(AppointmentEntity.class);

                verify(appointmentRepository)
                                .save(appointmentCaptor.capture());

                AppointmentEntity savedAppointment = appointmentCaptor.getValue();

                assertThat(savedAppointment.getClinic())
                                .isSameAs(clinic);
                assertThat(savedAppointment.getPatient())
                                .isSameAs(patient);
                assertThat(savedAppointment.getDoctor())
                                .isSameAs(doctor);
                assertThat(savedAppointment.getAgendaBlock())
                                .isSameAs(agendaBlock);
                assertThat(savedAppointment.getStatus())
                                .isSameAs(appointmentStatus);

                assertThat(savedAppointment.getReason())
                                .isEqualTo("Consulta médica general");
                assertThat(savedAppointment.getNotes())
                                .isEqualTo("Primera cita del paciente");

                assertThat(agendaBlock.getAvailable()).isFalse();

                verify(clinicRepository)
                                .findFirstByRecordStatusOrderByIdAsc(
                                                RecordStatus.ACTIVE);

                verify(patientRepository)
                                .findByIdAndRecordStatus(
                                                1L,
                                                RecordStatus.ACTIVE);

                verify(doctorRepository)
                                .findByIdAndRecordStatus(
                                                2L,
                                                RecordStatus.ACTIVE);

                verify(agendaBlockRepository)
                                .findByIdAndRecordStatusForUpdate(
                                                12L,
                                                RecordStatus.ACTIVE);

                verify(appointmentRepository)
                                .existsPatientScheduleConflict(
                                                1L,
                                                agendaBlock.getAppointmentDate(),
                                                agendaBlock.getStartTime(),
                                                agendaBlock.getEndTime(),
                                                java.util.List.of(
                                                                AppointmentStatusCode.PROGRAMADA.name(),
                                                                AppointmentStatusCode.CONFIRMADA.name()),
                                                RecordStatus.ACTIVE);

                verify(appointmentStatusRepository)
                                .findByCodeAndRecordStatus(
                                                AppointmentStatusCode.PROGRAMADA.name(),
                                                RecordStatus.ACTIVE);
        }

        @Test
        void shouldRejectAppointmentWhenPatientHasScheduleConflict() {

                CreateAppointmentRequest request = new CreateAppointmentRequest(
                                1L,
                                2L,
                                12L,
                                "Consulta médica general",
                                null);

                ClinicEntity clinic = createClinic();
                PatientEntity patient = createPatient(clinic);
                DoctorEntity doctor = createDoctor(clinic);
                AgendaBlockEntity agendaBlock = createAgendaBlock(clinic, doctor);

                when(clinicRepository
                                .findFirstByRecordStatusOrderByIdAsc(
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(clinic));

                when(patientRepository
                                .findByIdAndRecordStatus(
                                                1L,
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(patient));

                when(doctorRepository
                                .findByIdAndRecordStatus(
                                                2L,
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(doctor));

                when(agendaBlockRepository
                                .findByIdAndRecordStatusForUpdate(
                                                12L,
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(agendaBlock));

                when(appointmentRepository
                                .existsPatientScheduleConflict(
                                                any(),
                                                any(),
                                                any(),
                                                any(),
                                                any(),
                                                any()))
                                .thenReturn(true);

                assertThatThrownBy(
                                () -> appointmentService.createAppointment(request))
                                .isInstanceOf(ConflictException.class)
                                .hasMessage(
                                                "El paciente ya tiene una cita programada en el horario seleccionado.");

                verify(appointmentRepository, never())
                                .save(any(AppointmentEntity.class));

                verify(appointmentStatusRepository, never())
                                .findByCodeAndRecordStatus(
                                                any(),
                                                any());

                assertThat(agendaBlock.getAvailable()).isTrue();
        }

        @Test
        void shouldFindAppointmentsByDate() {
                LocalDate appointmentDate = LocalDate.of(2026, 7, 30);

                ClinicEntity clinic = createClinic();

                PatientEntity patient = createPatient(clinic);

                DoctorEntity doctor = createDoctorWithSpecialty(clinic);

                AgendaBlockEntity agendaBlock = createAgendaBlock(
                                clinic,
                                doctor,
                                appointmentDate);

                AppointmentStatusEntity appointmentStatus = createAppointmentStatus();

                AppointmentEntity appointment = createAppointment(
                                clinic,
                                patient,
                                doctor,
                                agendaBlock,
                                appointmentStatus);

                when(clinicRepository
                                .findFirstByRecordStatusOrderByIdAsc(
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(clinic));

                when(appointmentRepository.findClinicAppointments(
                                1L,
                                appointmentDate,
                                null,
                                null,
                                RecordStatus.ACTIVE))
                                .thenReturn(List.of(appointment));

                List<AppointmentAgendaResponse> response = appointmentService.findAppointments(
                                appointmentDate,
                                null,
                                null);

                assertThat(response).hasSize(1);

                AppointmentAgendaResponse result = response.getFirst();

                assertThat(result.id()).isEqualTo(20L);

                assertThat(result.patientId()).isEqualTo(1L);
                assertThat(result.patientFirstName()).isEqualTo("María");
                assertThat(result.patientLastName()).isEqualTo("González");

                assertThat(result.doctorId()).isEqualTo(2L);
                assertThat(result.doctorFirstName()).isEqualTo("Ana");
                assertThat(result.doctorLastName()).isEqualTo("Torres");

                assertThat(result.specialtyId()).isEqualTo(3L);
                assertThat(result.specialtyName())
                                .isEqualTo("Medicina general");

                assertThat(result.appointmentDate())
                                .isEqualTo(appointmentDate);

                assertThat(result.startTime())
                                .isEqualTo(LocalTime.of(9, 0));

                assertThat(result.endTime())
                                .isEqualTo(LocalTime.of(9, 30));

                assertThat(result.statusCode())
                                .isEqualTo("PROGRAMADA");

                assertThat(result.statusName())
                                .isEqualTo("Programada");

                assertThat(result.reason())
                                .isEqualTo("Consulta médica general");

                verify(appointmentRepository)
                                .findClinicAppointments(
                                                1L,
                                                appointmentDate,
                                                null,
                                                null,
                                                RecordStatus.ACTIVE);
        }

        @Test
        void shouldFindAppointmentsUsingDoctorAndStatusFilters() {
                LocalDate appointmentDate = LocalDate.of(2026, 7, 30);

                ClinicEntity clinic = createClinic();

                when(clinicRepository
                                .findFirstByRecordStatusOrderByIdAsc(
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(clinic));

                when(appointmentRepository.findClinicAppointments(
                                1L,
                                appointmentDate,
                                2L,
                                "CONFIRMADA",
                                RecordStatus.ACTIVE))
                                .thenReturn(List.of());

                List<AppointmentAgendaResponse> response = appointmentService.findAppointments(
                                appointmentDate,
                                2L,
                                " confirmada ");

                assertThat(response).isEmpty();

                verify(appointmentRepository)
                                .findClinicAppointments(
                                                1L,
                                                appointmentDate,
                                                2L,
                                                "CONFIRMADA",
                                                RecordStatus.ACTIVE);
        }

        @Test
        void shouldReturnEmptyListWhenDateHasNoAppointments() {
                LocalDate appointmentDate = LocalDate.of(2026, 7, 31);

                ClinicEntity clinic = createClinic();

                when(clinicRepository
                                .findFirstByRecordStatusOrderByIdAsc(
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(clinic));

                when(appointmentRepository.findClinicAppointments(
                                1L,
                                appointmentDate,
                                null,
                                null,
                                RecordStatus.ACTIVE))
                                .thenReturn(List.of());

                List<AppointmentAgendaResponse> response = appointmentService.findAppointments(
                                appointmentDate,
                                null,
                                null);

                assertThat(response).isEmpty();
        }

        @Test
        void shouldCancelScheduledAppointment() {
                ClinicEntity clinic = createClinic();
                PatientEntity patient = createPatient(clinic);
                DoctorEntity doctor = createDoctor(clinic);

                AgendaBlockEntity agendaBlock = createAgendaBlock(
                                clinic,
                                doctor,
                                LocalDate.now().plusDays(1));

                AppointmentStatusEntity scheduledStatus = createAppointmentStatus(
                                AppointmentStatusCode.PROGRAMADA,
                                "Programada");

                AppointmentStatusEntity cancelledStatus = createAppointmentStatus(
                                AppointmentStatusCode.CANCELADA,
                                "Cancelada");

                AppointmentEntity appointment = createAppointment(
                                clinic,
                                patient,
                                doctor,
                                agendaBlock,
                                scheduledStatus);

                appointment.setReason("Consulta médica general");
                appointment.setNotes("Primera cita del paciente");

                CancelAppointmentRequest request = new CancelAppointmentRequest(
                                "El paciente no podrá asistir");

                when(clinicRepository
                                .findFirstByRecordStatusOrderByIdAsc(
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(clinic));

                when(appointmentRepository
                                .findByIdAndRecordStatusForUpdate(
                                                20L,
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(appointment));

                when(appointmentStatusRepository
                                .findByCodeAndRecordStatus(
                                                AppointmentStatusCode.CANCELADA.name(),
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(cancelledStatus));

                when(appointmentRepository.save(appointment))
                                .thenReturn(appointment);

                AppointmentResponse response = appointmentService.cancelAppointment(
                                20L,
                                request);

                assertThat(response.id()).isEqualTo(20L);
                assertThat(response.statusCode()).isEqualTo("CANCELADA");
                assertThat(response.statusName()).isEqualTo("Cancelada");

                assertThat(appointment.getStatus())
                                .isSameAs(cancelledStatus);

                assertThat(appointment.getCancellationReason())
                                .isEqualTo("El paciente no podrá asistir");

                assertThat(appointment.getCancelledAt())
                                .isNotNull();

                assertThat(appointment.getAgendaBlock().getAvailable())
                                .isTrue();

                /*
                 * The original medical reason and notes must remain unchanged.
                 */
                assertThat(appointment.getReason())
                                .isEqualTo("Consulta médica general");

                assertThat(appointment.getNotes())
                                .isEqualTo("Primera cita del paciente");

                verify(appointmentRepository)
                                .findByIdAndRecordStatusForUpdate(
                                                20L,
                                                RecordStatus.ACTIVE);

                verify(appointmentStatusRepository)
                                .findByCodeAndRecordStatus(
                                                AppointmentStatusCode.CANCELADA.name(),
                                                RecordStatus.ACTIVE);

                verify(appointmentRepository)
                                .save(appointment);
        }

        @Test
        void shouldCancelConfirmedAppointmentWhenReasonIsProvided() {
                ClinicEntity clinic = createClinic();
                PatientEntity patient = createPatient(clinic);
                DoctorEntity doctor = createDoctor(clinic);

                AgendaBlockEntity agendaBlock = createAgendaBlock(
                                clinic,
                                doctor,
                                LocalDate.now().plusDays(1));

                AppointmentStatusEntity confirmedStatus = createAppointmentStatus(
                                AppointmentStatusCode.CONFIRMADA,
                                "Confirmada");

                AppointmentStatusEntity cancelledStatus = createAppointmentStatus(
                                AppointmentStatusCode.CANCELADA,
                                "Cancelada");

                AppointmentEntity appointment = createAppointment(
                                clinic,
                                patient,
                                doctor,
                                agendaBlock,
                                confirmedStatus);

                CancelAppointmentRequest request = new CancelAppointmentRequest(
                                "  El paciente solicitó la cancelación  ");

                when(clinicRepository
                                .findFirstByRecordStatusOrderByIdAsc(
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(clinic));

                when(appointmentRepository
                                .findByIdAndRecordStatusForUpdate(
                                                20L,
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(appointment));

                when(appointmentStatusRepository
                                .findByCodeAndRecordStatus(
                                                AppointmentStatusCode.CANCELADA.name(),
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(cancelledStatus));

                when(appointmentRepository.save(appointment))
                                .thenReturn(appointment);

                AppointmentResponse response = appointmentService.cancelAppointment(
                                20L,
                                request);

                assertThat(response.statusCode())
                                .isEqualTo("CANCELADA");

                assertThat(appointment.getCancellationReason())
                                .isEqualTo("El paciente solicitó la cancelación");

                assertThat(appointment.getCancelledAt())
                                .isNotNull();

                assertThat(agendaBlock.getAvailable())
                                .isTrue();

                verify(appointmentRepository)
                                .save(appointment);
        }

        @Test
        void shouldRejectConfirmedCancellationWithoutReason() {
                ClinicEntity clinic = createClinic();
                PatientEntity patient = createPatient(clinic);
                DoctorEntity doctor = createDoctor(clinic);

                AgendaBlockEntity agendaBlock = createAgendaBlock(
                                clinic,
                                doctor,
                                LocalDate.now().plusDays(1));

                AppointmentStatusEntity confirmedStatus = createAppointmentStatus(
                                AppointmentStatusCode.CONFIRMADA,
                                "Confirmada");

                AppointmentEntity appointment = createAppointment(
                                clinic,
                                patient,
                                doctor,
                                agendaBlock,
                                confirmedStatus);

                CancelAppointmentRequest request = new CancelAppointmentRequest("   ");

                when(clinicRepository
                                .findFirstByRecordStatusOrderByIdAsc(
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(clinic));

                when(appointmentRepository
                                .findByIdAndRecordStatusForUpdate(
                                                20L,
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(appointment));

                assertThatThrownBy(
                                () -> appointmentService.cancelAppointment(
                                                20L,
                                                request))
                                .isInstanceOf(BadRequestException.class)
                                .hasMessage(
                                                "El motivo de cancelación es obligatorio para una cita confirmada.");

                assertThat(appointment.getStatus())
                                .isSameAs(confirmedStatus);

                assertThat(appointment.getCancellationReason())
                                .isNull();

                assertThat(appointment.getCancelledAt())
                                .isNull();

                assertThat(agendaBlock.getAvailable())
                                .isFalse();

                verify(appointmentStatusRepository, never())
                                .findByCodeAndRecordStatus(
                                                any(),
                                                any());

                verify(appointmentRepository, never())
                                .save(any(AppointmentEntity.class));
        }

        @Test
        void shouldRejectCancellationOfAttendedAppointment() {
                assertCancellationRejectedForStatus(
                                AppointmentStatusCode.ATENDIDA,
                                "Atendida");
        }

        @Test
        void shouldRejectCancellationOfCancelledAppointment() {
                assertCancellationRejectedForStatus(
                                AppointmentStatusCode.CANCELADA,
                                "Cancelada");
        }

        @Test
        void shouldRejectCancellationOfNoShowAppointment() {
                assertCancellationRejectedForStatus(
                                AppointmentStatusCode.NO_ASISTIO,
                                "No asistió");
        }

        @Test
        void shouldRejectUnknownAppointment() {
                ClinicEntity clinic = createClinic();

                CancelAppointmentRequest request = new CancelAppointmentRequest(
                                "El paciente no podrá asistir");

                when(clinicRepository
                                .findFirstByRecordStatusOrderByIdAsc(
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(clinic));

                when(appointmentRepository
                                .findByIdAndRecordStatusForUpdate(
                                                99L,
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.empty());

                assertThatThrownBy(
                                () -> appointmentService.cancelAppointment(
                                                99L,
                                                request))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessage(
                                                "La cita seleccionada no está disponible.");

                verify(appointmentStatusRepository, never())
                                .findByCodeAndRecordStatus(
                                                any(),
                                                any());

                verify(appointmentRepository, never())
                                .save(any(AppointmentEntity.class));
        }

        @Test
        void shouldRescheduleScheduledAppointment() {
                ClinicEntity clinic = createClinic();
                PatientEntity patient = createPatient(clinic);
                DoctorEntity doctor = createDoctor(clinic);

                AgendaBlockEntity previousAgendaBlock = createAgendaBlock(
                                clinic,
                                doctor,
                                LocalDate.now().plusDays(1));

                previousAgendaBlock.setId(12L);
                previousAgendaBlock.setAvailable(false);

                AgendaBlockEntity newAgendaBlock = createAgendaBlock(
                                clinic,
                                doctor,
                                LocalDate.now().plusDays(2));

                newAgendaBlock.setId(13L);
                newAgendaBlock.setStartTime(
                                LocalTime.of(10, 0));
                newAgendaBlock.setEndTime(
                                LocalTime.of(10, 30));
                newAgendaBlock.setAvailable(true);

                AppointmentStatusEntity scheduledStatus = createAppointmentStatus(
                                AppointmentStatusCode.PROGRAMADA,
                                "Programada");

                AppointmentEntity appointment = createAppointment(
                                clinic,
                                patient,
                                doctor,
                                previousAgendaBlock,
                                scheduledStatus);

                RescheduleAppointmentRequest request = new RescheduleAppointmentRequest(13L);

                when(clinicRepository
                                .findFirstByRecordStatusOrderByIdAsc(
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(clinic));

                when(appointmentRepository
                                .findByIdAndRecordStatusForUpdate(
                                                20L,
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(appointment));

                when(agendaBlockRepository
                                .findByIdAndRecordStatusForUpdate(
                                                13L,
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(newAgendaBlock));

                when(appointmentRepository
                                .existsPatientScheduleConflictExcludingAppointment(
                                                20L,
                                                1L,
                                                newAgendaBlock.getAppointmentDate(),
                                                newAgendaBlock.getStartTime(),
                                                newAgendaBlock.getEndTime(),
                                                List.of(
                                                                AppointmentStatusCode.PROGRAMADA.name(),
                                                                AppointmentStatusCode.CONFIRMADA.name()),
                                                RecordStatus.ACTIVE))
                                .thenReturn(false);

                when(appointmentRepository.save(appointment))
                                .thenReturn(appointment);

                when(appointmentRescheduleHistoryRepository.save(
                                any(AppointmentRescheduleHistoryEntity.class)))
                                .thenAnswer(invocation -> invocation.getArgument(0));

                AppointmentResponse response = appointmentService.rescheduleAppointment(
                                20L,
                                request);

                assertThat(response.id())
                                .isEqualTo(20L);

                assertThat(response.agendaBlockId())
                                .isEqualTo(13L);

                assertThat(response.appointmentDate())
                                .isEqualTo(LocalDate.now().plusDays(2));

                assertThat(response.startTime())
                                .isEqualTo(LocalTime.of(10, 0));

                assertThat(response.endTime())
                                .isEqualTo(LocalTime.of(10, 30));

                assertThat(response.statusCode())
                                .isEqualTo("PROGRAMADA");

                assertThat(appointment.getAgendaBlock())
                                .isSameAs(newAgendaBlock);

                assertThat(previousAgendaBlock.getAvailable())
                                .isTrue();

                assertThat(newAgendaBlock.getAvailable())
                                .isFalse();

                ArgumentCaptor<AppointmentRescheduleHistoryEntity> historyCaptor = ArgumentCaptor.forClass(
                                AppointmentRescheduleHistoryEntity.class);

                verify(appointmentRescheduleHistoryRepository)
                                .save(historyCaptor.capture());

                AppointmentRescheduleHistoryEntity history = historyCaptor.getValue();

                assertThat(history.getAppointment())
                                .isSameAs(appointment);

                assertThat(history.getPreviousAgendaBlock())
                                .isSameAs(previousAgendaBlock);

                assertThat(history.getNewAgendaBlock())
                                .isSameAs(newAgendaBlock);

                assertThat(history.getPreviousAppointmentDate())
                                .isEqualTo(LocalDate.now().plusDays(1));

                assertThat(history.getPreviousStartTime())
                                .isEqualTo(LocalTime.of(9, 0));

                assertThat(history.getPreviousEndTime())
                                .isEqualTo(LocalTime.of(9, 30));

                assertThat(history.getNewAppointmentDate())
                                .isEqualTo(LocalDate.now().plusDays(2));

                assertThat(history.getNewStartTime())
                                .isEqualTo(LocalTime.of(10, 0));

                assertThat(history.getNewEndTime())
                                .isEqualTo(LocalTime.of(10, 30));

                verify(appointmentRepository)
                                .save(appointment);
        }

        @Test
        void shouldRejectRescheduleWhenAppointmentIsNotScheduled() {
                ClinicEntity clinic = createClinic();
                PatientEntity patient = createPatient(clinic);
                DoctorEntity doctor = createDoctor(clinic);

                AgendaBlockEntity agendaBlock = createAgendaBlock(
                                clinic,
                                doctor,
                                LocalDate.now().plusDays(1));

                agendaBlock.setAvailable(false);

                AppointmentStatusEntity confirmedStatus = createAppointmentStatus(
                                AppointmentStatusCode.CONFIRMADA,
                                "Confirmada");

                AppointmentEntity appointment = createAppointment(
                                clinic,
                                patient,
                                doctor,
                                agendaBlock,
                                confirmedStatus);

                RescheduleAppointmentRequest request = new RescheduleAppointmentRequest(13L);

                when(clinicRepository
                                .findFirstByRecordStatusOrderByIdAsc(
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(clinic));

                when(appointmentRepository
                                .findByIdAndRecordStatusForUpdate(
                                                20L,
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(appointment));

                assertThatThrownBy(
                                () -> appointmentService.rescheduleAppointment(
                                                20L,
                                                request))
                                .isInstanceOf(ConflictException.class)
                                .hasMessage(
                                                "La cita no puede ser reprogramada en su estado actual.");

                verify(agendaBlockRepository, never())
                                .findByIdAndRecordStatusForUpdate(
                                                any(),
                                                any());

                verify(appointmentRepository, never())
                                .save(any(AppointmentEntity.class));

                verify(appointmentRescheduleHistoryRepository, never())
                                .save(any(AppointmentRescheduleHistoryEntity.class));
        }

        @Test
        void shouldRejectRescheduleToSameAgendaBlock() {
                ClinicEntity clinic = createClinic();
                PatientEntity patient = createPatient(clinic);
                DoctorEntity doctor = createDoctor(clinic);

                AgendaBlockEntity agendaBlock = createAgendaBlock(
                                clinic,
                                doctor,
                                LocalDate.now().plusDays(1));

                agendaBlock.setId(12L);
                agendaBlock.setAvailable(false);

                AppointmentStatusEntity scheduledStatus = createAppointmentStatus(
                                AppointmentStatusCode.PROGRAMADA,
                                "Programada");

                AppointmentEntity appointment = createAppointment(
                                clinic,
                                patient,
                                doctor,
                                agendaBlock,
                                scheduledStatus);

                RescheduleAppointmentRequest request = new RescheduleAppointmentRequest(12L);

                when(clinicRepository
                                .findFirstByRecordStatusOrderByIdAsc(
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(clinic));

                when(appointmentRepository
                                .findByIdAndRecordStatusForUpdate(
                                                20L,
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(appointment));

                when(agendaBlockRepository
                                .findByIdAndRecordStatusForUpdate(
                                                12L,
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(agendaBlock));

                assertThatThrownBy(
                                () -> appointmentService.rescheduleAppointment(
                                                20L,
                                                request))
                                .isInstanceOf(BadRequestException.class)
                                .hasMessage(
                                                "El nuevo bloque de agenda debe ser diferente al bloque actual.");

                assertThat(agendaBlock.getAvailable())
                                .isFalse();

                verify(appointmentRepository, never())
                                .save(any(AppointmentEntity.class));

                verify(appointmentRescheduleHistoryRepository, never())
                                .save(any(AppointmentRescheduleHistoryEntity.class));
        }

        @Test
        void shouldRejectRescheduleWhenPatientHasScheduleConflict() {
                ClinicEntity clinic = createClinic();
                PatientEntity patient = createPatient(clinic);
                DoctorEntity doctor = createDoctor(clinic);

                AgendaBlockEntity previousAgendaBlock = createAgendaBlock(
                                clinic,
                                doctor,
                                LocalDate.now().plusDays(1));

                previousAgendaBlock.setId(12L);
                previousAgendaBlock.setAvailable(false);

                AgendaBlockEntity newAgendaBlock = createAgendaBlock(
                                clinic,
                                doctor,
                                LocalDate.now().plusDays(2));

                newAgendaBlock.setId(13L);
                newAgendaBlock.setAvailable(true);

                AppointmentStatusEntity scheduledStatus = createAppointmentStatus(
                                AppointmentStatusCode.PROGRAMADA,
                                "Programada");

                AppointmentEntity appointment = createAppointment(
                                clinic,
                                patient,
                                doctor,
                                previousAgendaBlock,
                                scheduledStatus);

                RescheduleAppointmentRequest request = new RescheduleAppointmentRequest(13L);

                when(clinicRepository
                                .findFirstByRecordStatusOrderByIdAsc(
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(clinic));

                when(appointmentRepository
                                .findByIdAndRecordStatusForUpdate(
                                                20L,
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(appointment));

                when(agendaBlockRepository
                                .findByIdAndRecordStatusForUpdate(
                                                13L,
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(newAgendaBlock));

                when(appointmentRepository
                                .existsPatientScheduleConflictExcludingAppointment(
                                                any(),
                                                any(),
                                                any(),
                                                any(),
                                                any(),
                                                any(),
                                                any()))
                                .thenReturn(true);

                assertThatThrownBy(
                                () -> appointmentService.rescheduleAppointment(
                                                20L,
                                                request))
                                .isInstanceOf(ConflictException.class)
                                .hasMessage(
                                                "El paciente ya tiene una cita programada en el horario seleccionado.");

                assertThat(appointment.getAgendaBlock())
                                .isSameAs(previousAgendaBlock);

                assertThat(previousAgendaBlock.getAvailable())
                                .isFalse();

                assertThat(newAgendaBlock.getAvailable())
                                .isTrue();

                verify(appointmentRepository, never())
                                .save(any(AppointmentEntity.class));

                verify(appointmentRescheduleHistoryRepository, never())
                                .save(any(AppointmentRescheduleHistoryEntity.class));
        }

        private void assertCancellationRejectedForStatus(
                        AppointmentStatusCode statusCode,
                        String statusName) {
                ClinicEntity clinic = createClinic();
                PatientEntity patient = createPatient(clinic);
                DoctorEntity doctor = createDoctor(clinic);

                AgendaBlockEntity agendaBlock = createAgendaBlock(
                                clinic,
                                doctor,
                                LocalDate.now().plusDays(1));

                AppointmentStatusEntity currentStatus = createAppointmentStatus(
                                statusCode,
                                statusName);

                AppointmentEntity appointment = createAppointment(
                                clinic,
                                patient,
                                doctor,
                                agendaBlock,
                                currentStatus);

                CancelAppointmentRequest request = new CancelAppointmentRequest(
                                "Solicitud de cancelación");

                when(clinicRepository
                                .findFirstByRecordStatusOrderByIdAsc(
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(clinic));

                when(appointmentRepository
                                .findByIdAndRecordStatusForUpdate(
                                                20L,
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(appointment));

                assertThatThrownBy(
                                () -> appointmentService.cancelAppointment(
                                                20L,
                                                request))
                                .isInstanceOf(ConflictException.class)
                                .hasMessage(
                                                "La cita no puede ser cancelada en su estado actual.");

                assertThat(appointment.getStatus())
                                .isSameAs(currentStatus);

                assertThat(appointment.getCancellationReason())
                                .isNull();

                assertThat(appointment.getCancelledAt())
                                .isNull();

                assertThat(agendaBlock.getAvailable())
                                .isFalse();

                verify(appointmentStatusRepository, never())
                                .findByCodeAndRecordStatus(
                                                any(),
                                                any());

                verify(appointmentRepository, never())
                                .save(any(AppointmentEntity.class));
        }

        private DoctorEntity createDoctorWithSpecialty(
                        ClinicEntity clinic) {
                MedicalSpecialtyEntity specialty = new MedicalSpecialtyEntity();

                specialty.setId(3L);
                specialty.setCode("GENERAL");
                specialty.setName("Medicina general");

                DoctorEntity doctor = createDoctor(clinic);

                doctor.setSpecialty(specialty);

                return doctor;
        }

        private AgendaBlockEntity createAgendaBlock(
                        ClinicEntity clinic,
                        DoctorEntity doctor,
                        LocalDate appointmentDate) {
                MedicalAgendaEntity medicalAgenda = new MedicalAgendaEntity();

                medicalAgenda.setId(5L);
                medicalAgenda.setClinic(clinic);
                medicalAgenda.setDoctor(doctor);
                medicalAgenda.setName("Agenda de Ana Torres");
                medicalAgenda.setActive(true);

                AgendaBlockEntity agendaBlock = new AgendaBlockEntity();

                agendaBlock.setId(12L);
                agendaBlock.setMedicalAgenda(medicalAgenda);
                agendaBlock.setAppointmentDate(appointmentDate);
                agendaBlock.setStartTime(LocalTime.of(9, 0));
                agendaBlock.setEndTime(LocalTime.of(9, 30));
                agendaBlock.setAvailable(false);

                return agendaBlock;
        }

        private AppointmentEntity createAppointment(
                        ClinicEntity clinic,
                        PatientEntity patient,
                        DoctorEntity doctor,
                        AgendaBlockEntity agendaBlock,
                        AppointmentStatusEntity status) {
                AppointmentEntity appointment = new AppointmentEntity();

                appointment.setId(20L);
                appointment.setClinic(clinic);
                appointment.setPatient(patient);
                appointment.setDoctor(doctor);
                appointment.setAgendaBlock(agendaBlock);
                appointment.setStatus(status);
                appointment.setReason(
                                "Consulta médica general");

                return appointment;
        }

        private ClinicEntity createClinic() {
                ClinicEntity clinic = new ClinicEntity();

                clinic.setId(1L);

                return clinic;
        }

        private PatientEntity createPatient(
                        ClinicEntity clinic) {
                PatientEntity patient = new PatientEntity();

                patient.setId(1L);
                patient.setClinic(clinic);
                patient.setFirstName("María");
                patient.setLastName("González");

                return patient;
        }

        private DoctorEntity createDoctor(
                        ClinicEntity clinic) {
                DoctorEntity doctor = new DoctorEntity();

                doctor.setId(2L);
                doctor.setClinic(clinic);
                doctor.setFirstName("Ana");
                doctor.setLastName("Torres");

                return doctor;
        }

        private AgendaBlockEntity createAgendaBlock(
                        ClinicEntity clinic,
                        DoctorEntity doctor) {
                MedicalAgendaEntity medicalAgenda = new MedicalAgendaEntity();

                medicalAgenda.setId(5L);
                medicalAgenda.setClinic(clinic);
                medicalAgenda.setDoctor(doctor);
                medicalAgenda.setName("Agenda de Ana Torres");
                medicalAgenda.setActive(true);

                AgendaBlockEntity agendaBlock = new AgendaBlockEntity();

                agendaBlock.setId(12L);
                agendaBlock.setMedicalAgenda(medicalAgenda);
                agendaBlock.setAppointmentDate(
                                LocalDate.now().plusDays(1));
                agendaBlock.setStartTime(LocalTime.of(9, 0));
                agendaBlock.setEndTime(LocalTime.of(9, 30));
                agendaBlock.setAvailable(true);

                return agendaBlock;
        }

        private AppointmentStatusEntity createAppointmentStatus() {
                AppointmentStatusEntity appointmentStatus = createAppointmentStatus(
                                AppointmentStatusCode.PROGRAMADA,
                                "Programada");

                appointmentStatus.setDescription(
                                "Cita médica registrada y pendiente de confirmación.");

                return appointmentStatus;
        }

        private AppointmentStatusEntity createAppointmentStatus(
                        AppointmentStatusCode statusCode,
                        String statusName) {
                AppointmentStatusEntity appointmentStatus = new AppointmentStatusEntity();

                appointmentStatus.setId(
                                statusCode.ordinal() + 1L);

                appointmentStatus.setCode(
                                statusCode.name());

                appointmentStatus.setName(
                                statusName);

                return appointmentStatus;
        }
}