package com.agendoc.modules.appointment.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/**
 * Request used to reschedule an existing medical appointment.
 */
public record RescheduleAppointmentRequest(

        @NotNull(message = "El nuevo bloque de agenda es obligatorio.")
        @Positive(message = "El identificador del bloque de agenda debe ser mayor que cero.")
        Long agendaBlockId
) {
}