package com.agendoc.modules.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Response containing the information required by a patient
 * to review one of their medical appointments.
 */
public record PatientAppointmentResponse(

        Long id,

        Long doctorId,

        String doctorFirstName,

        String doctorLastName,

        Long specialtyId,

        String specialtyName,

        Long agendaBlockId,

        LocalDate appointmentDate,

        LocalTime startTime,

        LocalTime endTime,

        String statusCode,

        String statusName,

        String reason,

        String cancellationReason
) {
}
