package com.agendoc.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.agendoc.security.authorization.AuthorizationDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
        request = mock(HttpServletRequest.class);

        when(request.getRequestURI())
                .thenReturn("/api/v1/appointments");
    }

    @Test
    void shouldReturnForbiddenForAuthorizationDenied() {

        AuthorizationDeniedException exception =
                new AuthorizationDeniedException(
                        "No tienes autorización para realizar esta operación."
                );

        ResponseEntity<ApiError> response =
                exceptionHandler.handleAuthorizationDenied(
                        exception,
                        request
                );

        assertEquals(
                HttpStatus.FORBIDDEN,
                response.getStatusCode()
        );

        assertNotNull(response.getBody());

        assertEquals(
                HttpStatus.FORBIDDEN.value(),
                response.getBody().status()
        );

        assertEquals(
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                response.getBody().error()
        );

        assertEquals(
                "No tienes autorización para realizar esta operación.",
                response.getBody().message()
        );

        assertEquals(
                "/api/v1/appointments",
                response.getBody().path()
        );

        assertNotNull(
                response.getBody().timestamp()
        );
    }
}