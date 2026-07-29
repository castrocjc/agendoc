package com.agendoc.modules.appointment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request used to create a medical appointment.
 */
public record CreateAppointmentRequest(

        @NotNull(message = "El paciente es obligatorio.")
        Long patientId,

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