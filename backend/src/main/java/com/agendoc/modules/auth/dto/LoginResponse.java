package com.agendoc.modules.auth.dto;

/**
 * Successful authentication response.
 */
public record LoginResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        AuthenticatedUser user
) {
}