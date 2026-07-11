package com.agendoc.modules.doctor.dto;

/**
 * Response contract for registered doctor information.
 */
public record DoctorResponse(
        Long id,
        Long clinicId,
        Long specialtyId,
        String specialtyName,
        String firstName,
        String lastName,
        String documentType,
        String documentNumber,
        String medicalLicenseNumber,
        String phone,
        String email,
        String recordStatus
) {
}