package com.agendoc.modules.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Response containing the information required to display
 * an appointment in the clinic agenda.
 */
public record AppointmentAgendaResponse(

        Long id,

        Long patientId,

        String patientFirstName,

        String patientLastName,

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

        String reason
) {
}