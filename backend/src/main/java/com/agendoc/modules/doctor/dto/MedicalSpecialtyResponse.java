package com.agendoc.modules.doctor.dto;

/**
 * Response contract for medical specialty information.
 */
public record MedicalSpecialtyResponse(
        Long id,
        String code,
        String name,
        String description
) {
}