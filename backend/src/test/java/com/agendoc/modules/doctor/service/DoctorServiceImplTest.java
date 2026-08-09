package com.agendoc.modules.doctor.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.common.exception.ConflictException;
import com.agendoc.common.exception.ResourceNotFoundException;
import com.agendoc.modules.clinic.entity.ClinicEntity;
import com.agendoc.modules.clinic.repository.ClinicRepository;
import com.agendoc.modules.doctor.dto.CreateDoctorRequest;
import com.agendoc.modules.doctor.dto.DoctorResponse;
import com.agendoc.modules.doctor.entity.DoctorEntity;
import com.agendoc.modules.doctor.entity.MedicalSpecialtyEntity;
import com.agendoc.modules.doctor.repository.DoctorRepository;
import com.agendoc.modules.doctor.repository.MedicalSpecialtyRepository;
import com.agendoc.security.authorization.AuthenticatedUserAuthorization;
import com.agendoc.security.authorization.SecurityRoleCode;
import com.agendoc.security.context.AuthenticatedUserContext;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

        @Mock
        private DoctorRepository doctorRepository;

        @Mock
        private MedicalSpecialtyRepository medicalSpecialtyRepository;

        @Mock
        private ClinicRepository clinicRepository;

        @Mock
        private AuthenticatedUserAuthorization authenticatedUserAuthorization;

        private AuthenticatedUserContext receptionistContext;

        private DoctorServiceImpl doctorService;

        @BeforeEach
        void setUp() {

                doctorService = new DoctorServiceImpl(
                                doctorRepository,
                                medicalSpecialtyRepository,
                                clinicRepository,
                                authenticatedUserAuthorization);

                receptionistContext = new AuthenticatedUserContext(
                                100L,
                                "receptionist.user",
                                1L,
                                SecurityRoleCode.RECEPTIONIST.name(),
                                null,
                                null);

                when(authenticatedUserAuthorization.requireRole(
                                SecurityRoleCode.RECEPTIONIST)).thenReturn(receptionistContext);
        }

        @Test
        void shouldCreateDoctorWhenRequestIsValid() {

                CreateDoctorRequest request = validRequest();

                ClinicEntity clinic = createClinic();
                MedicalSpecialtyEntity specialty = createSpecialty();

                when(doctorRepository.existsByDocumentNumberIgnoreCase(
                                "87654321")).thenReturn(false);

                when(doctorRepository.existsByMedicalLicenseNumberIgnoreCase(
                                "CMP98765")).thenReturn(false);

                when(doctorRepository.existsByEmailIgnoreCase(
                                "ana.torres@agendoc.com")).thenReturn(false);

                when(clinicRepository.findByIdAndRecordStatus(
                        1L,
                        RecordStatus.ACTIVE
                )).thenReturn(Optional.of(clinic));

                when(medicalSpecialtyRepository.findByIdAndRecordStatus(
                                1L,
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(specialty));

                when(doctorRepository.save(any(DoctorEntity.class)))
                                .thenAnswer(invocation -> {
                                        DoctorEntity doctor = invocation.getArgument(0);
                                        doctor.setId(10L);
                                        return doctor;
                                });

                DoctorResponse response = doctorService.createDoctor(request);

                verify(authenticatedUserAuthorization)
                        .requireRole(SecurityRoleCode.RECEPTIONIST);

                verify(clinicRepository)
                        .findByIdAndRecordStatus(
                                receptionistContext.clinicId(),
                                RecordStatus.ACTIVE
                        );

                assertThat(response.id()).isEqualTo(10L);
                assertThat(response.clinicId()).isEqualTo(1L);
                assertThat(response.specialtyId()).isEqualTo(1L);
                assertThat(response.specialtyName())
                                .isEqualTo("Medicina General");
                assertThat(response.firstName()).isEqualTo("Ana");
                assertThat(response.lastName()).isEqualTo("Torres");
                assertThat(response.documentType()).isEqualTo("DNI");
                assertThat(response.documentNumber()).isEqualTo("87654321");
                assertThat(response.medicalLicenseNumber())
                                .isEqualTo("CMP98765");
                assertThat(response.phone()).isEqualTo("999888777");
                assertThat(response.email())
                                .isEqualTo("ana.torres@agendoc.com");
                assertThat(response.recordStatus()).isEqualTo("ACTIVE");

                ArgumentCaptor<DoctorEntity> doctorCaptor = ArgumentCaptor.forClass(DoctorEntity.class);

                verify(doctorRepository).save(doctorCaptor.capture());

                DoctorEntity savedDoctor = doctorCaptor.getValue();

                assertThat(savedDoctor.getClinic()).isSameAs(clinic);
                assertThat(savedDoctor.getSpecialty()).isSameAs(specialty);
                assertThat(savedDoctor.getUser()).isNull();
                assertThat(savedDoctor.getFirstName()).isEqualTo("Ana");
                assertThat(savedDoctor.getLastName()).isEqualTo("Torres");
                assertThat(savedDoctor.getDocumentType()).isEqualTo("DNI");
                assertThat(savedDoctor.getDocumentNumber())
                                .isEqualTo("87654321");
                assertThat(savedDoctor.getMedicalLicenseNumber())
                                .isEqualTo("CMP98765");
                assertThat(savedDoctor.getPhone()).isEqualTo("999888777");
                assertThat(savedDoctor.getEmail())
                                .isEqualTo("ana.torres@agendoc.com");
        }

        @Test
        void shouldNormalizeDoctorDataBeforeSaving() {

                CreateDoctorRequest request = new CreateDoctorRequest(
                                "  Ana María  ",
                                "  Torres López  ",
                                "  dni  ",
                                "  abc123  ",
                                "  cmp98765  ",
                                1L,
                                "  999888777  ",
                                "  ANA.TORRES@AGENDOC.COM  ");

                ClinicEntity clinic = createClinic();
                MedicalSpecialtyEntity specialty = createSpecialty();

                when(doctorRepository.existsByDocumentNumberIgnoreCase(
                                "ABC123")).thenReturn(false);

                when(doctorRepository.existsByMedicalLicenseNumberIgnoreCase(
                                "CMP98765")).thenReturn(false);

                when(doctorRepository.existsByEmailIgnoreCase(
                                "ana.torres@agendoc.com")).thenReturn(false);

                when(clinicRepository.findByIdAndRecordStatus(
                        1L,
                        RecordStatus.ACTIVE
                )).thenReturn(Optional.of(clinic));

                when(medicalSpecialtyRepository.findByIdAndRecordStatus(
                                1L,
                                RecordStatus.ACTIVE)).thenReturn(Optional.of(specialty));

                when(doctorRepository.save(any(DoctorEntity.class)))
                                .thenAnswer(invocation -> {
                                        DoctorEntity doctor = invocation.getArgument(0);
                                        doctor.setId(10L);
                                        return doctor;
                                });

                doctorService.createDoctor(request);

                ArgumentCaptor<DoctorEntity> doctorCaptor = ArgumentCaptor.forClass(DoctorEntity.class);

                verify(doctorRepository).save(doctorCaptor.capture());

                DoctorEntity savedDoctor = doctorCaptor.getValue();

                assertThat(savedDoctor.getFirstName())
                                .isEqualTo("Ana María");
                assertThat(savedDoctor.getLastName())
                                .isEqualTo("Torres López");
                assertThat(savedDoctor.getDocumentType()).isEqualTo("DNI");
                assertThat(savedDoctor.getDocumentNumber()).isEqualTo("ABC123");
                assertThat(savedDoctor.getMedicalLicenseNumber())
                                .isEqualTo("CMP98765");
                assertThat(savedDoctor.getPhone()).isEqualTo("999888777");
                assertThat(savedDoctor.getEmail())
                                .isEqualTo("ana.torres@agendoc.com");
        }

        @Test
        void shouldRejectDuplicatedDocumentNumber() {

                CreateDoctorRequest request = validRequest();

                when(clinicRepository.findByIdAndRecordStatus(
                        1L,
                        RecordStatus.ACTIVE
                )).thenReturn(Optional.of(createClinic()));

                when(doctorRepository.existsByDocumentNumberIgnoreCase(
                                "87654321")).thenReturn(true);

                assertThatThrownBy(() -> doctorService.createDoctor(request))
                                .isInstanceOf(ConflictException.class)
                                .hasMessage(
                                                "Ya existe un médico registrado con ese número de documento.");

                verify(doctorRepository, never())
                                .save(any(DoctorEntity.class));
        }

        @Test
        void shouldRejectDuplicatedMedicalLicenseNumber() {

                CreateDoctorRequest request = validRequest();

                when(clinicRepository.findByIdAndRecordStatus(
                        1L,
                        RecordStatus.ACTIVE
                )).thenReturn(Optional.of(createClinic()));

                when(doctorRepository.existsByDocumentNumberIgnoreCase(
                                "87654321")).thenReturn(false);

                when(doctorRepository.existsByMedicalLicenseNumberIgnoreCase(
                                "CMP98765")).thenReturn(true);

                assertThatThrownBy(() -> doctorService.createDoctor(request))
                                .isInstanceOf(ConflictException.class)
                                .hasMessage(
                                                "Ya existe un médico registrado con ese número de colegiatura.");

                verify(doctorRepository, never())
                                .save(any(DoctorEntity.class));
        }

        @Test
        void shouldRejectDuplicatedEmail() {

                CreateDoctorRequest request = validRequest();

                when(clinicRepository.findByIdAndRecordStatus(
                        1L,
                        RecordStatus.ACTIVE
                )).thenReturn(Optional.of(createClinic()));

                when(doctorRepository.existsByDocumentNumberIgnoreCase(
                                "87654321")).thenReturn(false);

                when(doctorRepository.existsByMedicalLicenseNumberIgnoreCase(
                                "CMP98765")).thenReturn(false);

                when(doctorRepository.existsByEmailIgnoreCase(
                                "ana.torres@agendoc.com")).thenReturn(true);

                assertThatThrownBy(() -> doctorService.createDoctor(request))
                                .isInstanceOf(ConflictException.class)
                                .hasMessage(
                                                "Ya existe un médico registrado con ese correo.");

                verify(doctorRepository, never())
                                .save(any(DoctorEntity.class));
        }

        @Test
        void shouldRejectUnavailableSpecialty() {

                CreateDoctorRequest request = validRequest();

                when(clinicRepository.findByIdAndRecordStatus(
                        1L,
                        RecordStatus.ACTIVE
                )).thenReturn(Optional.of(createClinic()));

                when(doctorRepository.existsByDocumentNumberIgnoreCase(
                                "87654321")).thenReturn(false);

                when(doctorRepository.existsByMedicalLicenseNumberIgnoreCase(
                                "CMP98765")).thenReturn(false);

                when(doctorRepository.existsByEmailIgnoreCase(
                                "ana.torres@agendoc.com")).thenReturn(false);

                when(medicalSpecialtyRepository.findByIdAndRecordStatus(
                                1L,
                                RecordStatus.ACTIVE)).thenReturn(Optional.empty());

                assertThatThrownBy(() -> doctorService.createDoctor(request))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessage(
                                                "La especialidad seleccionada no está disponible.");

                verify(doctorRepository, never())
                                .save(any(DoctorEntity.class));
        }

        @Test
        void shouldRejectRegistrationWhenActiveClinicDoesNotExist() {

                CreateDoctorRequest request = validRequest();

                when(clinicRepository.findByIdAndRecordStatus(
                                1L,
                                RecordStatus.ACTIVE
                )).thenReturn(Optional.empty());

                assertThatThrownBy(() -> doctorService.createDoctor(request))
                                .isInstanceOf(ResourceNotFoundException.class)
                                .hasMessage(
                                                "No existe un consultorio activo disponible."
                                );

                verify(doctorRepository, never())
                                .existsByDocumentNumberIgnoreCase(any());

                verify(medicalSpecialtyRepository, never())
                                .findByIdAndRecordStatus(
                                                any(),
                                                any()
                                );

                verify(doctorRepository, never())
                                .save(any(DoctorEntity.class));
        }

        private CreateDoctorRequest validRequest() {
                return new CreateDoctorRequest(
                                "Ana",
                                "Torres",
                                "DNI",
                                "87654321",
                                "CMP98765",
                                1L,
                                "999888777",
                                "ana.torres@agendoc.com");
        }

        private ClinicEntity createClinic() {
                ClinicEntity clinic = new ClinicEntity();
                clinic.setId(1L);
                clinic.setName("AgenDoc Clinic");
                clinic.setRecordStatus(RecordStatus.ACTIVE);

                return clinic;
        }

        private MedicalSpecialtyEntity createSpecialty() {
                MedicalSpecialtyEntity specialty = new MedicalSpecialtyEntity();

                specialty.setId(1L);
                specialty.setCode("GENERAL_MEDICINE");
                specialty.setName("Medicina General");
                specialty.setDescription(
                                "Atención médica integral y de primera consulta.");
                specialty.setRecordStatus(RecordStatus.ACTIVE);

                return specialty;
        }
}