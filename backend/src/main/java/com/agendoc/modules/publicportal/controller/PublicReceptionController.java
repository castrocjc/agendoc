package com.agendoc.modules.publicportal.controller;

import com.agendoc.modules.publicportal.dto.FirstAppointmentRequest;
import com.agendoc.modules.publicportal.dto.FirstAppointmentResponse;
import com.agendoc.modules.publicportal.dto.PublicAgendaAvailabilityResponse;
import com.agendoc.modules.publicportal.dto.PublicClinicResponse;
import com.agendoc.modules.publicportal.dto.PublicDoctorSummaryResponse;
import com.agendoc.modules.publicportal.dto.PublicSpecialtyResponse;
import com.agendoc.modules.publicportal.service.PublicReceptionService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public REST controller for the clinic Digital Reception.
 */
@RestController
@RequestMapping("/api/v1/public/clinics/{clinicSlug}")
@RequiredArgsConstructor
public class PublicReceptionController {

    private final PublicReceptionService publicReceptionService;

    @GetMapping
    public ResponseEntity<PublicClinicResponse> getClinic(
            @PathVariable String clinicSlug
    ) {
        PublicClinicResponse response =
                publicReceptionService.getClinic(
                        clinicSlug
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/specialties")
    public ResponseEntity<List<PublicSpecialtyResponse>>
    getSpecialties(
            @PathVariable String clinicSlug
    ) {
        List<PublicSpecialtyResponse> response =
                publicReceptionService.getSpecialties(
                        clinicSlug
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<PublicDoctorSummaryResponse>>
    getDoctors(
            @PathVariable String clinicSlug,
            @RequestParam(required = false)
            Long specialtyId
    ) {
        List<PublicDoctorSummaryResponse> response =
                publicReceptionService.getDoctors(
                        clinicSlug,
                        specialtyId
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/doctors/{doctorId}/availability")
    public ResponseEntity<List<PublicAgendaAvailabilityResponse>>
    getAvailability(
            @PathVariable String clinicSlug,
            @PathVariable Long doctorId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        List<PublicAgendaAvailabilityResponse> response =
                publicReceptionService.getAvailability(
                        clinicSlug,
                        doctorId,
                        date
                );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/first-appointments")
    public ResponseEntity<FirstAppointmentResponse>
    createFirstAppointment(
            @PathVariable String clinicSlug,
            @Valid
            @RequestBody
            FirstAppointmentRequest request
    ) {
        FirstAppointmentResponse response =
                publicReceptionService.createFirstAppointment(
                        clinicSlug,
                        request
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
}