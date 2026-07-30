package com.agendoc.modules.appointment.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agendoc.common.exception.ConflictException;
import com.agendoc.common.entity.RecordStatus;
import com.agendoc.modules.agenda.entity.AgendaBlockEntity;
import com.agendoc.modules.agenda.entity.MedicalAgendaEntity;
import com.agendoc.modules.agenda.repository.AgendaBlockRepository;
import com.agendoc.modules.appointment.dto.AppointmentResponse;
import com.agendoc.modules.appointment.dto.CreateAppointmentRequest;
import com.agendoc.modules.appointment.entity.AppointmentEntity;
import com.agendoc.modules.appointment.entity.AppointmentStatusCode;
import com.agendoc.modules.appointment.entity.AppointmentStatusEntity;
import com.agendoc.modules.appointment.repository.AppointmentRepository;
import com.agendoc.modules.appointment.repository.AppointmentStatusRepository;
import com.agendoc.modules.clinic.entity.ClinicEntity;
import com.agendoc.modules.clinic.repository.ClinicRepository;
import com.agendoc.modules.doctor.entity.DoctorEntity;
import com.agendoc.modules.doctor.repository.DoctorRepository;
import com.agendoc.modules.patient.entity.PatientEntity;
import com.agendoc.modules.patient.repository.PatientRepository;
import com.agendoc.modules.appointment.dto.AppointmentAgendaResponse;
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

        private AppointmentServiceImpl appointmentService;

        @BeforeEach
        void setUp() {
                appointmentService = new AppointmentServiceImpl(
                                appointmentRepository,
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
                AppointmentStatusEntity appointmentStatus = new AppointmentStatusEntity();

                appointmentStatus.setId(1L);
                appointmentStatus.setCode(
                                AppointmentStatusCode.PROGRAMADA.name());
                appointmentStatus.setName("Programada");
                appointmentStatus.setDescription(
                                "Cita médica registrada y pendiente de confirmación.");

                return appointmentStatus;
        }
}