package com.agendoc.modules.appointment.controller;

import com.agendoc.modules.appointment.dto.AppointmentAgendaResponse;
import com.agendoc.modules.appointment.dto.AppointmentResponse;
import com.agendoc.modules.appointment.dto.CreateAppointmentRequest;
import com.agendoc.modules.appointment.dto.CancelAppointmentRequest;
import com.agendoc.modules.appointment.dto.RescheduleAppointmentRequest;
import com.agendoc.modules.appointment.service.AppointmentService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * REST controller for medical appointment management operations.
 */
@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

        private final AppointmentService appointmentService;

        @PostMapping
        public ResponseEntity<AppointmentResponse> createAppointment(
                        @Valid @RequestBody CreateAppointmentRequest request) {
                AppointmentResponse response = appointmentService.createAppointment(request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        @PatchMapping("/{appointmentId}/cancel")
        public ResponseEntity<AppointmentResponse> cancelAppointment(
                        @PathVariable Long appointmentId,
                        @Valid @RequestBody CancelAppointmentRequest request) {
                AppointmentResponse response = appointmentService.cancelAppointment(
                                appointmentId,
                                request);

                return ResponseEntity.ok(response);
        }

        @PatchMapping("/{appointmentId}/reschedule")
        public ResponseEntity<AppointmentResponse> rescheduleAppointment(
                        @PathVariable Long appointmentId,
                        @Valid @RequestBody RescheduleAppointmentRequest request) {
                AppointmentResponse response = appointmentService.rescheduleAppointment(
                                appointmentId,
                                request);

                return ResponseEntity.ok(response);
        }

        @GetMapping
        public ResponseEntity<List<AppointmentAgendaResponse>> findAppointments(
                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,

                        @RequestParam(required = false) Long doctorId,

                        @RequestParam(required = false) String status) {
                List<AppointmentAgendaResponse> response = appointmentService.findAppointments(
                                date,
                                doctorId,
                                status);

                return ResponseEntity.ok(response);
        }
}