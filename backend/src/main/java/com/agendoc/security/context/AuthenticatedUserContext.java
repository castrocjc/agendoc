package com.agendoc.security.context;

/**
 * Immutable representation of the authenticated user's domain context.
 *
 * @param userId authenticated user identifier
 * @param username authenticated username
 * @param clinicId clinic associated with the authenticated user
 * @param roleCode technical role code
 * @param patientId patient profile identifier when the user is a patient
 * @param doctorId doctor profile identifier when the user is a doctor
 */
public record AuthenticatedUserContext(
        Long userId,
        String username,
        Long clinicId,
        String roleCode,
        Long patientId,
        Long doctorId
) {
}