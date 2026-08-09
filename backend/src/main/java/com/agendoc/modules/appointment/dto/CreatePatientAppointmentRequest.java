package com.agendoc.modules.appointment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request used by an authenticated patient to reserve
 * a medical appointment.
 *
 * The patient and clinic identifiers are obtained from
 * the authenticated user's domain context.
 */
public record CreatePatientAppointmentRequest(

        @NotNull(message = "El médico es obligatorio.")
        Long doctorId,

        @NotNull(message = "El bloque de agenda es obligatorio.")
        Long agendaBlockId,

        @Size(
                max = 500,
                message = "El motivo de la cita no puede superar los 500 caracteres."
        )
        String reason,

        @Size(
                max = 1000,
                message = "Las observaciones no pueden superar los 1000 caracteres."
        )
        String notes
) {
}