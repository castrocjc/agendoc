package com.agendoc.modules.doctor.controller;

import java.util.List;

import com.agendoc.modules.doctor.dto.CreateDoctorRequest;
import com.agendoc.modules.doctor.dto.DoctorResponse;
import com.agendoc.modules.doctor.service.DoctorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for doctor management operations.
 */
@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorController {

        private final DoctorService doctorService;

        @GetMapping
        public ResponseEntity<List<DoctorResponse>> findDoctors() {

        return ResponseEntity.ok(
                doctorService.findDoctors()
        );
        }

        @PostMapping
        public ResponseEntity<DoctorResponse> createDoctor(
                        @Valid @RequestBody CreateDoctorRequest request) {

                DoctorResponse response = doctorService.createDoctor(request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }
}