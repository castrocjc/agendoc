package com.agendoc.modules.appointment.dto;

import jakarta.validation.constraints.Size;

/**
 * Request used to register a patient no-show.
 */
public record RegisterAppointmentNoShowRequest(

        @Size(
                max = 500,
                message = "El comentario no puede exceder 500 caracteres."
        )
        String comment

) {
}