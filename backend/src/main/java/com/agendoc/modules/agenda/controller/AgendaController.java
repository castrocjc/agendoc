package com.agendoc.modules.agenda.controller;

import com.agendoc.modules.agenda.dto.AgendaBlockResponse;
import com.agendoc.modules.agenda.dto.CreateAgendaBlocksRequest;
import com.agendoc.modules.agenda.dto.CreateAgendaBlocksResponse;
import com.agendoc.modules.agenda.service.AgendaService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

/**
 * REST controller for medical agenda management operations.
 */
@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class AgendaController {

        private final AgendaService agendaService;

        @PreAuthorize("hasRole('RECEPTIONIST')")
        @PostMapping("/{doctorId}/agenda-blocks")
        public ResponseEntity<CreateAgendaBlocksResponse> createAgendaBlocks(
                @PathVariable Long doctorId,
                @Valid @RequestBody CreateAgendaBlocksRequest request
        ) {

                CreateAgendaBlocksResponse response = agendaService.createAgendaBlocks(
                                doctorId,
                                request);

                return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(response);
        }

        @PreAuthorize("hasAnyRole('PATIENT', 'RECEPTIONIST')")
        @GetMapping("/{doctorId}/agenda-blocks")
        public ResponseEntity<List<AgendaBlockResponse>> findAgendaBlocks(
                @PathVariable Long doctorId,
                @RequestParam
                @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                LocalDate appointmentDate
        ) {

                List<AgendaBlockResponse> response = agendaService.findAgendaBlocks(
                                doctorId,
                                appointmentDate);

                return ResponseEntity.ok(response);
        }
}