package com.agendoc.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.agendoc.modules.auth.dto.AuthenticatedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

class JwtTokenServiceImplTest {

        private static final String ISSUER = "agendoc-test";
        private static final String SECRET = "agendoc-test-secret-key-with-at-least-32-characters";
        private static final long EXPIRATION_SECONDS = 3600L;

        private JwtTokenService jwtTokenService;

        @BeforeEach
        void setUp() {
                JwtProperties jwtProperties = new JwtProperties(
                                ISSUER,
                                SECRET,
                                EXPIRATION_SECONDS);

                jwtTokenService = new JwtTokenServiceImpl(jwtProperties);
        }

        @Test
        void shouldRejectEmptyToken() {
                assertFalse(jwtTokenService.isTokenValid(""));
        }

        @Test
        void shouldRejectNullToken() {
                assertFalse(jwtTokenService.isTokenValid(null));
        }

        @Test
        void shouldRejectExpiredToken() {
                Instant issuedAt = Instant.now().minusSeconds(120);

                Instant expiresAt = Instant.now().minusSeconds(60);

                String token = Jwts.builder()
                                .issuer(ISSUER)
                                .subject("1")
                                .issuedAt(Date.from(issuedAt))
                                .expiration(Date.from(expiresAt))
                                .signWith(
                                                Keys.hmacShaKeyFor(
                                                                SECRET.getBytes(
                                                                                StandardCharsets.UTF_8)))
                                .compact();

                assertFalse(jwtTokenService.isTokenValid(token));
        }

        @Test
        void shouldRejectTokenWithNonNumericSubject() {
                Instant issuedAt = Instant.now();
                Instant expiresAt = issuedAt.plusSeconds(
                                EXPIRATION_SECONDS);

                String token = Jwts.builder()
                                .issuer(ISSUER)
                                .subject("invalid-user-id")
                                .issuedAt(Date.from(issuedAt))
                                .expiration(Date.from(expiresAt))
                                .signWith(
                                                Keys.hmacShaKeyFor(
                                                                SECRET.getBytes(
                                                                                StandardCharsets.UTF_8)))
                                .compact();

                assertFalse(jwtTokenService.isTokenValid(token));
        }

        @Test
        void shouldRejectTokenWithoutSubject() {
                Instant issuedAt = Instant.now();
                Instant expiresAt = issuedAt.plusSeconds(
                                EXPIRATION_SECONDS);

                String token = Jwts.builder()
                                .issuer(ISSUER)
                                .issuedAt(Date.from(issuedAt))
                                .expiration(Date.from(expiresAt))
                                .signWith(
                                                Keys.hmacShaKeyFor(
                                                                SECRET.getBytes(
                                                                                StandardCharsets.UTF_8)))
                                .compact();

                assertFalse(jwtTokenService.isTokenValid(token));
        }

        @Test
        void shouldGenerateValidToken() {
                AuthenticatedUser authenticatedUser = createAuthenticatedUser();

                String token = jwtTokenService.generateToken(authenticatedUser);

                assertNotNull(token);
                assertFalse(token.isBlank());
                assertTrue(jwtTokenService.isTokenValid(token));
        }

        @Test
        void shouldExtractUserIdFromValidToken() {
                AuthenticatedUser authenticatedUser = createAuthenticatedUser();

                String token = jwtTokenService.generateToken(authenticatedUser);

                Long userId = jwtTokenService.extractUserId(token);

                assertEquals(authenticatedUser.id(), userId);
        }

        @Test
        void shouldExposeConfiguredExpirationSeconds() {
                long expirationSeconds = jwtTokenService.getExpirationSeconds();

                assertEquals(EXPIRATION_SECONDS, expirationSeconds);
        }

        @Test
        void shouldRejectTokenSignedWithDifferentSecret() {
                AuthenticatedUser authenticatedUser = createAuthenticatedUser();

                String token = jwtTokenService.generateToken(authenticatedUser);

                JwtProperties differentProperties = new JwtProperties(
                                ISSUER,
                                "different-test-secret-key-with-at-least-32-characters",
                                EXPIRATION_SECONDS);

                JwtTokenService differentTokenService = new JwtTokenServiceImpl(differentProperties);

                assertFalse(differentTokenService.isTokenValid(token));
        }

        @Test
        void shouldRejectMalformedToken() {
                assertFalse(
                                jwtTokenService.isTokenValid(
                                                "this-is-not-a-valid-jwt"));
        }

        @Test
        void shouldRejectTokenWithDifferentIssuer() {
                AuthenticatedUser authenticatedUser = createAuthenticatedUser();

                JwtProperties differentIssuerProperties = new JwtProperties(
                                "another-issuer",
                                SECRET,
                                EXPIRATION_SECONDS);

                JwtTokenService differentIssuerService = new JwtTokenServiceImpl(differentIssuerProperties);

                String token = differentIssuerService.generateToken(authenticatedUser);

                assertFalse(jwtTokenService.isTokenValid(token));
        }

        private AuthenticatedUser createAuthenticatedUser() {
                return new AuthenticatedUser(
                                1L,
                                "receptionist",
                                "receptionist@agendoc.com",
                                "RECEPTIONIST",
                                1L);
        }
}