package com.agendoc.modules.publicportal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.common.exception.ConflictException;
import com.agendoc.common.exception.ResourceNotFoundException;
import com.agendoc.modules.agenda.entity.AgendaBlockEntity;
import com.agendoc.modules.agenda.entity.MedicalAgendaEntity;
import com.agendoc.modules.agenda.repository.AgendaBlockRepository;
import com.agendoc.modules.agenda.repository.MedicalAgendaRepository;
import com.agendoc.modules.clinic.entity.ClinicEntity;
import com.agendoc.modules.clinic.repository.ClinicRepository;
import com.agendoc.modules.doctor.entity.MedicalSpecialtyEntity;
import com.agendoc.modules.doctor.entity.DoctorEntity;
import com.agendoc.modules.doctor.repository.DoctorRepository;
import com.agendoc.modules.doctor.repository.MedicalSpecialtyRepository;
import com.agendoc.modules.publicportal.dto.FirstAppointmentRequest;
import com.agendoc.modules.publicportal.dto.FirstAppointmentResponse;
import com.agendoc.modules.publicportal.dto.PublicClinicResponse;
import com.agendoc.modules.publicportal.dto.PublicSpecialtyResponse;
import com.agendoc.modules.publicportal.dto.PublicDoctorSummaryResponse;
import com.agendoc.modules.publicportal.dto.PublicAgendaAvailabilityResponse;
import com.agendoc.modules.appointment.entity.AppointmentEntity;
import com.agendoc.modules.appointment.entity.AppointmentStatusCode;
import com.agendoc.modules.appointment.entity.AppointmentStatusEntity;
import com.agendoc.modules.appointment.repository.AppointmentRepository;
import com.agendoc.modules.appointment.repository.AppointmentStatusRepository;
import com.agendoc.modules.patient.entity.PatientEntity;
import com.agendoc.modules.patient.repository.PatientRepository;
import com.agendoc.modules.role.entity.RoleEntity;
import com.agendoc.modules.role.repository.RoleRepository;
import com.agendoc.modules.user.entity.UserEntity;
import com.agendoc.modules.user.repository.UserRepository;
import com.agendoc.security.authorization.SecurityRoleCode;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class PublicReceptionServiceImplTest {

        private static final String CLINIC_SLUG = "agendoc-development-clinic";

        @Mock
        private ClinicRepository clinicRepository;

        @Mock
        private MedicalSpecialtyRepository medicalSpecialtyRepository;

        @Mock
        private DoctorRepository doctorRepository;

        @Mock
        private MedicalAgendaRepository medicalAgendaRepository;

        @Mock
        private AgendaBlockRepository agendaBlockRepository;

        @Mock
        private UserRepository userRepository;

        @Mock
        private PatientRepository patientRepository;

        @Mock
        private RoleRepository roleRepository;

        @Mock
        private AppointmentRepository appointmentRepository;

        @Mock
        private AppointmentStatusRepository appointmentStatusRepository;

        @Mock
        private PasswordEncoder passwordEncoder;

        private PublicReceptionServiceImpl service;

        @BeforeEach
        void setUp() {
                service = new PublicReceptionServiceImpl(
                                clinicRepository,
                                medicalSpecialtyRepository,
                                doctorRepository,
                                medicalAgendaRepository,
                                agendaBlockRepository,
                                userRepository,
                                patientRepository,
                                roleRepository,
                                appointmentRepository,
                                appointmentStatusRepository,
                                passwordEncoder);
        }

        @Test
        void shouldReturnPublicClinicWhenSlugExists() {
                ClinicEntity clinic = createPublicClinic();

                when(clinicRepository.findBySlugAndRecordStatus(
                                CLINIC_SLUG,
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(clinic));

                PublicClinicResponse response = service.getClinic(CLINIC_SLUG);

                assertEquals(CLINIC_SLUG, response.slug());
                assertEquals(
                                "AgenDoc Public Clinic",
                                response.publicName());
                assertEquals(
                                "Medical care for development.",
                                response.publicDescription());
                assertEquals(
                                "https://cdn.agendoc.local/logo.png",
                                response.logoUrl());
                assertEquals(
                                "+52 55 0000 0000",
                                response.phone());
                assertEquals(
                                "+52 55 1111 1111",
                                response.whatsapp());
                assertEquals(
                                "contacto@agendoc.local",
                                response.email());
                assertEquals(
                                "Ciudad de México",
                                response.address());
                assertEquals(
                                "https://maps.example.com/agendoc",
                                response.mapUrl());
        }

        @Test
        void shouldUsePublicNameWhenItExists() {
                ClinicEntity clinic = createPublicClinic();
                clinic.setName("Administrative Clinic Name");
                clinic.setPublicName("Public Clinic Name");

                when(clinicRepository.findBySlugAndRecordStatus(
                                CLINIC_SLUG,
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(clinic));

                PublicClinicResponse response = service.getClinic(CLINIC_SLUG);

                assertEquals(
                                "Public Clinic Name",
                                response.publicName());
        }

        @Test
        void shouldUseClinicNameWhenPublicNameIsNull() {
                ClinicEntity clinic = createPublicClinic();
                clinic.setName("Administrative Clinic Name");
                clinic.setPublicName(null);

                when(clinicRepository.findBySlugAndRecordStatus(
                                CLINIC_SLUG,
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(clinic));

                PublicClinicResponse response = service.getClinic(CLINIC_SLUG);

                assertEquals(
                                "Administrative Clinic Name",
                                response.publicName());
        }

        @Test
        void shouldThrowWhenClinicSlugDoesNotExist() {
                when(clinicRepository.findBySlugAndRecordStatus(
                                CLINIC_SLUG,
                                RecordStatus.ACTIVE)).thenReturn(Optional.empty());

                assertThrows(
                                ResourceNotFoundException.class,
                                () -> service.getClinic(CLINIC_SLUG));
        }

        @Test
        void shouldThrowWhenPublicPortalIsDisabled() {
                ClinicEntity clinic = createPublicClinic();
                clinic.setPublicPortalEnabled(false);

                when(clinicRepository.findBySlugAndRecordStatus(
                                CLINIC_SLUG,
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(clinic));

                assertThrows(
                                ResourceNotFoundException.class,
                                () -> service.getClinic(CLINIC_SLUG));
        }

        @Test
        void shouldNormalizeClinicSlug() {
                ClinicEntity clinic = createPublicClinic();

                when(clinicRepository.findBySlugAndRecordStatus(
                                CLINIC_SLUG,
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(clinic));

                service.getClinic(
                                "  AGENDOC-DEVELOPMENT-CLINIC  ");

                verify(clinicRepository)
                                .findBySlugAndRecordStatus(
                                                CLINIC_SLUG,
                                                RecordStatus.ACTIVE);
        }

        @Test
        void shouldReturnNullForBlankOptionalFields() {
                ClinicEntity clinic = createPublicClinic();

                clinic.setPublicDescription("   ");
                clinic.setLogoUrl("");
                clinic.setPhone("  ");
                clinic.setWhatsapp(null);
                clinic.setEmail("");
                clinic.setAddress("   ");
                clinic.setMapUrl(null);

                when(clinicRepository.findBySlugAndRecordStatus(
                                CLINIC_SLUG,
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(clinic));

                PublicClinicResponse response = service.getClinic(CLINIC_SLUG);

                assertNull(response.publicDescription());
                assertNull(response.logoUrl());
                assertNull(response.phone());
                assertNull(response.whatsapp());
                assertNull(response.email());
                assertNull(response.address());
                assertNull(response.mapUrl());
        }

        @Test
        void shouldThrowWhenClinicSlugIsBlank() {
                assertThrows(
                                ResourceNotFoundException.class,
                                () -> service.getClinic("   "));
        }

        @Test
        void shouldReturnClinicSpecialties() {
                ClinicEntity clinic = createPublicClinic();
                clinic.setId(1L);

                MedicalSpecialtyEntity cardiology = createSpecialty(
                                10L,
                                "Cardiología");

                MedicalSpecialtyEntity dermatology = createSpecialty(
                                20L,
                                "Dermatología");

                when(clinicRepository.findBySlugAndRecordStatus(
                                CLINIC_SLUG,
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(clinic));

                when(medicalSpecialtyRepository
                                .findAllAssociatedWithActiveDoctorsByClinicId(
                                                clinic.getId(),
                                                RecordStatus.ACTIVE))
                                .thenReturn(List.of(
                                                cardiology,
                                                dermatology));

                List<PublicSpecialtyResponse> response = service.getSpecialties(CLINIC_SLUG);

                assertEquals(2, response.size());

                assertEquals(10L, response.get(0).id());
                assertEquals(
                                "Cardiología",
                                response.get(0).name());

                assertEquals(20L, response.get(1).id());
                assertEquals(
                                "Dermatología",
                                response.get(1).name());
        }

        @Test
        void shouldReturnEmptySpecialtyListWhenClinicHasNoSpecialties() {
                ClinicEntity clinic = createPublicClinic();
                clinic.setId(1L);

                when(clinicRepository.findBySlugAndRecordStatus(
                                CLINIC_SLUG,
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(clinic));

                when(medicalSpecialtyRepository
                                .findAllAssociatedWithActiveDoctorsByClinicId(
                                                clinic.getId(),
                                                RecordStatus.ACTIVE))
                                .thenReturn(List.of());

                List<PublicSpecialtyResponse> response = service.getSpecialties(CLINIC_SLUG);

                assertTrue(response.isEmpty());
        }

        @Test
        void shouldNormalizeClinicSlugBeforeFindingSpecialties() {
                ClinicEntity clinic = createPublicClinic();
                clinic.setId(1L);

                when(clinicRepository.findBySlugAndRecordStatus(
                                CLINIC_SLUG,
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(clinic));

                when(medicalSpecialtyRepository
                                .findAllAssociatedWithActiveDoctorsByClinicId(
                                                clinic.getId(),
                                                RecordStatus.ACTIVE))
                                .thenReturn(List.of());

                service.getSpecialties(
                                "  AGENDOC-DEVELOPMENT-CLINIC  ");

                verify(clinicRepository)
                                .findBySlugAndRecordStatus(
                                                CLINIC_SLUG,
                                                RecordStatus.ACTIVE);

                verify(medicalSpecialtyRepository)
                                .findAllAssociatedWithActiveDoctorsByClinicId(
                                                clinic.getId(),
                                                RecordStatus.ACTIVE);
        }

        @Test
        void shouldReturnClinicDoctorsWithoutSpecialtyFilter() {
                ClinicEntity clinic = createPublicClinic();
                clinic.setId(1L);

                MedicalSpecialtyEntity cardiology = createSpecialty(10L, "Cardiología");

                DoctorEntity doctor = createDoctor(
                                100L,
                                "María",
                                "López",
                                cardiology);

                when(clinicRepository.findBySlugAndRecordStatus(
                                CLINIC_SLUG,
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(clinic));

                when(doctorRepository
                                .findAllByClinicIdAndRecordStatusOrderByLastNameAscFirstNameAsc(
                                                clinic.getId(),
                                                RecordStatus.ACTIVE))
                                .thenReturn(List.of(doctor));

                List<PublicDoctorSummaryResponse> response = service.getDoctors(
                                CLINIC_SLUG,
                                null);

                assertEquals(1, response.size());
                assertEquals(100L, response.get(0).id());
                assertEquals("María", response.get(0).firstName());
                assertEquals("López", response.get(0).lastName());
                assertEquals(10L, response.get(0).specialtyId());
                assertEquals(
                                "Cardiología",
                                response.get(0).specialtyName());
        }

        @Test
        void shouldReturnClinicDoctorsFilteredBySpecialty() {
                ClinicEntity clinic = createPublicClinic();
                clinic.setId(1L);

                MedicalSpecialtyEntity dermatology = createSpecialty(20L, "Dermatología");

                DoctorEntity doctor = createDoctor(
                                200L,
                                "Ana",
                                "Torres",
                                dermatology);

                when(clinicRepository.findBySlugAndRecordStatus(
                                CLINIC_SLUG,
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(clinic));

                when(doctorRepository
                                .findAllByClinicIdAndSpecialtyIdAndRecordStatusOrderByLastNameAscFirstNameAsc(
                                                clinic.getId(),
                                                dermatology.getId(),
                                                RecordStatus.ACTIVE))
                                .thenReturn(List.of(doctor));

                List<PublicDoctorSummaryResponse> response = service.getDoctors(
                                CLINIC_SLUG,
                                dermatology.getId());

                assertEquals(1, response.size());
                assertEquals(200L, response.get(0).id());
                assertEquals("Ana", response.get(0).firstName());
                assertEquals("Torres", response.get(0).lastName());
                assertEquals(20L, response.get(0).specialtyId());
                assertEquals(
                                "Dermatología",
                                response.get(0).specialtyName());
        }

        @Test
        void shouldReturnEmptyDoctorListWhenClinicHasNoDoctors() {
                ClinicEntity clinic = createPublicClinic();
                clinic.setId(1L);

                when(clinicRepository.findBySlugAndRecordStatus(
                                CLINIC_SLUG,
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(clinic));

                when(doctorRepository
                                .findAllByClinicIdAndRecordStatusOrderByLastNameAscFirstNameAsc(
                                                clinic.getId(),
                                                RecordStatus.ACTIVE))
                                .thenReturn(List.of());

                List<PublicDoctorSummaryResponse> response = service.getDoctors(
                                CLINIC_SLUG,
                                null);

                assertTrue(response.isEmpty());
        }

        @Test
        void shouldNormalizeClinicSlugBeforeFindingDoctors() {
                ClinicEntity clinic = createPublicClinic();
                clinic.setId(1L);

                when(clinicRepository.findBySlugAndRecordStatus(
                                CLINIC_SLUG,
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(clinic));

                when(doctorRepository
                                .findAllByClinicIdAndRecordStatusOrderByLastNameAscFirstNameAsc(
                                                clinic.getId(),
                                                RecordStatus.ACTIVE))
                                .thenReturn(List.of());

                service.getDoctors(
                                "  AGENDOC-DEVELOPMENT-CLINIC  ",
                                null);

                verify(clinicRepository)
                                .findBySlugAndRecordStatus(
                                                CLINIC_SLUG,
                                                RecordStatus.ACTIVE);

                verify(doctorRepository)
                                .findAllByClinicIdAndRecordStatusOrderByLastNameAscFirstNameAsc(
                                                clinic.getId(),
                                                RecordStatus.ACTIVE);
        }

        @Test
        void shouldReturnAvailableAgendaBlocks() {
                ClinicEntity clinic = createPublicClinic();
                clinic.setId(1L);

                MedicalSpecialtyEntity specialty = createSpecialty(10L, "Cardiología");

                DoctorEntity doctor = createDoctor(100L, "María", "López", specialty);

                MedicalAgendaEntity agenda = createMedicalAgenda(1000L, clinic, doctor);

                AgendaBlockEntity block = createAgendaBlock(
                                5000L,
                                agenda,
                                LocalDate.now().plusDays(1),
                                LocalTime.of(9, 0),
                                LocalTime.of(9, 30));

                when(clinicRepository.findBySlugAndRecordStatus(
                                CLINIC_SLUG,
                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(clinic));

                when(doctorRepository.findByIdAndClinicIdAndRecordStatus(
                                doctor.getId(),
                                clinic.getId(),
                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(doctor));

                when(medicalAgendaRepository
                                .findByDoctorIdAndClinicIdAndActiveTrueAndRecordStatus(
                                                doctor.getId(),
                                                clinic.getId(),
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(agenda));

                when(agendaBlockRepository
                                .findByMedicalAgendaIdAndAppointmentDateAndAvailableTrueAndRecordStatusOrderByStartTimeAsc(
                                                agenda.getId(),
                                                block.getAppointmentDate(),
                                                RecordStatus.ACTIVE))
                                .thenReturn(List.of(block));

                List<PublicAgendaAvailabilityResponse> response = service.getAvailability(
                                CLINIC_SLUG,
                                doctor.getId(),
                                block.getAppointmentDate());

                assertEquals(1, response.size());
                assertEquals(5000L, response.get(0).agendaBlockId());
        }

        @Test
        void shouldReturnEmptyAvailabilityWhenDoctorHasNoAgenda() {
                ClinicEntity clinic = createPublicClinic();
                clinic.setId(1L);

                MedicalSpecialtyEntity specialty = createSpecialty(10L, "Cardiología");

                DoctorEntity doctor = createDoctor(100L, "María", "López", specialty);

                when(clinicRepository.findBySlugAndRecordStatus(
                                CLINIC_SLUG,
                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(clinic));

                when(doctorRepository.findByIdAndClinicIdAndRecordStatus(
                                doctor.getId(),
                                clinic.getId(),
                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(doctor));

                when(medicalAgendaRepository
                                .findByDoctorIdAndClinicIdAndActiveTrueAndRecordStatus(
                                                doctor.getId(),
                                                clinic.getId(),
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.empty());

                List<PublicAgendaAvailabilityResponse> response = service.getAvailability(
                                CLINIC_SLUG,
                                doctor.getId(),
                                LocalDate.now().plusDays(1));

                assertTrue(response.isEmpty());
        }

        @Test
        void shouldCreateFirstAppointmentSuccessfully() {
                ClinicEntity clinic = createPublicClinic();
                clinic.setId(1L);

                MedicalSpecialtyEntity specialty = createSpecialty(
                                10L,
                                "Cardiología");

                DoctorEntity doctor = createDoctor(
                                100L,
                                "María",
                                "López",
                                specialty);
                doctor.setClinic(clinic);

                MedicalAgendaEntity agenda = createMedicalAgenda(
                                1000L,
                                clinic,
                                doctor);

                AgendaBlockEntity agendaBlock = createAgendaBlock(
                                5000L,
                                agenda,
                                LocalDate.now().plusDays(1),
                                LocalTime.of(9, 0),
                                LocalTime.of(9, 30));

                RoleEntity patientRole = createRole(
                                20L,
                                SecurityRoleCode.PATIENT.name(),
                                "Paciente");

                AppointmentStatusEntity programmedStatus = createAppointmentStatus(
                                30L,
                                AppointmentStatusCode.PROGRAMADA.name(),
                                "Programada");

                FirstAppointmentRequest request = new FirstAppointmentRequest(
                                "  Juan Carlos  ",
                                "  Castro Cruz  ",
                                "  JUAN.CASTRO@EXAMPLE.COM  ",
                                "  +52 55 1234 5678  ",
                                "Password123!",
                                "Password123!",
                                doctor.getId(),
                                agendaBlock.getId(),
                                "  Consulta inicial  ");

                when(clinicRepository.findBySlugAndRecordStatus(
                                CLINIC_SLUG,
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(clinic));

                when(userRepository.existsByClinicIdAndEmail(
                                clinic.getId(),
                                "juan.castro@example.com")).thenReturn(false);

                when(patientRepository.existsByClinicIdAndEmail(
                                clinic.getId(),
                                "juan.castro@example.com")).thenReturn(false);

                when(doctorRepository.findByIdAndClinicIdAndRecordStatus(
                                doctor.getId(),
                                clinic.getId(),
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(doctor));

                when(agendaBlockRepository
                                .findForUpdateByIdAndDoctorIdAndClinicId(
                                                agendaBlock.getId(),
                                                doctor.getId(),
                                                clinic.getId(),
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(agendaBlock));

                when(roleRepository.findByCodeAndRecordStatus(
                                SecurityRoleCode.PATIENT.name(),
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(patientRole));

                when(appointmentStatusRepository
                                .findByCodeAndRecordStatus(
                                                AppointmentStatusCode.PROGRAMADA.name(),
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(programmedStatus));

                when(passwordEncoder.encode("Password123!"))
                                .thenReturn("encoded-password");

                when(userRepository.save(any(UserEntity.class)))
                                .thenAnswer(invocation -> {
                                        UserEntity user = invocation.getArgument(0);
                                        user.setId(2000L);
                                        return user;
                                });

                when(patientRepository.save(any(PatientEntity.class)))
                                .thenAnswer(invocation -> {
                                        PatientEntity patient = invocation.getArgument(0);
                                        patient.setId(3000L);
                                        return patient;
                                });

                when(appointmentRepository.save(any(AppointmentEntity.class)))
                                .thenAnswer(invocation -> {
                                        AppointmentEntity appointment = invocation.getArgument(0);

                                        appointment.setId(4000L);

                                        return appointment;
                                });

                when(appointmentRepository
                                .existsActiveAppointmentByAgendaBlockId(
                                                agendaBlock.getId(),
                                                List.of(
                                                                AppointmentStatusCode.PROGRAMADA.name(),
                                                                AppointmentStatusCode.CONFIRMADA.name()),
                                                RecordStatus.ACTIVE))
                                .thenReturn(false);

                FirstAppointmentResponse response = service.createFirstAppointment(
                                CLINIC_SLUG,
                                request);

                assertEquals(4000L, response.appointmentId());
                assertEquals("Juan Carlos", response.firstName());
                assertEquals("Castro Cruz", response.lastName());
                assertEquals(
                                "juan.castro@example.com",
                                response.email());
                assertEquals(100L, response.doctorId());
                assertEquals("María", response.doctorFirstName());
                assertEquals("López", response.doctorLastName());
                assertEquals(10L, response.specialtyId());
                assertEquals(
                                "Cardiología",
                                response.specialtyName());
                assertEquals(
                                agendaBlock.getAppointmentDate(),
                                response.appointmentDate());
                assertEquals(
                                LocalTime.of(9, 0),
                                response.startTime());
                assertEquals(
                                LocalTime.of(9, 30),
                                response.endTime());
                assertEquals(
                                AppointmentStatusCode.PROGRAMADA.name(),
                                response.statusCode());
                assertEquals(
                                "Programada",
                                response.statusName());

                assertFalse(agendaBlock.getAvailable());

                verify(passwordEncoder)
                                .encode("Password123!");

                verify(userRepository)
                                .existsByClinicIdAndEmail(
                                                clinic.getId(),
                                                "juan.castro@example.com");

                verify(patientRepository)
                                .existsByClinicIdAndEmail(
                                                clinic.getId(),
                                                "juan.castro@example.com");

                ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);

                verify(userRepository).save(
                                userCaptor.capture());

                UserEntity savedUser = userCaptor.getValue();

                assertEquals(clinic, savedUser.getClinic());
                assertEquals(patientRole, savedUser.getRole());
                assertEquals(
                                "agendoc-development-clinic:juan.castro@example.com",
                                savedUser.getUsername());
                assertEquals(
                                "juan.castro@example.com",
                                savedUser.getEmail());
                assertEquals(
                                "encoded-password",
                                savedUser.getPasswordHash());
                assertEquals(true, savedUser.isActive());

                ArgumentCaptor<PatientEntity> patientCaptor = ArgumentCaptor.forClass(PatientEntity.class);

                verify(patientRepository).save(
                                patientCaptor.capture());

                PatientEntity savedPatient = patientCaptor.getValue();

                assertEquals(clinic, savedPatient.getClinic());
                assertEquals(savedUser, savedPatient.getUser());
                assertEquals(
                                "Juan Carlos",
                                savedPatient.getFirstName());
                assertEquals(
                                "Castro Cruz",
                                savedPatient.getLastName());
                assertEquals(
                                "+52 55 1234 5678",
                                savedPatient.getPhone());
                assertEquals(
                                "juan.castro@example.com",
                                savedPatient.getEmail());
                assertNull(savedPatient.getDocumentType());
                assertNull(savedPatient.getDocumentNumber());
                assertNull(savedPatient.getBirthDate());
                assertNull(savedPatient.getAddress());

                ArgumentCaptor<AppointmentEntity> appointmentCaptor = ArgumentCaptor.forClass(AppointmentEntity.class);

                verify(appointmentRepository).save(
                                appointmentCaptor.capture());

                AppointmentEntity savedAppointment = appointmentCaptor.getValue();

                assertEquals(clinic, savedAppointment.getClinic());
                assertEquals(savedPatient, savedAppointment.getPatient());
                assertEquals(doctor, savedAppointment.getDoctor());
                assertEquals(
                                agendaBlock,
                                savedAppointment.getAgendaBlock());
                assertEquals(
                                programmedStatus,
                                savedAppointment.getStatus());
                assertEquals(
                                "Consulta inicial",
                                savedAppointment.getReason());
                assertNull(savedAppointment.getNotes());

                verify(appointmentRepository)
                                .existsActiveAppointmentByAgendaBlockId(
                                                agendaBlock.getId(),
                                                List.of(
                                                                AppointmentStatusCode.PROGRAMADA.name(),
                                                                AppointmentStatusCode.CONFIRMADA.name()),
                                                RecordStatus.ACTIVE);

        }

        @Test
        void shouldRejectFirstAppointmentWhenBlockHasActiveAppointment() {
                ClinicEntity clinic = createPublicClinic();
                clinic.setId(1L);

                MedicalSpecialtyEntity specialty = createSpecialty(
                                10L,
                                "Cardiología");

                DoctorEntity doctor = createDoctor(
                                100L,
                                "María",
                                "López",
                                specialty);
                doctor.setClinic(clinic);

                MedicalAgendaEntity agenda = createMedicalAgenda(
                                1000L,
                                clinic,
                                doctor);

                AgendaBlockEntity agendaBlock = createAgendaBlock(
                                5000L,
                                agenda,
                                LocalDate.now().plusDays(1),
                                LocalTime.of(9, 0),
                                LocalTime.of(9, 30));

                FirstAppointmentRequest request = new FirstAppointmentRequest(
                                "Juan Carlos",
                                "Castro Cruz",
                                "juan.castro@example.com",
                                "+52 55 1234 5678",
                                "Password123!",
                                "Password123!",
                                doctor.getId(),
                                agendaBlock.getId(),
                                "Consulta inicial");

                when(clinicRepository.findBySlugAndRecordStatus(
                                CLINIC_SLUG,
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(clinic));

                when(userRepository.existsByClinicIdAndEmail(
                                clinic.getId(),
                                "juan.castro@example.com")).thenReturn(false);

                when(patientRepository.existsByClinicIdAndEmail(
                                clinic.getId(),
                                "juan.castro@example.com")).thenReturn(false);

                when(doctorRepository.findByIdAndClinicIdAndRecordStatus(
                                doctor.getId(),
                                clinic.getId(),
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(doctor));

                when(agendaBlockRepository
                                .findForUpdateByIdAndDoctorIdAndClinicId(
                                                agendaBlock.getId(),
                                                doctor.getId(),
                                                clinic.getId(),
                                                RecordStatus.ACTIVE))
                                .thenReturn(Optional.of(agendaBlock));

                when(appointmentRepository
                                .existsActiveAppointmentByAgendaBlockId(
                                                agendaBlock.getId(),
                                                List.of(
                                                                AppointmentStatusCode.PROGRAMADA.name(),
                                                                AppointmentStatusCode.CONFIRMADA.name()),
                                                RecordStatus.ACTIVE))
                                .thenReturn(true);

                assertThrows(
                                ConflictException.class,
                                () -> service.createFirstAppointment(
                                                CLINIC_SLUG,
                                                request));

                assertTrue(agendaBlock.getAvailable());

                verify(userRepository, never())
                                .save(any(UserEntity.class));

                verify(patientRepository, never())
                                .save(any(PatientEntity.class));

                verify(appointmentRepository, never())
                                .save(any(AppointmentEntity.class));

                verify(roleRepository, never())
                                .findByCodeAndRecordStatus(
                                                SecurityRoleCode.PATIENT.name(),
                                                RecordStatus.ACTIVE);

                verify(appointmentStatusRepository, never())
                                .findByCodeAndRecordStatus(
                                                AppointmentStatusCode.PROGRAMADA.name(),
                                                RecordStatus.ACTIVE);
        }

        private DoctorEntity createDoctor(
                        Long id,
                        String firstName,
                        String lastName,
                        MedicalSpecialtyEntity specialty) {
                DoctorEntity doctor = new DoctorEntity();

                doctor.setId(id);
                doctor.setFirstName(firstName);
                doctor.setLastName(lastName);
                doctor.setSpecialty(specialty);

                return doctor;
        }

        private MedicalSpecialtyEntity createSpecialty(
                        Long id,
                        String name) {
                MedicalSpecialtyEntity specialty = new MedicalSpecialtyEntity();

                specialty.setId(id);
                specialty.setName(name);

                return specialty;
        }

        private ClinicEntity createPublicClinic() {
                ClinicEntity clinic = new ClinicEntity();

                clinic.setSlug(CLINIC_SLUG);
                clinic.setName("AgenDoc Development Clinic");
                clinic.setPublicName("AgenDoc Public Clinic");
                clinic.setPublicDescription(
                                "Medical care for development.");
                clinic.setLogoUrl(
                                "https://cdn.agendoc.local/logo.png");
                clinic.setPhone("+52 55 0000 0000");
                clinic.setWhatsapp("+52 55 1111 1111");
                clinic.setEmail("contacto@agendoc.local");
                clinic.setAddress("Ciudad de México");
                clinic.setMapUrl(
                                "https://maps.example.com/agendoc");
                clinic.setPublicPortalEnabled(true);

                return clinic;
        }

        private MedicalAgendaEntity createMedicalAgenda(
                        Long id,
                        ClinicEntity clinic,
                        DoctorEntity doctor) {
                MedicalAgendaEntity agenda = new MedicalAgendaEntity();

                agenda.setId(id);
                agenda.setClinic(clinic);
                agenda.setDoctor(doctor);
                agenda.setActive(true);

                return agenda;
        }

        private AgendaBlockEntity createAgendaBlock(
                        Long id,
                        MedicalAgendaEntity agenda,
                        LocalDate date,
                        LocalTime start,
                        LocalTime end) {
                AgendaBlockEntity block = new AgendaBlockEntity();

                block.setId(id);
                block.setMedicalAgenda(agenda);
                block.setAppointmentDate(date);
                block.setStartTime(start);
                block.setEndTime(end);
                block.setAvailable(true);

                return block;
        }

        private RoleEntity createRole(
                        Long id,
                        String code,
                        String name) {
                RoleEntity role = new RoleEntity();

                role.setId(id);
                role.setCode(code);
                role.setName(name);

                return role;
        }

        private AppointmentStatusEntity createAppointmentStatus(
                        Long id,
                        String code,
                        String name) {
                AppointmentStatusEntity status = new AppointmentStatusEntity();

                status.setId(id);
                status.setCode(code);
                status.setName(name);

                return status;
        }

}