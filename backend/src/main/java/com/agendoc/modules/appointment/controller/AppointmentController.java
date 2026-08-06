package com.agendoc.modules.appointment.controller;

import com.agendoc.modules.appointment.dto.AppointmentAgendaResponse;
import com.agendoc.modules.appointment.dto.AppointmentResponse;
import com.agendoc.modules.appointment.dto.CancelAppointmentRequest;
import com.agendoc.modules.appointment.dto.CreateAppointmentRequest;
import com.agendoc.modules.appointment.dto.CreatePatientAppointmentRequest;
import com.agendoc.modules.appointment.dto.RegisterAppointmentNoShowRequest;
import com.agendoc.modules.appointment.dto.PatientAppointmentResponse;
import com.agendoc.modules.appointment.dto.PatientMedicalHistoryResponse;
import com.agendoc.modules.appointment.dto.RescheduleAppointmentRequest;
import com.agendoc.modules.appointment.dto.RegisterMedicalObservationRequest;
import com.agendoc.modules.appointment.dto.MedicalObservationResponse;
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
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * REST controller for medical appointment management operations.
 */
@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentController {

        private final AppointmentService appointmentService;

        @PreAuthorize("hasRole('RECEPTIONIST')")
        @PostMapping
        public ResponseEntity<AppointmentResponse> createAppointment(
                @Valid @RequestBody CreateAppointmentRequest request) {
                AppointmentResponse response = appointmentService.createAppointment(request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        @PreAuthorize("hasRole('PATIENT')")
        @PostMapping("/patient")
        public ResponseEntity<AppointmentResponse> createPatientAppointment(
                @Valid @RequestBody CreatePatientAppointmentRequest request) {

                AppointmentResponse response =
                        appointmentService.createPatientAppointment(request);

                return ResponseEntity
                        .status(HttpStatus.CREATED)
                        .body(response);
        }

        @PreAuthorize("hasRole('PATIENT')")
        @GetMapping("/patient")
        public ResponseEntity<List<PatientAppointmentResponse>>
                findPatientAppointments() {

                List<PatientAppointmentResponse> response =
                        appointmentService.findPatientAppointments();

                return ResponseEntity.ok(response);
        }


        @PreAuthorize("hasAnyRole('RECEPTIONIST', 'PATIENT')")
        @PatchMapping("/{appointmentId}/cancel")
        public ResponseEntity<AppointmentResponse> cancelAppointment(
                        @PathVariable Long appointmentId,
                        @Valid @RequestBody CancelAppointmentRequest request) {
                AppointmentResponse response = appointmentService.cancelAppointment(
                                appointmentId,
                                request);

                return ResponseEntity.ok(response);
        }

        @PreAuthorize("hasRole('RECEPTIONIST')")
        @PatchMapping("/{appointmentId}/confirm-arrival")
        public ResponseEntity<AppointmentResponse> confirmArrival(
                        @PathVariable Long appointmentId) {

                AppointmentResponse response = appointmentService.confirmArrival(
                                appointmentId);

                return ResponseEntity.ok(response);
        }

        @PreAuthorize("hasRole('RECEPTIONIST')")
        @PatchMapping("/{appointmentId}/no-show")
        public ResponseEntity<AppointmentResponse> registerNoShow(
                        @PathVariable Long appointmentId,
                        @Valid @RequestBody RegisterAppointmentNoShowRequest request) {

                AppointmentResponse response = appointmentService.registerNoShow(
                                appointmentId,
                                request);

                return ResponseEntity.ok(response);
        }

        @PreAuthorize("hasRole('RECEPTIONIST')")
        @PatchMapping("/{appointmentId}/reschedule")
        public ResponseEntity<AppointmentResponse> rescheduleAppointment(
                        @PathVariable Long appointmentId,
                        @Valid @RequestBody RescheduleAppointmentRequest request) {
                AppointmentResponse response = appointmentService.rescheduleAppointment(
                                appointmentId,
                                request);

                return ResponseEntity.ok(response);
        }

        @PreAuthorize("hasRole('DOCTOR')")
        @GetMapping("/{appointmentId}/patient-history")
        public ResponseEntity<List<PatientMedicalHistoryResponse>>
                findPatientMedicalHistory(
                        @PathVariable Long appointmentId) {

                List<PatientMedicalHistoryResponse> response =
                        appointmentService.findPatientMedicalHistory(
                                appointmentId
                        );

                return ResponseEntity.ok(response);
        }



        @PreAuthorize("hasRole('DOCTOR')")
        @GetMapping("/{appointmentId}/medical-observation")
        public ResponseEntity<MedicalObservationResponse>
                findMedicalObservation(
                        @PathVariable Long appointmentId) {

                MedicalObservationResponse response =
                        appointmentService.findMedicalObservation(
                                appointmentId
                        );

                return ResponseEntity.ok(response);
        }

        @PreAuthorize("hasRole('DOCTOR')")
        @PatchMapping("/{appointmentId}/medical-observation")
        public ResponseEntity<MedicalObservationResponse>
                registerMedicalObservation(
                        @PathVariable Long appointmentId,
                        @Valid @RequestBody
                        RegisterMedicalObservationRequest request) {

                MedicalObservationResponse response =
                        appointmentService.registerMedicalObservation(
                                appointmentId,
                                request
                        );

                return ResponseEntity.ok(response);
        }

        @PreAuthorize("hasRole('DOCTOR')")
        @PatchMapping("/{appointmentId}/attend")
        public ResponseEntity<AppointmentResponse>
                markAppointmentAsAttended(
                        @PathVariable Long appointmentId) {

                AppointmentResponse response =
                        appointmentService.markAppointmentAsAttended(
                                appointmentId
                        );

                return ResponseEntity.ok(response);
        }

        @PreAuthorize("hasRole('DOCTOR')")
        @GetMapping("/doctor")
        public ResponseEntity<List<AppointmentAgendaResponse>>
                findDoctorAppointments(
                        @RequestParam
                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                        LocalDate date,

                        @RequestParam(required = false)
                        String status) {

                List<AppointmentAgendaResponse> response =
                        appointmentService.findDoctorAppointments(
                                date,
                                status
                        );

                return ResponseEntity.ok(response);
        }

        @PreAuthorize("hasRole('RECEPTIONIST')")
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