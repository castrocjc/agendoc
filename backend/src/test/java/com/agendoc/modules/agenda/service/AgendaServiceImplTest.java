package com.agendoc.modules.agenda.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.common.exception.BadRequestException;
import com.agendoc.common.exception.ResourceNotFoundException;
import com.agendoc.modules.agenda.dto.AgendaBlockResponse;
import com.agendoc.modules.agenda.entity.AgendaBlockEntity;
import com.agendoc.modules.agenda.entity.MedicalAgendaEntity;
import com.agendoc.modules.agenda.repository.AgendaBlockRepository;
import com.agendoc.modules.agenda.repository.MedicalAgendaRepository;
import com.agendoc.modules.clinic.entity.ClinicEntity;
import com.agendoc.modules.clinic.repository.ClinicRepository;
import com.agendoc.modules.doctor.entity.DoctorEntity;
import com.agendoc.modules.doctor.repository.DoctorRepository;
import com.agendoc.security.authorization.AuthenticatedUserAuthorization;
import com.agendoc.security.authorization.SecurityRoleCode;
import com.agendoc.security.context.AuthenticatedUserContext;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AgendaServiceImplTest {

        @Mock
        private MedicalAgendaRepository medicalAgendaRepository;

        @Mock
        private AgendaBlockRepository agendaBlockRepository;

        @Mock
        private DoctorRepository doctorRepository;

        @Mock
        private ClinicRepository clinicRepository;

        @Mock
        private AuthenticatedUserAuthorization authenticatedUserAuthorization;

        private AuthenticatedUserContext patientContext;

        private AgendaServiceImpl agendaService;

        @BeforeEach
        void setUp() {

                agendaService = new AgendaServiceImpl(
                                medicalAgendaRepository,
                                agendaBlockRepository,
                                doctorRepository,
                                clinicRepository,
                                authenticatedUserAuthorization);

                patientContext = new AuthenticatedUserContext(
                                200L,
                                "patient.user",
                                1L,
                                SecurityRoleCode.PATIENT.name(),
                                50L,
                                null);

                when(authenticatedUserAuthorization.requireAnyRole(
                                SecurityRoleCode.PATIENT,
                                SecurityRoleCode.RECEPTIONIST)).thenReturn(patientContext);
        }

        @Test
        void shouldReturnAvailableAgendaBlocksOrderedByStartTime() {

                LocalDate appointmentDate = LocalDate.now().plusDays(1);

                ClinicEntity clinic = createClinic(1L);
                DoctorEntity doctor = createDoctor(10L, clinic);
                MedicalAgendaEntity medicalAgenda = createMedicalAgenda(20L, doctor, clinic);

                AgendaBlockEntity firstBlock = createAgendaBlock(
                                100L,
                                medicalAgenda,
                                appointmentDate,
                                LocalTime.of(9, 0),
                                LocalTime.of(9, 30),
                                true);

                AgendaBlockEntity secondBlock = createAgendaBlock(
                                101L,
                                medicalAgenda,
                                appointmentDate,
                                LocalTime.of(9, 30),
                                LocalTime.of(10, 0),
                                true);

                when(doctorRepository.findByIdAndClinicIdAndRecordStatus(
                                10L,
                                patientContext.clinicId(),
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(doctor));

                when(medicalAgendaRepository.findByDoctorIdAndRecordStatus(
                                10L,
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(medicalAgenda));

                when(agendaBlockRepository
                                .findByMedicalAgendaIdAndAppointmentDateAndAvailableTrueAndRecordStatusOrderByStartTimeAsc(
                                                20L,
                                                appointmentDate,
                                                RecordStatus.ACTIVE))
                                .thenReturn(List.of(firstBlock, secondBlock));

                List<AgendaBlockResponse> response = agendaService.findAgendaBlocks(
                                10L,
                                appointmentDate);

                verify(authenticatedUserAuthorization)
                                .requireAnyRole(
                                                SecurityRoleCode.PATIENT,
                                                SecurityRoleCode.RECEPTIONIST);

                verify(doctorRepository)
                                .findByIdAndClinicIdAndRecordStatus(
                                                10L,
                                                patientContext.clinicId(),
                                                RecordStatus.ACTIVE);

                assertThat(response).hasSize(2);

                assertThat(response.get(0).id()).isEqualTo(100L);
                assertThat(response.get(0).appointmentDate())
                                .isEqualTo(appointmentDate);
                assertThat(response.get(0).startTime())
                                .isEqualTo(LocalTime.of(9, 0));
                assertThat(response.get(0).endTime())
                                .isEqualTo(LocalTime.of(9, 30));
                assertThat(response.get(0).available()).isTrue();

                assertThat(response.get(1).id()).isEqualTo(101L);
                assertThat(response.get(1).startTime())
                                .isEqualTo(LocalTime.of(9, 30));
                assertThat(response.get(1).endTime())
                                .isEqualTo(LocalTime.of(10, 0));
                assertThat(response.get(1).available()).isTrue();
        }

        @Test
        void shouldReturnEmptyListWhenDoctorDoesNotHaveMedicalAgenda() {

                LocalDate appointmentDate = LocalDate.now().plusDays(1);

                ClinicEntity clinic = createClinic(1L);
                DoctorEntity doctor = createDoctor(10L, clinic);

                when(doctorRepository.findByIdAndClinicIdAndRecordStatus(
                                10L,
                                patientContext.clinicId(),
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(doctor));

                when(medicalAgendaRepository.findByDoctorIdAndRecordStatus(
                                10L,
                                RecordStatus.ACTIVE)).thenReturn(Optional.empty());

                List<AgendaBlockResponse> response = agendaService.findAgendaBlocks(
                                10L,
                                appointmentDate);

                assertThat(response).isEmpty();

                verify(agendaBlockRepository, never())
                                .findByMedicalAgendaIdAndAppointmentDateAndAvailableTrueAndRecordStatusOrderByStartTimeAsc(
                                                20L,
                                                appointmentDate,
                                                RecordStatus.ACTIVE);
        }

        @Test
        void shouldReturnEmptyListWhenNoAvailableBlocksExistForDate() {

                LocalDate appointmentDate = LocalDate.now().plusDays(1);

                ClinicEntity clinic = createClinic(1L);
                DoctorEntity doctor = createDoctor(10L, clinic);
                MedicalAgendaEntity medicalAgenda = createMedicalAgenda(20L, doctor, clinic);

                when(doctorRepository.findByIdAndClinicIdAndRecordStatus(
                                10L,
                                patientContext.clinicId(),
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(doctor));

                when(medicalAgendaRepository.findByDoctorIdAndRecordStatus(
                                10L,
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(medicalAgenda));

                when(agendaBlockRepository
                                .findByMedicalAgendaIdAndAppointmentDateAndAvailableTrueAndRecordStatusOrderByStartTimeAsc(
                                                20L,
                                                appointmentDate,
                                                RecordStatus.ACTIVE))
                                .thenReturn(List.of());

                List<AgendaBlockResponse> response = agendaService.findAgendaBlocks(
                                10L,
                                appointmentDate);

                assertThat(response).isEmpty();
        }

        @Test
        void shouldRejectAvailabilityQueryForPastDate() {

                LocalDate pastDate = LocalDate.now().minusDays(1);

                assertThatThrownBy(() -> agendaService.findAgendaBlocks(
                                10L,
                                pastDate))
                                .isInstanceOf(BadRequestException.class)
                                .hasMessage(
                                                "No se puede consultar disponibilidad para una fecha pasada.");

                verify(doctorRepository, never())
                                .findByIdAndClinicIdAndRecordStatus(
                                                10L,
                                                patientContext.clinicId(),
                                                RecordStatus.ACTIVE);

                verify(medicalAgendaRepository, never())
                                .findByDoctorIdAndRecordStatus(
                                                10L,
                                                RecordStatus.ACTIVE);
        }

        @Test
        void shouldRejectAvailabilityQueryWhenDoctorDoesNotExist() {

                LocalDate appointmentDate = LocalDate.now().plusDays(1);

                when(doctorRepository.findByIdAndClinicIdAndRecordStatus(
                                10L,
                                patientContext.clinicId(),
                                RecordStatus.ACTIVE)).thenReturn(Optional.empty());

                assertThatThrownBy(() -> agendaService.findAgendaBlocks(
                                10L,
                                appointmentDate))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessage(
                                                "El médico seleccionado no está disponible.");

                verify(medicalAgendaRepository, never())
                                .findByDoctorIdAndRecordStatus(
                                                10L,
                                                RecordStatus.ACTIVE);
        }

        @Test
        void shouldRejectAvailabilityQueryWhenDoctorBelongsToAnotherClinic() {

                LocalDate appointmentDate = LocalDate.now().plusDays(1);

                when(doctorRepository.findByIdAndClinicIdAndRecordStatus(
                                10L,
                                patientContext.clinicId(),
                                RecordStatus.ACTIVE)).thenReturn(Optional.empty());

                assertThatThrownBy(() -> agendaService.findAgendaBlocks(
                                10L,
                                appointmentDate))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessage(
                                                "El médico seleccionado no está disponible.");

                verify(medicalAgendaRepository, never())
                                .findByDoctorIdAndRecordStatus(
                                                10L,
                                                RecordStatus.ACTIVE);
        }

        private ClinicEntity createClinic(Long clinicId) {
                ClinicEntity clinic = new ClinicEntity();

                clinic.setId(clinicId);
                clinic.setName("AgenDoc Clinic");
                clinic.setRecordStatus(RecordStatus.ACTIVE);

                return clinic;
        }

        private DoctorEntity createDoctor(
                        Long doctorId,
                        ClinicEntity clinic) {
                DoctorEntity doctor = new DoctorEntity();

                doctor.setId(doctorId);
                doctor.setClinic(clinic);
                doctor.setFirstName("Ana");
                doctor.setLastName("Torres");
                doctor.setRecordStatus(RecordStatus.ACTIVE);

                return doctor;
        }

        private MedicalAgendaEntity createMedicalAgenda(
                        Long medicalAgendaId,
                        DoctorEntity doctor,
                        ClinicEntity clinic) {
                MedicalAgendaEntity medicalAgenda = new MedicalAgendaEntity();

                medicalAgenda.setId(medicalAgendaId);
                medicalAgenda.setDoctor(doctor);
                medicalAgenda.setClinic(clinic);
                medicalAgenda.setName("Agenda de Ana Torres");
                medicalAgenda.setActive(true);
                medicalAgenda.setRecordStatus(RecordStatus.ACTIVE);

                return medicalAgenda;
        }

        private AgendaBlockEntity createAgendaBlock(
                        Long agendaBlockId,
                        MedicalAgendaEntity medicalAgenda,
                        LocalDate appointmentDate,
                        LocalTime startTime,
                        LocalTime endTime,
                        boolean available) {
                AgendaBlockEntity agendaBlock = new AgendaBlockEntity();

                agendaBlock.setId(agendaBlockId);
                agendaBlock.setMedicalAgenda(medicalAgenda);
                agendaBlock.setAppointmentDate(appointmentDate);
                agendaBlock.setStartTime(startTime);
                agendaBlock.setEndTime(endTime);
                agendaBlock.setAvailable(available);
                agendaBlock.setRecordStatus(RecordStatus.ACTIVE);

                return agendaBlock;
        }
}