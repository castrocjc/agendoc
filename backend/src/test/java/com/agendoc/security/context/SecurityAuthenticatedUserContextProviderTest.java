package com.agendoc.security.context;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.modules.clinic.entity.ClinicEntity;
import com.agendoc.modules.doctor.entity.DoctorEntity;
import com.agendoc.modules.doctor.repository.DoctorRepository;
import com.agendoc.modules.patient.entity.PatientEntity;
import com.agendoc.modules.patient.repository.PatientRepository;
import com.agendoc.modules.role.entity.RoleEntity;
import com.agendoc.modules.user.entity.UserEntity;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
class SecurityAuthenticatedUserContextProviderTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private DoctorRepository doctorRepository;

    private SecurityAuthenticatedUserContextProvider contextProvider;

    @BeforeEach
    void setUp() {
        contextProvider =
                new SecurityAuthenticatedUserContextProvider(
                        patientRepository,
                        doctorRepository
                );

        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void shouldReturnReceptionistContextWithoutBusinessProfile() {

        UserEntity user = createUser(
                10L,
                1L,
                "RECEPTIONIST"
        );

        authenticate(user);

        AuthenticatedUserContext context =
                contextProvider.getCurrentContext();

        assertThat(context.userId()).isEqualTo(10L);
        assertThat(context.username())
                .isEqualTo("authenticated.user");
        assertThat(context.clinicId()).isEqualTo(1L);
        assertThat(context.roleCode())
                .isEqualTo("RECEPTIONIST");
        assertThat(context.patientId()).isNull();
        assertThat(context.doctorId()).isNull();

        verify(patientRepository, never())
                .findByUserIdAndClinicIdAndRecordStatus(
                        10L,
                        1L,
                        RecordStatus.ACTIVE
                );

        verify(doctorRepository, never())
                .findByUserIdAndClinicIdAndRecordStatus(
                        10L,
                        1L,
                        RecordStatus.ACTIVE
                );
    }

    @Test
    void shouldReturnPatientContextWhenActiveProfileExists() {

        UserEntity user = createUser(
                20L,
                1L,
                "PATIENT"
        );

        PatientEntity patient = new PatientEntity();
        patient.setId(200L);
        patient.setRecordStatus(RecordStatus.ACTIVE);

        when(patientRepository
                .findByUserIdAndClinicIdAndRecordStatus(
                        20L,
                        1L,
                        RecordStatus.ACTIVE
                ))
                .thenReturn(Optional.of(patient));

        authenticate(user);

        AuthenticatedUserContext context =
                contextProvider.getCurrentContext();

        assertThat(context.userId()).isEqualTo(20L);
        assertThat(context.clinicId()).isEqualTo(1L);
        assertThat(context.roleCode()).isEqualTo("PATIENT");
        assertThat(context.patientId()).isEqualTo(200L);
        assertThat(context.doctorId()).isNull();

        verify(patientRepository)
                .findByUserIdAndClinicIdAndRecordStatus(
                        20L,
                        1L,
                        RecordStatus.ACTIVE
                );

        verify(doctorRepository, never())
                .findByUserIdAndClinicIdAndRecordStatus(
                        20L,
                        1L,
                        RecordStatus.ACTIVE
                );
    }

    @Test
    void shouldReturnDoctorContextWhenActiveProfileExists() {

        UserEntity user = createUser(
                30L,
                1L,
                "DOCTOR"
        );

        DoctorEntity doctor = new DoctorEntity();
        doctor.setId(300L);
        doctor.setRecordStatus(RecordStatus.ACTIVE);

        when(doctorRepository
                .findByUserIdAndClinicIdAndRecordStatus(
                        30L,
                        1L,
                        RecordStatus.ACTIVE
                ))
                .thenReturn(Optional.of(doctor));

        authenticate(user);

        AuthenticatedUserContext context =
                contextProvider.getCurrentContext();

        assertThat(context.userId()).isEqualTo(30L);
        assertThat(context.clinicId()).isEqualTo(1L);
        assertThat(context.roleCode()).isEqualTo("DOCTOR");
        assertThat(context.patientId()).isNull();
        assertThat(context.doctorId()).isEqualTo(300L);

        verify(doctorRepository)
                .findByUserIdAndClinicIdAndRecordStatus(
                        30L,
                        1L,
                        RecordStatus.ACTIVE
                );

        verify(patientRepository, never())
                .findByUserIdAndClinicIdAndRecordStatus(
                        30L,
                        1L,
                        RecordStatus.ACTIVE
                );
    }

    @Test
    void shouldRejectRequestWhenAuthenticationDoesNotExist() {

        assertThatThrownBy(
                () -> contextProvider.getCurrentContext()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "No authenticated user is available"
                );
    }

    @Test
    void shouldRejectAnonymousAuthentication() {

        AnonymousAuthenticationToken authentication =
                new AnonymousAuthenticationToken(
                        "anonymous-key",
                        "anonymousUser",
                        List.of(
                                new SimpleGrantedAuthority(
                                        "ROLE_ANONYMOUS"
                                )
                        )
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        assertThatThrownBy(
                () -> contextProvider.getCurrentContext()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "No authenticated user is available"
                );
    }

    @Test
    void shouldRejectInvalidAuthenticatedPrincipal() {

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        "invalid-principal",
                        null,
                        List.of(
                                new SimpleGrantedAuthority(
                                        "ROLE_RECEPTIONIST"
                                )
                        )
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);

        assertThatThrownBy(
                () -> contextProvider.getCurrentContext()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "Authenticated principal is not a valid AgenDoc user"
                );
    }

    @Test
    void shouldRejectAuthenticatedUserWithoutClinic() {

        UserEntity user = createUser(
                10L,
                1L,
                "RECEPTIONIST"
        );

        user.setClinic(null);

        authenticate(user);

        assertThatThrownBy(
                () -> contextProvider.getCurrentContext()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "Authenticated user does not have an associated clinic"
                );
    }

    @Test
    void shouldRejectAuthenticatedUserWithoutRole() {

        UserEntity user = createUser(
                10L,
                1L,
                "RECEPTIONIST"
        );

        user.setRole(null);

        authenticate(user);

        assertThatThrownBy(
                () -> contextProvider.getCurrentContext()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "Authenticated user does not have an associated role"
                );
    }

    @Test
    void shouldRejectAuthenticatedUserWithoutIdentifier() {

        UserEntity user = createUser(
                null,
                1L,
                "RECEPTIONIST"
        );

        authenticate(user);

        assertThatThrownBy(
                () -> contextProvider.getCurrentContext()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "Authenticated user does not have a valid identifier"
                );
    }

    @Test
    void shouldRejectAuthenticatedUserWithoutClinicIdentifier() {

        UserEntity user = createUser(
                10L,
                null,
                "RECEPTIONIST"
        );

        authenticate(user);

        assertThatThrownBy(
                () -> contextProvider.getCurrentContext()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "Authenticated clinic does not have a valid identifier"
                );
    }

    @Test
    void shouldRejectAuthenticatedUserWithoutValidRoleCode() {

        UserEntity user = createUser(
                10L,
                1L,
                " "
        );

        authenticate(user);

        assertThatThrownBy(
                () -> contextProvider.getCurrentContext()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "Authenticated user does not have a valid role code"
                );
    }

    @Test
    void shouldRejectUnsupportedRole() {

        UserEntity user = createUser(
                10L,
                1L,
                "ADMIN"
        );

        authenticate(user);

        assertThatThrownBy(
                () -> contextProvider.getCurrentContext()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "Authenticated user has an unsupported role"
                );

        verify(patientRepository, never())
                .findByUserIdAndClinicIdAndRecordStatus(
                        10L,
                        1L,
                        RecordStatus.ACTIVE
                );

        verify(doctorRepository, never())
                .findByUserIdAndClinicIdAndRecordStatus(
                        10L,
                        1L,
                        RecordStatus.ACTIVE
                );
    }

    @Test
    void shouldRejectPatientWithoutActiveBusinessProfile() {

        UserEntity user = createUser(
                20L,
                1L,
                "PATIENT"
        );

        when(patientRepository
                .findByUserIdAndClinicIdAndRecordStatus(
                        20L,
                        1L,
                        RecordStatus.ACTIVE
                ))
                .thenReturn(Optional.empty());

        authenticate(user);

        assertThatThrownBy(
                () -> contextProvider.getCurrentContext()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "Authenticated patient profile was not found"
                );
    }

    @Test
    void shouldRejectDoctorWithoutActiveBusinessProfile() {

        UserEntity user = createUser(
                30L,
                1L,
                "DOCTOR"
        );

        when(doctorRepository
                .findByUserIdAndClinicIdAndRecordStatus(
                        30L,
                        1L,
                        RecordStatus.ACTIVE
                ))
                .thenReturn(Optional.empty());

        authenticate(user);

        assertThatThrownBy(
                () -> contextProvider.getCurrentContext()
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(
                        "Authenticated doctor profile was not found"
                );
    }

    private UserEntity createUser(
            Long userId,
            Long clinicId,
            String roleCode
    ) {

        ClinicEntity clinic = new ClinicEntity();
        clinic.setId(clinicId);
        clinic.setName("AgenDoc Clinic");
        clinic.setRecordStatus(RecordStatus.ACTIVE);

        RoleEntity role = new RoleEntity();
        role.setId(1L);
        role.setCode(roleCode);
        role.setName(roleCode);
        role.setRecordStatus(RecordStatus.ACTIVE);

        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setClinic(clinic);
        user.setRole(role);
        user.setUsername("authenticated.user");
        user.setEmail("authenticated.user@agendoc.local");
        user.setPasswordHash("not-exposed");
        user.setActive(true);
        user.setRecordStatus(RecordStatus.ACTIVE);

        return user;
    }

    private void authenticate(UserEntity user) {

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        List.of(
                                new SimpleGrantedAuthority(
                                        "ROLE_TEST"
                                )
                        )
                );

        SecurityContextHolder
                .getContext()
                .setAuthentication(authentication);
    }
}