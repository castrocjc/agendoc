package com.agendoc.modules.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Response containing the information of a medical appointment.
 */
public record AppointmentResponse(

        Long id,

        Long clinicId,

        Long patientId,

        String patientFirstName,

        String patientLastName,

        Long doctorId,

        String doctorFirstName,

        String doctorLastName,

        Long agendaBlockId,

        LocalDate appointmentDate,

        LocalTime startTime,

        LocalTime endTime,

        String statusCode,

        String statusName,

        String reason,

        String notes,

        String recordStatus
) {
}