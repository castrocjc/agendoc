package com.agendoc.security.jwt;

import com.agendoc.modules.auth.dto.AuthenticatedUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

/**
 * Default implementation for issuing and validating AgenDoc JWT access tokens.
 */
@Service
public class JwtTokenServiceImpl implements JwtTokenService {

    private static final String USERNAME_CLAIM = "username";
    private static final String ROLE_CLAIM = "role";
    private static final String CLINIC_ID_CLAIM = "clinicId";

    private final JwtProperties jwtProperties;
    private final SecretKey signingKey;

    public JwtTokenServiceImpl(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.signingKey = Keys.hmacShaKeyFor(
                jwtProperties.secret().getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public String generateToken(AuthenticatedUser authenticatedUser) {
        Instant issuedAt = Instant.now();
        Instant expiresAt = issuedAt.plusSeconds(
                jwtProperties.expirationSeconds()
        );

        return Jwts.builder()
                .issuer(jwtProperties.issuer())
                .subject(authenticatedUser.id().toString())
                .issuedAt(Date.from(issuedAt))
                .expiration(Date.from(expiresAt))
                .claim(USERNAME_CLAIM, authenticatedUser.username())
                .claim(ROLE_CLAIM, authenticatedUser.role())
                .claim(CLINIC_ID_CLAIM, authenticatedUser.clinicId())
                .signWith(signingKey)
                .compact();
    }

    @Override
    public Long extractUserId(String token) {
        Claims claims = parseClaims(token);

        try {
            return Long.valueOf(claims.getSubject());
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "JWT subject does not contain a valid user identifier",
                    exception
            );
        }
    }

    @Override
    public boolean isTokenValid(String token) {
        try {
            Claims claims = parseClaims(token);
            Long.valueOf(claims.getSubject());
            return true;
        } catch (JwtException | IllegalArgumentException exception) {
            return false;
        }
    }

    @Override
    public long getExpirationSeconds() {
        return jwtProperties.expirationSeconds();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .requireIssuer(jwtProperties.issuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}