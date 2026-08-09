package com.agendoc.modules.appointment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request used by a doctor to register or update
 * the medical observation of an appointment.
 */
public record RegisterMedicalObservationRequest(

        @NotBlank(
                message = "La observación médica es obligatoria."
        )
        @Size(
                max = 2000,
                message = "La observación médica no puede superar los 2000 caracteres."
        )
        String observation
) {
}
