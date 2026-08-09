package com.agendoc.modules.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

/**
 * Response containing one previous medical observation
 * associated with an attended appointment of a patient.
 */
public record PatientMedicalHistoryResponse(

        Long appointmentId,

        LocalDate appointmentDate,

        LocalTime startTime,

        Long doctorId,

        String doctorFirstName,

        String doctorLastName,

        Long specialtyId,

        String specialtyName,

        String medicalObservation,

        OffsetDateTime recordedAt,

        String recordedBy
) {
}
