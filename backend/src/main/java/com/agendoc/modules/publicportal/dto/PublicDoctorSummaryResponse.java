package com.agendoc.modules.publicportal.dto;

/**
 * Public summary of a doctor displayed in the Digital Reception.
 */
public record PublicDoctorSummaryResponse(
        Long id,
        String firstName,
        String lastName,
        Long specialtyId,
        String specialtyName
) {
}