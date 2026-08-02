package com.agendoc.security.context;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.modules.doctor.repository.DoctorRepository;
import com.agendoc.modules.patient.repository.PatientRepository;
import com.agendoc.modules.user.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Resolves the authenticated user's domain context from Spring Security.
 */
@Component
@RequiredArgsConstructor
public class SecurityAuthenticatedUserContextProvider
        implements AuthenticatedUserContextProvider {

    private static final String PATIENT_ROLE = "PATIENT";
    private static final String DOCTOR_ROLE = "DOCTOR";
    private static final String RECEPTIONIST_ROLE = "RECEPTIONIST";

    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public AuthenticatedUserContext getCurrentContext() {

        UserEntity user = getAuthenticatedUser();

        Long clinicId = user.getClinic().getId();
        String roleCode = user.getRole().getCode();

        Long patientId = null;
        Long doctorId = null;

        switch (roleCode) {
            case PATIENT_ROLE ->
                    patientId = resolvePatientId(
                            user.getId(),
                            clinicId
                    );

            case DOCTOR_ROLE ->
                    doctorId = resolveDoctorId(
                            user.getId(),
                            clinicId
                    );

            case RECEPTIONIST_ROLE -> {
                // Receptionists currently use the authenticated user
                // and clinic context without a separate business profile.
            }

            default -> throw new IllegalStateException(
                    "Authenticated user has an unsupported role"
            );
        }

        return new AuthenticatedUserContext(
                user.getId(),
                user.getUsername(),
                clinicId,
                roleCode,
                patientId,
                doctorId
        );
    }

    private UserEntity getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication instanceof AnonymousAuthenticationToken) {

            throw new IllegalStateException(
                    "No authenticated user is available"
            );
        }

        if (!(authentication.getPrincipal() instanceof UserEntity user)) {
            throw new IllegalStateException(
                    "Authenticated principal is not a valid AgenDoc user"
            );
        }

        if (user.getClinic() == null) {
            throw new IllegalStateException(
                    "Authenticated user does not have an associated clinic"
            );
        }

        if (user.getRole() == null) {
            throw new IllegalStateException(
                    "Authenticated user does not have an associated role"
            );
        }

        if (user.getId() == null) {
            throw new IllegalStateException(
                    "Authenticated user does not have a valid identifier"
            );
        }

        if (user.getClinic().getId() == null) {
            throw new IllegalStateException(
                    "Authenticated clinic does not have a valid identifier"
            );
        }

        if (user.getRole().getCode() == null
                || user.getRole().getCode().isBlank()) {

            throw new IllegalStateException(
                    "Authenticated user does not have a valid role code"
            );
        }

        return user;
    }

    private Long resolvePatientId(
            Long userId,
            Long clinicId
    ) {

        return patientRepository
                .findByUserIdAndClinicIdAndRecordStatus(
                        userId,
                        clinicId,
                        RecordStatus.ACTIVE
                )
                .orElseThrow(
                        () -> new IllegalStateException(
                                "Authenticated patient profile was not found"
                        )
                )
                .getId();
    }

    private Long resolveDoctorId(
            Long userId,
            Long clinicId
    ) {

        return doctorRepository
                .findByUserIdAndClinicIdAndRecordStatus(
                        userId,
                        clinicId,
                        RecordStatus.ACTIVE
                )
                .orElseThrow(
                        () -> new IllegalStateException(
                                "Authenticated doctor profile was not found"
                        )
                )
                .getId();
    }
}