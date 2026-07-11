package com.agendoc.modules.doctor.controller;

import com.agendoc.modules.doctor.dto.MedicalSpecialtyResponse;
import com.agendoc.modules.doctor.service.MedicalSpecialtyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for medical specialty queries.
 */
@RestController
@RequestMapping("/api/v1/medical-specialties")
@RequiredArgsConstructor
public class MedicalSpecialtyController {

    private final MedicalSpecialtyService medicalSpecialtyService;

    @GetMapping
    public ResponseEntity<List<MedicalSpecialtyResponse>>
            findActiveSpecialties() {

        List<MedicalSpecialtyResponse> response =
                medicalSpecialtyService.findActiveSpecialties();

        return ResponseEntity.ok(response);
    }
}