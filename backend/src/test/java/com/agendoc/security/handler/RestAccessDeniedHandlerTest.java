package com.agendoc.security.handler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;

class RestAccessDeniedHandlerTest {

    private ObjectMapper objectMapper;
    private RestAccessDeniedHandler accessDeniedHandler;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper().findAndRegisterModules();

        accessDeniedHandler =
                new RestAccessDeniedHandler(objectMapper);
    }

    @Test
    void shouldWriteConsistentForbiddenApiError() throws Exception {
        MockHttpServletRequest request =
                new MockHttpServletRequest(
                        "PATCH",
                        "/api/v1/appointments/25/cancel"
                );

        MockHttpServletResponse response =
                new MockHttpServletResponse();

        accessDeniedHandler.handle(
                request,
                response,
                new AccessDeniedException(
                        "Required authority was not found"
                )
        );

        assertEquals(
                HttpStatus.FORBIDDEN.value(),
                response.getStatus()
        );

        MediaType contentType =
                MediaType.parseMediaType(
                        response.getContentType()
                );

        assertEquals(
                MediaType.APPLICATION_JSON.getType(),
                contentType.getType()
        );

        assertEquals(
                MediaType.APPLICATION_JSON.getSubtype(),
                contentType.getSubtype()
        );

        assertEquals(
                "UTF-8",
                response.getCharacterEncoding()
        );

        JsonNode body =
                objectMapper.readTree(
                        response.getContentAsString()
                );

        assertEquals(
                HttpStatus.FORBIDDEN.value(),
                body.get("status").asInt()
        );

        assertEquals(
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                body.get("error").asText()
        );

        assertEquals(
                "No tienes autorización para realizar esta operación.",
                body.get("message").asText()
        );

        assertEquals(
                "/api/v1/appointments/25/cancel",
                body.get("path").asText()
        );

        assertFalse(
                body.get("timestamp").asText().isBlank()
        );

        assertFalse(
                response.getContentAsString()
                        .contains("Required authority was not found")
        );
    }
}