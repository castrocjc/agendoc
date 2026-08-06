package com.agendoc.modules.appointment.dto;

import java.time.OffsetDateTime;

/**
 * Response containing the medical observation
 * associated with an appointment.
 */
public record MedicalObservationResponse(

        Long appointmentId,

        String observation,

        OffsetDateTime recordedAt,

        String recordedBy
) {
}
