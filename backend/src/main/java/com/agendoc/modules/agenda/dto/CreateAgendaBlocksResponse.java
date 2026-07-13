package com.agendoc.modules.agenda.dto;

import java.util.List;

public record CreateAgendaBlocksResponse(
        Long doctorId,
        Long medicalAgendaId,
        List<AgendaBlockResponse> createdBlocks
) {
}