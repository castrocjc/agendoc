package com.agendoc.modules.patient.dto;

import java.time.LocalDate;

/**
 * Response contract for registered patient information.
 */
public record PatientResponse(
        Long id,
        Long clinicId,
        String firstName,
        String lastName,
        String documentType,
        String documentNumber,
        LocalDate birthDate,
        String phone,
        String email,
        String address,
        String recordStatus
) {
}