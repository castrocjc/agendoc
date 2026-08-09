package com.agendoc.modules.publicportal.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.agendoc.modules.user.repository.UserRepository;
import com.agendoc.modules.publicportal.dto.FirstAppointmentRequest;
import com.agendoc.modules.publicportal.dto.FirstAppointmentResponse;
import com.agendoc.modules.publicportal.dto.PublicClinicResponse;
import com.agendoc.modules.publicportal.service.PublicReceptionService;
import com.agendoc.security.config.SecurityConfiguration;
import com.agendoc.security.handler.RestAccessDeniedHandler;
import com.agendoc.security.handler.RestAuthenticationEntryPoint;
import com.agendoc.security.jwt.JwtAuthenticationFilter;
import com.agendoc.security.jwt.JwtTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Verifies that the Digital Reception is publicly accessible while
 * non-public API resources remain protected by Spring Security.
 */
@WebMvcTest(
        controllers = PublicReceptionController.class,
        properties = {
                "agendoc.cors.allowed-origins=http://localhost:5173",
                "agendoc.security.jwt.secret=agendoc-test-secret-key-32-characters-minimum",
                "agendoc.security.jwt.expiration-seconds=3600"
        }
)
@AutoConfigureMockMvc
@Import({
        SecurityConfiguration.class,
        JwtAuthenticationFilter.class,
        RestAuthenticationEntryPoint.class,
        RestAccessDeniedHandler.class
})
class PublicReceptionSecurityIntegrationTest {

    private static final String CLINIC_SLUG =
            "agendoc-development-clinic";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PublicReceptionService publicReceptionService;

    @MockBean
    private JwtTokenService jwtTokenService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void shouldAllowPublicClinicEndpointWithoutJwt()
            throws Exception {

        PublicClinicResponse response =
                new PublicClinicResponse(
                        CLINIC_SLUG,
                        "AgenDoc Development Clinic",
                        null,
                        null,
                        "+52 55 0000 0000",
                        null,
                        "contacto@agendoc.local",
                        "Ciudad de México",
                        null
                );

        when(publicReceptionService.getClinic(CLINIC_SLUG))
                .thenReturn(response);

        mockMvc.perform(
                        get(
                                "/api/v1/public/clinics/{clinicSlug}",
                                CLINIC_SLUG
                        )
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.slug")
                                .value(CLINIC_SLUG)
                );

        verify(publicReceptionService)
                .getClinic(CLINIC_SLUG);
    }

    @Test
    void shouldAllowFirstAppointmentEndpointWithoutJwt()
            throws Exception {

        FirstAppointmentRequest request =
                new FirstAppointmentRequest(
                        "Juan",
                        "Castro",
                        "juan.security@example.com",
                        "+52 55 1234 5678",
                        "Password123!",
                        "Password123!",
                        100L,
                        5000L,
                        "Consulta inicial"
                );

        FirstAppointmentResponse response =
                new FirstAppointmentResponse(
                        4000L,
                        "Juan",
                        "Castro",
                        "juan.security@example.com",
                        100L,
                        "María",
                        "López",
                        10L,
                        "Cardiología",
                        LocalDate.of(2026, 8, 10),
                        LocalTime.of(9, 0),
                        LocalTime.of(9, 30),
                        "PROGRAMADA",
                        "Programada"
                );

        when(publicReceptionService.createFirstAppointment(
                eq(CLINIC_SLUG),
                eq(request)
        )).thenReturn(response);

        mockMvc.perform(
                        post(
                                "/api/v1/public/clinics/{clinicSlug}"
                                        + "/first-appointments",
                                CLINIC_SLUG
                        )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.appointmentId")
                                .value(4000L)
                );

        verify(publicReceptionService)
                .createFirstAppointment(
                        CLINIC_SLUG,
                        request
                );
    }

    @Test
    void shouldRejectProtectedEndpointWithoutJwt()
            throws Exception {

        mockMvc.perform(
                        get("/api/v1/patients")
                )
                .andExpect(status().isUnauthorized());
    }
}