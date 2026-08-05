package com.agendoc.modules.publicportal.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Response returned after successfully registering a patient and
 * reserving the first appointment through the Digital Reception.
 */
public record FirstAppointmentResponse(

        Long appointmentId,

        String firstName,

        String lastName,

        String email,

        Long doctorId,

        String doctorFirstName,

        String doctorLastName,

        Long specialtyId,

        String specialtyName,

        LocalDate appointmentDate,

        LocalTime startTime,

        LocalTime endTime,

        String statusCode,

        String statusName
) {
}