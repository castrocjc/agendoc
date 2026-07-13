package com.agendoc.modules.agenda.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record AgendaBlockItemRequest(

        @NotNull(message = "La fecha es obligatoria")
        @FutureOrPresent(message = "La fecha no puede estar en el pasado")
        LocalDate appointmentDate,

        @NotNull(message = "La hora de inicio es obligatoria")
        LocalTime startTime,

        @NotNull(message = "La hora de fin es obligatoria")
        LocalTime endTime
) {

    @AssertTrue(message = "La hora de inicio debe ser anterior a la hora de fin")
    public boolean isValidTimeRange() {
        if (startTime == null || endTime == null) {
            return true;
        }

        return startTime.isBefore(endTime);
    }
}