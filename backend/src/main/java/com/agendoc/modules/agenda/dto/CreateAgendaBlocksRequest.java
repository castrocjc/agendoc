package com.agendoc.modules.agenda.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record CreateAgendaBlocksRequest(

        @NotEmpty(message = "Debe registrar al menos un bloque de agenda")
        List<@Valid AgendaBlockItemRequest> blocks
) {
}