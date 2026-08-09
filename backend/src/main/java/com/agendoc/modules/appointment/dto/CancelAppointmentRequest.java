package com.agendoc.modules.appointment.dto;

import jakarta.validation.constraints.Size;

/**
 * Request used to cancel a medical appointment.
 */
public record CancelAppointmentRequest(

        @Size(
                max = 500,
                message = "El motivo no puede exceder 500 caracteres."
        )
        String reason

) {
}