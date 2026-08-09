package com.agendoc.security.jwt;

import com.agendoc.modules.auth.dto.AuthenticatedUser;

/**
 * Defines JWT generation and validation operations.
 */
public interface JwtTokenService {

    String generateToken(AuthenticatedUser authenticatedUser);

    Long extractUserId(String token);

    boolean isTokenValid(String token);

    long getExpirationSeconds();
}