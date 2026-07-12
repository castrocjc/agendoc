package com.agendoc.modules.patient.controller;

import com.agendoc.modules.patient.dto.CreatePatientRequest;
import com.agendoc.modules.patient.dto.PatientResponse;
import com.agendoc.modules.patient.service.PatientService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for patient management operations.
 */
@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientController {

        private final PatientService patientService;

        @PostMapping
        public ResponseEntity<PatientResponse> createPatient(
                        @Valid @RequestBody CreatePatientRequest request) {
                PatientResponse response = patientService.createPatient(request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        @GetMapping("/search")
        public ResponseEntity<List<PatientResponse>> searchPatients(
                        @RequestParam(name = "query") String query) {
                List<PatientResponse> response = patientService.searchPatients(query);

                return ResponseEntity.ok(response);
        }
}