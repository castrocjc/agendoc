package com.agendoc.modules.publicportal.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Public representation of an available appointment slot.
 */
public record PublicAgendaAvailabilityResponse(
        Long agendaBlockId,
        LocalDate appointmentDate,
        LocalTime startTime,
        LocalTime endTime
) {
}