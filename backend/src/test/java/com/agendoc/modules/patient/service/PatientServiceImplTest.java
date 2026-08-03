package com.agendoc.modules.patient.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.common.exception.BadRequestException;
import com.agendoc.common.exception.ConflictException;
import com.agendoc.common.exception.ResourceNotFoundException;
import com.agendoc.modules.clinic.entity.ClinicEntity;
import com.agendoc.modules.clinic.repository.ClinicRepository;
import com.agendoc.modules.patient.dto.CreatePatientRequest;
import com.agendoc.modules.patient.dto.PatientResponse;
import com.agendoc.modules.patient.entity.PatientEntity;
import com.agendoc.modules.patient.repository.PatientRepository;
import com.agendoc.security.authorization.AuthenticatedUserAuthorization;
import com.agendoc.security.authorization.SecurityRoleCode;
import com.agendoc.security.context.AuthenticatedUserContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private ClinicRepository clinicRepository;

    @Mock
    private AuthenticatedUserAuthorization authenticatedUserAuthorization;

    private PatientServiceImpl patientService;

    private AuthenticatedUserContext receptionistContext;

    @BeforeEach
    void setUp() {

        patientService = new PatientServiceImpl(
                patientRepository,
                clinicRepository,
                authenticatedUserAuthorization
        );

        receptionistContext = new AuthenticatedUserContext(
                100L,
                "receptionist.user",
                1L,
                SecurityRoleCode.RECEPTIONIST.name(),
                null,
                null
        );
    }

    @Test
    void shouldCreatePatientWhenRequestIsValid() {

        CreatePatientRequest request = validRequest();
        ClinicEntity clinic = createClinic();

        when(authenticatedUserAuthorization.requireRole(
                SecurityRoleCode.RECEPTIONIST
        )).thenReturn(receptionistContext);

        when(clinicRepository.findByIdAndRecordStatus(
                receptionistContext.clinicId(),
                RecordStatus.ACTIVE
        )).thenReturn(Optional.of(clinic));

        when(patientRepository.existsByDocumentNumberIgnoreCase(
                "87654321"
        )).thenReturn(false);

        when(patientRepository.existsByEmailIgnoreCase(
                "maria.gonzalez@agendoc.com"
        )).thenReturn(false);

        when(patientRepository.save(any(PatientEntity.class)))
                .thenAnswer(invocation -> {
                    PatientEntity patient = invocation.getArgument(0);
                    patient.setId(10L);
                    return patient;
                });

        PatientResponse response =
                patientService.createPatient(request);

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.clinicId()).isEqualTo(1L);
        assertThat(response.firstName()).isEqualTo("María");
        assertThat(response.lastName()).isEqualTo("González");
        assertThat(response.documentType()).isEqualTo("DNI");
        assertThat(response.documentNumber()).isEqualTo("87654321");
        assertThat(response.birthDate())
                .isEqualTo(LocalDate.of(1990, 5, 15));
        assertThat(response.phone()).isEqualTo("999888777");
        assertThat(response.email())
                .isEqualTo("maria.gonzalez@agendoc.com");
        assertThat(response.address())
                .isEqualTo("Av. Principal 123");
        assertThat(response.recordStatus()).isEqualTo("ACTIVE");

        ArgumentCaptor<PatientEntity> patientCaptor =
                ArgumentCaptor.forClass(PatientEntity.class);

        verify(patientRepository).save(patientCaptor.capture());

        PatientEntity savedPatient = patientCaptor.getValue();

        assertThat(savedPatient.getClinic()).isSameAs(clinic);
        assertThat(savedPatient.getUser()).isNull();
        assertThat(savedPatient.getFirstName()).isEqualTo("María");
        assertThat(savedPatient.getLastName()).isEqualTo("González");
        assertThat(savedPatient.getDocumentType()).isEqualTo("DNI");
        assertThat(savedPatient.getDocumentNumber())
                .isEqualTo("87654321");
        assertThat(savedPatient.getEmail())
                .isEqualTo("maria.gonzalez@agendoc.com");

        verify(authenticatedUserAuthorization)
                .requireRole(SecurityRoleCode.RECEPTIONIST);

        verify(clinicRepository)
                .findByIdAndRecordStatus(
                        receptionistContext.clinicId(),
                        RecordStatus.ACTIVE
                );
    }

    @Test
    void shouldNormalizePatientDataBeforeSaving() {

        CreatePatientRequest request = new CreatePatientRequest(
                "  María  ",
                "  González López  ",
                "  dni  ",
                "  abc123  ",
                LocalDate.of(1990, 5, 15),
                "  999888777  ",
                "  MARIA.GONZALEZ@AGENDOC.COM  ",
                "  Av. Principal 123  "
        );

        ClinicEntity clinic = createClinic();

        when(authenticatedUserAuthorization.requireRole(
                SecurityRoleCode.RECEPTIONIST
        )).thenReturn(receptionistContext);

        when(clinicRepository.findByIdAndRecordStatus(
                receptionistContext.clinicId(),
                RecordStatus.ACTIVE
        )).thenReturn(Optional.of(clinic));

        when(patientRepository.existsByDocumentNumberIgnoreCase(
                "ABC123"
        )).thenReturn(false);

        when(patientRepository.existsByEmailIgnoreCase(
                "maria.gonzalez@agendoc.com"
        )).thenReturn(false);

        when(patientRepository.save(any(PatientEntity.class)))
                .thenAnswer(invocation -> {
                    PatientEntity patient = invocation.getArgument(0);
                    patient.setId(10L);
                    return patient;
                });

        patientService.createPatient(request);

        ArgumentCaptor<PatientEntity> patientCaptor =
                ArgumentCaptor.forClass(PatientEntity.class);

        verify(patientRepository).save(patientCaptor.capture());

        PatientEntity savedPatient = patientCaptor.getValue();

        assertThat(savedPatient.getFirstName()).isEqualTo("María");
        assertThat(savedPatient.getLastName())
                .isEqualTo("González López");
        assertThat(savedPatient.getDocumentType()).isEqualTo("DNI");
        assertThat(savedPatient.getDocumentNumber()).isEqualTo("ABC123");
        assertThat(savedPatient.getPhone()).isEqualTo("999888777");
        assertThat(savedPatient.getEmail())
                .isEqualTo("maria.gonzalez@agendoc.com");
        assertThat(savedPatient.getAddress())
                .isEqualTo("Av. Principal 123");
    }

    @Test
    void shouldRejectDuplicatedDocumentNumber() {

        when(authenticatedUserAuthorization.requireRole(
                SecurityRoleCode.RECEPTIONIST
        )).thenReturn(receptionistContext);

        when(clinicRepository.findByIdAndRecordStatus(
                receptionistContext.clinicId(),
                RecordStatus.ACTIVE
        )).thenReturn(Optional.of(createClinic()));

        when(patientRepository.existsByDocumentNumberIgnoreCase(
                "87654321"
        )).thenReturn(true);

        assertThatThrownBy(() ->
                patientService.createPatient(validRequest())
        )
                .isInstanceOf(ConflictException.class)
                .hasMessage(
                        "Ya existe un paciente registrado con ese número de documento."
                );

        verify(patientRepository, never())
                .save(any(PatientEntity.class));
    }

    @Test
    void shouldRejectDuplicatedEmail() {

        when(authenticatedUserAuthorization.requireRole(
                SecurityRoleCode.RECEPTIONIST
        )).thenReturn(receptionistContext);

        when(clinicRepository.findByIdAndRecordStatus(
                receptionistContext.clinicId(),
                RecordStatus.ACTIVE
        )).thenReturn(Optional.of(createClinic()));

        when(patientRepository.existsByDocumentNumberIgnoreCase(
                "87654321"
        )).thenReturn(false);

        when(patientRepository.existsByEmailIgnoreCase(
                "maria.gonzalez@agendoc.com"
        )).thenReturn(true);

        assertThatThrownBy(() ->
                patientService.createPatient(validRequest())
        )
                .isInstanceOf(ConflictException.class)
                .hasMessage(
                        "Ya existe un paciente registrado con ese correo."
                );

        verify(patientRepository, never())
                .save(any(PatientEntity.class));
    }

    @Test
    void shouldRejectCreationWhenAuthenticatedClinicIsUnavailable() {

        when(authenticatedUserAuthorization.requireRole(
                SecurityRoleCode.RECEPTIONIST
        )).thenReturn(receptionistContext);

        when(clinicRepository.findByIdAndRecordStatus(
                receptionistContext.clinicId(),
                RecordStatus.ACTIVE
        )).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                patientService.createPatient(validRequest())
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "No existe un consultorio activo disponible."
                );

        verify(patientRepository, never())
                .existsByDocumentNumberIgnoreCase(any());

        verify(patientRepository, never())
                .save(any(PatientEntity.class));
    }

    @Test
    void shouldSearchPatientsWithinAuthenticatedClinic() {

        PatientEntity patient = createPatient(createClinic());

        when(authenticatedUserAuthorization.requireRole(
                SecurityRoleCode.RECEPTIONIST
        )).thenReturn(receptionistContext);

        when(patientRepository.searchByClinicAndTerm(
                eq(receptionistContext.clinicId()),
                eq(RecordStatus.ACTIVE),
                eq("María"),
                any(Pageable.class)
        )).thenReturn(List.of(patient));

        List<PatientResponse> response =
                patientService.searchPatients("  María  ");

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().id()).isEqualTo(10L);
        assertThat(response.getFirst().firstName()).isEqualTo("María");

        verify(authenticatedUserAuthorization)
                .requireRole(SecurityRoleCode.RECEPTIONIST);

        verify(patientRepository)
                .searchByClinicAndTerm(
                        eq(receptionistContext.clinicId()),
                        eq(RecordStatus.ACTIVE),
                        eq("María"),
                        any(Pageable.class)
                );

        verify(clinicRepository, never())
                .findByIdAndRecordStatus(any(), any());
    }

    @Test
    void shouldReturnEmptyListWhenNoPatientsMatchSearch() {

        when(authenticatedUserAuthorization.requireRole(
                SecurityRoleCode.RECEPTIONIST
        )).thenReturn(receptionistContext);

        when(patientRepository.searchByClinicAndTerm(
                eq(receptionistContext.clinicId()),
                eq(RecordStatus.ACTIVE),
                eq("ZZ"),
                any(Pageable.class)
        )).thenReturn(List.of());

        List<PatientResponse> response =
                patientService.searchPatients("ZZ");

        assertThat(response).isEmpty();
    }

    @Test
    void shouldRejectSearchTermShorterThanTwoCharacters() {

        when(authenticatedUserAuthorization.requireRole(
                SecurityRoleCode.RECEPTIONIST
        )).thenReturn(receptionistContext);

        assertThatThrownBy(() ->
                patientService.searchPatients(" A ")
        )
                .isInstanceOf(BadRequestException.class)
                .hasMessage(
                        "Ingresa al menos 2 caracteres para buscar pacientes."
                );

        verify(patientRepository, never())
                .searchByClinicAndTerm(
                        any(),
                        any(),
                        any(),
                        any()
                );
    }

    @Test
    void shouldRejectNullSearchTerm() {

        when(authenticatedUserAuthorization.requireRole(
                SecurityRoleCode.RECEPTIONIST
        )).thenReturn(receptionistContext);

        assertThatThrownBy(() ->
                patientService.searchPatients(null)
        )
                .isInstanceOf(BadRequestException.class)
                .hasMessage(
                        "Ingresa al menos 2 caracteres para buscar pacientes."
                );

        verify(patientRepository, never())
                .searchByClinicAndTerm(
                        any(),
                        any(),
                        any(),
                        any()
                );
    }

    private CreatePatientRequest validRequest() {
        return new CreatePatientRequest(
                "María",
                "González",
                "DNI",
                "87654321",
                LocalDate.of(1990, 5, 15),
                "999888777",
                "maria.gonzalez@agendoc.com",
                "Av. Principal 123"
        );
    }

    private ClinicEntity createClinic() {
        ClinicEntity clinic = new ClinicEntity();
        clinic.setId(1L);
        clinic.setName("AgenDoc Clinic");
        clinic.setRecordStatus(RecordStatus.ACTIVE);

        return clinic;
    }

    private PatientEntity createPatient(
            ClinicEntity clinic
    ) {
        PatientEntity patient = new PatientEntity();

        patient.setId(10L);
        patient.setClinic(clinic);
        patient.setFirstName("María");
        patient.setLastName("González");
        patient.setDocumentType("DNI");
        patient.setDocumentNumber("87654321");
        patient.setBirthDate(LocalDate.of(1990, 5, 15));
        patient.setPhone("999888777");
        patient.setEmail("maria.gonzalez@agendoc.com");
        patient.setAddress("Av. Principal 123");
        patient.setRecordStatus(RecordStatus.ACTIVE);

        return patient;
    }
}