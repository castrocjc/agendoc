package com.agendoc.modules.auth.dto;

/**
 * Minimal authenticated user information returned to clients.
 */
public record AuthenticatedUser(
        Long id,
        String username,
        String email,
        String role,
        Long clinicId
) {
}