package com.agendoc.modules.agenda.service;

import com.agendoc.modules.agenda.dto.AgendaBlockResponse;
import com.agendoc.modules.agenda.dto.CreateAgendaBlocksRequest;
import com.agendoc.modules.agenda.dto.CreateAgendaBlocksResponse;
import java.time.LocalDate;
import java.util.List;

/**
 * Defines medical agenda management use cases.
 */
public interface AgendaService {

    CreateAgendaBlocksResponse createAgendaBlocks(
            Long doctorId,
            CreateAgendaBlocksRequest request
    );

    List<AgendaBlockResponse> findAgendaBlocks(
            Long doctorId,
            LocalDate appointmentDate
    );
}