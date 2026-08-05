package com.agendoc.modules.publicportal.controller;

import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.mockito.Mockito.never;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.agendoc.modules.publicportal.dto.FirstAppointmentRequest;
import com.agendoc.modules.publicportal.dto.FirstAppointmentResponse;
import com.agendoc.modules.publicportal.dto.PublicAgendaAvailabilityResponse;
import com.agendoc.modules.publicportal.dto.PublicClinicResponse;
import com.agendoc.modules.publicportal.dto.PublicDoctorSummaryResponse;
import com.agendoc.modules.publicportal.dto.PublicSpecialtyResponse;
import com.agendoc.modules.publicportal.service.PublicReceptionService;
import com.agendoc.security.config.SecurityConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.agendoc.security.handler.RestAccessDeniedHandler;
import com.agendoc.security.handler.RestAuthenticationEntryPoint;
import com.agendoc.security.jwt.JwtAuthenticationFilter;

@WebMvcTest(controllers = PublicReceptionController.class, properties = {
                "agendoc.security.jwt.secret=agendoc-test-secret-key-32-characters-minimum",
                "agendoc.security.jwt.expiration-seconds=3600"
}, excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {
                                SecurityConfiguration.class,
                                JwtAuthenticationFilter.class,
                                RestAuthenticationEntryPoint.class,
                                RestAccessDeniedHandler.class
                })
})
@AutoConfigureMockMvc(addFilters = false)
class PublicReceptionControllerTest {

        private static final String CLINIC_SLUG = "agendoc-development-clinic";

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private PublicReceptionService publicReceptionService;

        @Test
        void shouldReturnPublicClinic() throws Exception {
                PublicClinicResponse response = new PublicClinicResponse(
                                CLINIC_SLUG,
                                "AgenDoc Development Clinic",
                                "Atención médica para desarrollo.",
                                "https://cdn.agendoc.local/logo.png",
                                "+52 55 0000 0000",
                                "+52 55 1111 1111",
                                "contacto@agendoc.local",
                                "Ciudad de México",
                                "https://maps.example.com/agendoc");

                when(publicReceptionService.getClinic(CLINIC_SLUG))
                                .thenReturn(response);

                mockMvc.perform(
                                get(
                                                "/api/v1/public/clinics/{clinicSlug}",
                                                CLINIC_SLUG))
                                .andExpect(status().isOk())
                                .andExpect(
                                                content().contentTypeCompatibleWith(
                                                                MediaType.APPLICATION_JSON))
                                .andExpect(
                                                jsonPath("$.slug")
                                                                .value(CLINIC_SLUG))
                                .andExpect(
                                                jsonPath("$.publicName")
                                                                .value("AgenDoc Development Clinic"))
                                .andExpect(
                                                jsonPath("$.email")
                                                                .value("contacto@agendoc.local"));

                verify(publicReceptionService)
                                .getClinic(CLINIC_SLUG);
        }

        @Test
        void shouldReturnPublicSpecialties() throws Exception {
                List<PublicSpecialtyResponse> response = List.of(
                                new PublicSpecialtyResponse(
                                                10L,
                                                "Cardiología"),
                                new PublicSpecialtyResponse(
                                                20L,
                                                "Dermatología"));

                when(publicReceptionService.getSpecialties(CLINIC_SLUG))
                                .thenReturn(response);

                mockMvc.perform(
                                get(
                                                "/api/v1/public/clinics/{clinicSlug}/specialties",
                                                CLINIC_SLUG))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value(10L))
                                .andExpect(
                                                jsonPath("$[0].name")
                                                                .value("Cardiología"))
                                .andExpect(jsonPath("$[1].id").value(20L))
                                .andExpect(
                                                jsonPath("$[1].name")
                                                                .value("Dermatología"));

                verify(publicReceptionService)
                                .getSpecialties(CLINIC_SLUG);
        }

        @Test
        void shouldReturnPublicDoctorsFilteredBySpecialty()
                        throws Exception {

                Long specialtyId = 10L;

                List<PublicDoctorSummaryResponse> response = List.of(
                                new PublicDoctorSummaryResponse(
                                                100L,
                                                "María",
                                                "López",
                                                specialtyId,
                                                "Cardiología"));

                when(publicReceptionService.getDoctors(
                                CLINIC_SLUG,
                                specialtyId)).thenReturn(response);

                mockMvc.perform(
                                get(
                                                "/api/v1/public/clinics/{clinicSlug}/doctors",
                                                CLINIC_SLUG)
                                                .param(
                                                                "specialtyId",
                                                                specialtyId.toString()))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value(100L))
                                .andExpect(
                                                jsonPath("$[0].firstName")
                                                                .value("María"))
                                .andExpect(
                                                jsonPath("$[0].lastName")
                                                                .value("López"))
                                .andExpect(
                                                jsonPath("$[0].specialtyId")
                                                                .value(10L))
                                .andExpect(
                                                jsonPath("$[0].specialtyName")
                                                                .value("Cardiología"));

                verify(publicReceptionService)
                                .getDoctors(
                                                CLINIC_SLUG,
                                                specialtyId);
        }

        @Test
        void shouldReturnDoctorAvailability() throws Exception {
                Long doctorId = 100L;
                LocalDate appointmentDate = LocalDate.of(2026, 8, 10);

                List<PublicAgendaAvailabilityResponse> response = List.of(
                                new PublicAgendaAvailabilityResponse(
                                                5000L,
                                                appointmentDate,
                                                LocalTime.of(9, 0),
                                                LocalTime.of(9, 30)));

                when(publicReceptionService.getAvailability(
                                CLINIC_SLUG,
                                doctorId,
                                appointmentDate)).thenReturn(response);

                mockMvc.perform(
                                get(
                                                "/api/v1/public/clinics/{clinicSlug}"
                                                                + "/doctors/{doctorId}/availability",
                                                CLINIC_SLUG,
                                                doctorId)
                                                .param(
                                                                "date",
                                                                appointmentDate.toString()))
                                .andExpect(status().isOk())
                                .andExpect(
                                                jsonPath("$[0].agendaBlockId")
                                                                .value(5000L))
                                .andExpect(
                                                jsonPath("$[0].appointmentDate")
                                                                .value("2026-08-10"))
                                .andExpect(
                                                jsonPath("$[0].startTime")
                                                                .value("09:00:00"))
                                .andExpect(
                                                jsonPath("$[0].endTime")
                                                                .value("09:30:00"));

                verify(publicReceptionService)
                                .getAvailability(
                                                CLINIC_SLUG,
                                                doctorId,
                                                appointmentDate);
        }

        @Test
        void shouldCreateFirstAppointment() throws Exception {
                FirstAppointmentRequest request = new FirstAppointmentRequest(
                                "Juan Carlos",
                                "Castro Cruz",
                                "juan.castro@example.com",
                                "+52 55 1234 5678",
                                "Password123!",
                                "Password123!",
                                100L,
                                5000L,
                                "Consulta inicial");

                FirstAppointmentResponse response = new FirstAppointmentResponse(
                                4000L,
                                "Juan Carlos",
                                "Castro Cruz",
                                "juan.castro@example.com",
                                100L,
                                "María",
                                "López",
                                10L,
                                "Cardiología",
                                LocalDate.of(2026, 8, 10),
                                LocalTime.of(9, 0),
                                LocalTime.of(9, 30),
                                "PROGRAMADA",
                                "Programada");

                when(publicReceptionService.createFirstAppointment(
                                eq(CLINIC_SLUG),
                                eq(request))).thenReturn(response);

                mockMvc.perform(
                                post(
                                                "/api/v1/public/clinics/{clinicSlug}"
                                                                + "/first-appointments",
                                                CLINIC_SLUG)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(
                                                                objectMapper.writeValueAsString(
                                                                                request)))
                                .andExpect(status().isCreated())
                                .andExpect(
                                                content().contentTypeCompatibleWith(
                                                                MediaType.APPLICATION_JSON))
                                .andExpect(
                                                jsonPath("$.appointmentId")
                                                                .value(4000L))
                                .andExpect(
                                                jsonPath("$.firstName")
                                                                .value("Juan Carlos"))
                                .andExpect(
                                                jsonPath("$.email")
                                                                .value("juan.castro@example.com"))
                                .andExpect(
                                                jsonPath("$.doctorId")
                                                                .value(100L))
                                .andExpect(
                                                jsonPath("$.specialtyName")
                                                                .value("Cardiología"))
                                .andExpect(
                                                jsonPath("$.appointmentDate")
                                                                .value("2026-08-10"))
                                .andExpect(
                                                jsonPath("$.statusCode")
                                                                .value("PROGRAMADA"));

                verify(publicReceptionService)
                                .createFirstAppointment(
                                                CLINIC_SLUG,
                                                request);
        }

        @Test
        void shouldRejectRequestWhenFirstNameIsBlank()
                        throws Exception {

                FirstAppointmentRequest request = new FirstAppointmentRequest(
                                "",
                                "Castro",
                                "juan@example.com",
                                "+52 55 1234 5678",
                                "Password123!",
                                "Password123!",
                                100L,
                                5000L,
                                null);

                mockMvc.perform(
                                post(
                                                "/api/v1/public/clinics/{clinicSlug}"
                                                                + "/first-appointments",
                                                CLINIC_SLUG)
                                                .contentType(
                                                                MediaType.APPLICATION_JSON)
                                                .content(
                                                                objectMapper.writeValueAsString(
                                                                                request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(
                                                jsonPath("$.error")
                                                                .value("Bad Request"))
                                .andExpect(
                                                jsonPath("$.message")
                                                                .value(
                                                                                not(blankOrNullString())))
                                .andExpect(
                                                jsonPath("$.path")
                                                                .value(
                                                                                "/api/v1/public/clinics/"
                                                                                                + CLINIC_SLUG
                                                                                                + "/first-appointments"));

                verify(publicReceptionService, never())
                                .createFirstAppointment(
                                                anyString(),
                                                any(FirstAppointmentRequest.class));
        }

        @Test
        void shouldRejectRequestWhenEmailIsInvalid() throws Exception {

                FirstAppointmentRequest request = new FirstAppointmentRequest(
                                "Juan",
                                "Castro",
                                "correo-invalido",
                                "+52 55 1234 5678",
                                "Password123!",
                                "Password123!",
                                100L,
                                5000L,
                                null);

                mockMvc.perform(
                                post("/api/v1/public/clinics/{clinicSlug}/first-appointments",
                                                CLINIC_SLUG)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(jsonPath("$.error").value("Bad Request"))
                                .andExpect(
                                                jsonPath("$.message")
                                                                .value(not(blankOrNullString())))
                                .andExpect(
                                                jsonPath("$.path")
                                                                .value(
                                                                                "/api/v1/public/clinics/"
                                                                                                + CLINIC_SLUG
                                                                                                + "/first-appointments"));

                verify(publicReceptionService, never())
                                .createFirstAppointment(
                                                anyString(),
                                                any(FirstAppointmentRequest.class));
        }

        @Test
        void shouldRejectRequestWhenPasswordIsTooShort() throws Exception {

                FirstAppointmentRequest request = new FirstAppointmentRequest(
                                "Juan",
                                "Castro",
                                "juan@example.com",
                                "+52 55 1234 5678",
                                "123",
                                "123",
                                100L,
                                5000L,
                                null);

                mockMvc.perform(
                                post("/api/v1/public/clinics/{clinicSlug}/first-appointments",
                                                CLINIC_SLUG)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(jsonPath("$.error").value("Bad Request"))
                                .andExpect(
                                                jsonPath("$.message")
                                                                .value(not(blankOrNullString())))
                                .andExpect(
                                                jsonPath("$.path")
                                                                .value(
                                                                                "/api/v1/public/clinics/"
                                                                                                + CLINIC_SLUG
                                                                                                + "/first-appointments"));

                verify(publicReceptionService, never())
                                .createFirstAppointment(
                                                anyString(),
                                                any(FirstAppointmentRequest.class));
        }

        @Test
        void shouldRejectRequestWhenDoctorIdIsNull() throws Exception {

                FirstAppointmentRequest request = new FirstAppointmentRequest(
                                "Juan",
                                "Castro",
                                "juan@example.com",
                                "+52 55 1234 5678",
                                "Password123!",
                                "Password123!",
                                null,
                                5000L,
                                null);

                mockMvc.perform(
                                post("/api/v1/public/clinics/{clinicSlug}/first-appointments",
                                                CLINIC_SLUG)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(jsonPath("$.error").value("Bad Request"))
                                .andExpect(
                                                jsonPath("$.message")
                                                                .value(not(blankOrNullString())))
                                .andExpect(
                                                jsonPath("$.path")
                                                                .value(
                                                                                "/api/v1/public/clinics/"
                                                                                                + CLINIC_SLUG
                                                                                                + "/first-appointments"));

                verify(publicReceptionService, never())
                                .createFirstAppointment(
                                                anyString(),
                                                any(FirstAppointmentRequest.class));
        }

        @Test
        void shouldRejectRequestWhenAgendaBlockIdIsNull() throws Exception {

                FirstAppointmentRequest request = new FirstAppointmentRequest(
                                "Juan",
                                "Castro",
                                "juan@example.com",
                                "+52 55 1234 5678",
                                "Password123!",
                                "Password123!",
                                100L,
                                null,
                                null);

                mockMvc.perform(
                                post("/api/v1/public/clinics/{clinicSlug}/first-appointments",
                                                CLINIC_SLUG)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(jsonPath("$.error").value("Bad Request"))
                                .andExpect(
                                                jsonPath("$.message")
                                                                .value(not(blankOrNullString())))
                                .andExpect(
                                                jsonPath("$.path")
                                                                .value(
                                                                                "/api/v1/public/clinics/"
                                                                                                + CLINIC_SLUG
                                                                                                + "/first-appointments"));

                verify(publicReceptionService, never())
                                .createFirstAppointment(
                                                anyString(),
                                                any(FirstAppointmentRequest.class));
        }

        @Test
        void shouldRejectRequestWhenLastNameIsBlank()
                        throws Exception {

                FirstAppointmentRequest request = new FirstAppointmentRequest(
                                "Juan",
                                "   ",
                                "juan@example.com",
                                "+52 55 1234 5678",
                                "Password123!",
                                "Password123!",
                                100L,
                                5000L,
                                null);

                mockMvc.perform(
                                post(
                                                "/api/v1/public/clinics/{clinicSlug}"
                                                                + "/first-appointments",
                                                CLINIC_SLUG)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(
                                                                objectMapper.writeValueAsString(
                                                                                request)))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(
                                                jsonPath("$.error")
                                                                .value("Bad Request"))
                                .andExpect(
                                                jsonPath("$.message")
                                                                .value(
                                                                                not(blankOrNullString())))
                                .andExpect(
                                                jsonPath("$.path")
                                                                .value(
                                                                                "/api/v1/public/clinics/"
                                                                                                + CLINIC_SLUG
                                                                                                + "/first-appointments"));

                verify(publicReceptionService, never())
                                .createFirstAppointment(
                                                anyString(),
                                                any(FirstAppointmentRequest.class));
        }

        @Test
        void shouldRejectRequestWhenBodyIsMissing()
                        throws Exception {

                mockMvc.perform(
                                post(
                                                "/api/v1/public/clinics/{clinicSlug}"
                                                                + "/first-appointments",
                                                CLINIC_SLUG)
                                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(
                                                jsonPath("$.error")
                                                                .value("Bad Request"))
                                .andExpect(
                                                jsonPath("$.message")
                                                                .value(
                                                                                "El cuerpo de la solicitud es obligatorio "
                                                                                                + "y debe contener un JSON válido."))
                                .andExpect(
                                                jsonPath("$.path")
                                                                .value(
                                                                                "/api/v1/public/clinics/"
                                                                                                + CLINIC_SLUG
                                                                                                + "/first-appointments"));

                verify(publicReceptionService, never())
                                .createFirstAppointment(
                                                anyString(),
                                                any(FirstAppointmentRequest.class));
        }

        @Test
        void shouldRejectRequestWhenJsonIsInvalid()
                        throws Exception {

                String invalidJson = """
                                {
                                  "firstName": "Juan",
                                  "lastName":
                                }
                                """;

                mockMvc.perform(
                                post(
                                                "/api/v1/public/clinics/{clinicSlug}"
                                                                + "/first-appointments",
                                                CLINIC_SLUG)
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(invalidJson))
                                .andExpect(status().isBadRequest())
                                .andExpect(jsonPath("$.status").value(400))
                                .andExpect(
                                                jsonPath("$.error")
                                                                .value("Bad Request"))
                                .andExpect(
                                                jsonPath("$.message")
                                                                .value(
                                                                                "El cuerpo de la solicitud es obligatorio "
                                                                                                + "y debe contener un JSON válido."))
                                .andExpect(
                                                jsonPath("$.path")
                                                                .value(
                                                                                "/api/v1/public/clinics/"
                                                                                                + CLINIC_SLUG
                                                                                                + "/first-appointments"));

                verify(publicReceptionService, never())
                                .createFirstAppointment(
                                                anyString(),
                                                any(FirstAppointmentRequest.class));
        }

}