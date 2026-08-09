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
import org.springframework.security.authentication.InsufficientAuthenticationException;

class RestAuthenticationEntryPointTest {

        private ObjectMapper objectMapper;
        private RestAuthenticationEntryPoint authenticationEntryPoint;

        @BeforeEach
        void setUp() {
                objectMapper = new ObjectMapper().findAndRegisterModules();

                authenticationEntryPoint = new RestAuthenticationEntryPoint(objectMapper);
        }

        @Test
        void shouldWriteConsistentUnauthorizedApiError() throws Exception {
                MockHttpServletRequest request = new MockHttpServletRequest(
                                "GET",
                                "/api/v1/appointments");

                MockHttpServletResponse response = new MockHttpServletResponse();

                authenticationEntryPoint.commence(
                                request,
                                response,
                                new InsufficientAuthenticationException(
                                                "Authentication is required"));

                assertEquals(
                                HttpStatus.UNAUTHORIZED.value(),
                                response.getStatus());

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
                                response.getCharacterEncoding());

                JsonNode body = objectMapper.readTree(
                                response.getContentAsString());

                assertEquals(
                                HttpStatus.UNAUTHORIZED.value(),
                                body.get("status").asInt());

                assertEquals(
                                HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                                body.get("error").asText());

                assertEquals(
                                "Debes iniciar sesión para acceder a este recurso.",
                                body.get("message").asText());

                assertEquals(
                                "/api/v1/appointments",
                                body.get("path").asText());

                assertFalse(
                                body.get("timestamp").asText().isBlank());

                assertFalse(
                                response.getContentAsString()
                                                .contains("Authentication is required"));
        }
}