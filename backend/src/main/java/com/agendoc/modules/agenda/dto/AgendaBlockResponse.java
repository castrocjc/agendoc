package com.agendoc.modules.agenda.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendaBlockResponse(
        Long id,
        LocalDate appointmentDate,
        LocalTime startTime,
        LocalTime endTime,
        boolean available
) {
}