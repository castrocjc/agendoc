package com.agendoc.security.authorization;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class AuthorizationDeniedExceptionTest {

    @Test
    void shouldPreserveAuthorizationMessage() {

        AuthorizationDeniedException exception =
                new AuthorizationDeniedException(
                        "No tienes autorización para realizar esta operación."
                );

        assertEquals(
                "No tienes autorización para realizar esta operación.",
                exception.getMessage()
        );
    }
}