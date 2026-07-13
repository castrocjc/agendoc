package com.agendoc.modules.agenda.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.common.exception.ResourceNotFoundException;
import com.agendoc.common.exception.BadRequestException;
import com.agendoc.common.exception.ConflictException;
import com.agendoc.modules.agenda.dto.AgendaBlockItemRequest;
import com.agendoc.modules.agenda.dto.AgendaBlockResponse;
import com.agendoc.modules.agenda.dto.CreateAgendaBlocksRequest;
import com.agendoc.modules.agenda.dto.CreateAgendaBlocksResponse;
import com.agendoc.modules.agenda.entity.AgendaBlockEntity;
import com.agendoc.modules.agenda.entity.MedicalAgendaEntity;
import com.agendoc.modules.clinic.entity.ClinicEntity;
import com.agendoc.modules.doctor.entity.DoctorEntity;
import com.agendoc.modules.agenda.repository.AgendaBlockRepository;
import com.agendoc.modules.agenda.repository.MedicalAgendaRepository;
import com.agendoc.modules.clinic.repository.ClinicRepository;
import com.agendoc.modules.doctor.repository.DoctorRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of medical agenda management use cases.
 */
@Service
@RequiredArgsConstructor
public class AgendaServiceImpl implements AgendaService {

        private static final String CLINIC_NOT_AVAILABLE = "No existe un consultorio activo disponible.";

        private static final String DOCTOR_NOT_AVAILABLE = "El médico seleccionado no está disponible.";

        private static final String INVALID_TIME_RANGE = "La hora de inicio debe ser anterior a la hora de fin.";

        private static final String PAST_AGENDA_BLOCK = "No se pueden crear bloques de agenda en una fecha u hora pasada.";

        private static final String DUPLICATED_AGENDA_BLOCK = "Existe un bloque de agenda duplicado.";

        private static final String OVERLAPPING_AGENDA_BLOCK = "El bloque de agenda se superpone con otro horario registrado.";

        private final MedicalAgendaRepository medicalAgendaRepository;
        private final AgendaBlockRepository agendaBlockRepository;
        private final DoctorRepository doctorRepository;
        private final ClinicRepository clinicRepository;

        @Override
        @Transactional
        public CreateAgendaBlocksResponse createAgendaBlocks(
                        Long doctorId,
                        CreateAgendaBlocksRequest request) {
                ClinicEntity clinic = findActiveClinic();
                DoctorEntity doctor = findActiveDoctor(doctorId, clinic);

                MedicalAgendaEntity medicalAgenda = findOrCreateMedicalAgenda(doctor, clinic);

                validateAgendaBlocks(
                                medicalAgenda,
                                request.blocks());

                List<AgendaBlockEntity> agendaBlocks = request.blocks()
                                .stream()
                                .map(block -> createBlockEntity(
                                                medicalAgenda,
                                                block))
                                .toList();

                List<AgendaBlockEntity> savedBlocks = agendaBlockRepository.saveAll(agendaBlocks);

                return new CreateAgendaBlocksResponse(
                                doctor.getId(),
                                medicalAgenda.getId(),
                                savedBlocks.stream()
                                                .map(this::toResponse)
                                                .toList());
        }

        @Override
        @Transactional(readOnly = true)
        public List<AgendaBlockResponse> findAgendaBlocks(
                        Long doctorId,
                        LocalDate appointmentDate) {
                ClinicEntity clinic = findActiveClinic();
                DoctorEntity doctor = findActiveDoctor(doctorId, clinic);

                return medicalAgendaRepository
                                .findByDoctorIdAndRecordStatus(
                                                doctor.getId(),
                                                RecordStatus.ACTIVE)
                                .map(medicalAgenda -> agendaBlockRepository
                                                .findByMedicalAgendaIdAndAppointmentDateAndRecordStatusOrderByStartTimeAsc(
                                                                medicalAgenda.getId(),
                                                                appointmentDate,
                                                                RecordStatus.ACTIVE)
                                                .stream()
                                                .map(this::toResponse)
                                                .toList())
                                .orElseGet(List::of);
        }

        private ClinicEntity findActiveClinic() {
                return clinicRepository
                                .findFirstByRecordStatusOrderByIdAsc(
                                                RecordStatus.ACTIVE)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                CLINIC_NOT_AVAILABLE));
        }

        private DoctorEntity findActiveDoctor(
                        Long doctorId,
                        ClinicEntity clinic) {
                DoctorEntity doctor = doctorRepository
                                .findByIdAndRecordStatus(
                                                doctorId,
                                                RecordStatus.ACTIVE)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                DOCTOR_NOT_AVAILABLE));

                if (!doctor.getClinic().getId().equals(clinic.getId())) {
                        throw new ResourceNotFoundException(
                                        DOCTOR_NOT_AVAILABLE);
                }

                return doctor;
        }

        private MedicalAgendaEntity findOrCreateMedicalAgenda(
                        DoctorEntity doctor,
                        ClinicEntity clinic) {
                return medicalAgendaRepository
                                .findByDoctorIdAndRecordStatus(
                                                doctor.getId(),
                                                RecordStatus.ACTIVE)
                                .orElseGet(() -> createMedicalAgenda(
                                                doctor,
                                                clinic));
        }

        private MedicalAgendaEntity createMedicalAgenda(
                        DoctorEntity doctor,
                        ClinicEntity clinic) {
                MedicalAgendaEntity medicalAgenda = new MedicalAgendaEntity();

                medicalAgenda.setClinic(clinic);
                medicalAgenda.setDoctor(doctor);
                medicalAgenda.setName(
                                "Agenda de "
                                                + doctor.getFirstName()
                                                + " "
                                                + doctor.getLastName());
                medicalAgenda.setActive(true);

                return medicalAgendaRepository.save(medicalAgenda);
        }

        private void validateAgendaBlocks(
                        MedicalAgendaEntity medicalAgenda,
                        List<AgendaBlockItemRequest> requestedBlocks) {
                LocalDateTime currentDateTime = LocalDateTime.now();

                validateIndividualBlocks(
                                requestedBlocks,
                                currentDateTime);

                validateRequestedBlocksAgainstEachOther(
                                requestedBlocks);

                validateRequestedBlocksAgainstExistingBlocks(
                                medicalAgenda,
                                requestedBlocks);
        }

        private void validateIndividualBlocks(
                        List<AgendaBlockItemRequest> requestedBlocks,
                        LocalDateTime currentDateTime) {
                for (AgendaBlockItemRequest block : requestedBlocks) {
                        if (!block.startTime().isBefore(block.endTime())) {
                                throw new BadRequestException(
                                                INVALID_TIME_RANGE);
                        }

                        LocalDateTime blockStart = LocalDateTime.of(
                                        block.appointmentDate(),
                                        block.startTime());

                        if (!blockStart.isAfter(currentDateTime)) {
                                throw new BadRequestException(
                                                PAST_AGENDA_BLOCK);
                        }
                }
        }

        private void validateRequestedBlocksAgainstEachOther(
                        List<AgendaBlockItemRequest> requestedBlocks) {
                for (int currentIndex = 0; currentIndex < requestedBlocks.size(); currentIndex++) {

                        AgendaBlockItemRequest currentBlock = requestedBlocks.get(currentIndex);

                        for (int comparedIndex = currentIndex + 1; comparedIndex < requestedBlocks
                                        .size(); comparedIndex++) {

                                AgendaBlockItemRequest comparedBlock = requestedBlocks.get(comparedIndex);

                                if (!currentBlock.appointmentDate()
                                                .equals(comparedBlock.appointmentDate())) {
                                        continue;
                                }

                                if (isSameSchedule(
                                                currentBlock.startTime(),
                                                currentBlock.endTime(),
                                                comparedBlock.startTime(),
                                                comparedBlock.endTime())) {
                                        throw new ConflictException(
                                                        DUPLICATED_AGENDA_BLOCK);
                                }

                                if (isOverlapping(
                                                currentBlock.startTime(),
                                                currentBlock.endTime(),
                                                comparedBlock.startTime(),
                                                comparedBlock.endTime())) {
                                        throw new ConflictException(
                                                        OVERLAPPING_AGENDA_BLOCK);
                                }
                        }
                }
        }

        private void validateRequestedBlocksAgainstExistingBlocks(
                        MedicalAgendaEntity medicalAgenda,
                        List<AgendaBlockItemRequest> requestedBlocks) {
                Map<LocalDate, List<AgendaBlockItemRequest>> blocksByDate = requestedBlocks.stream()
                                .collect(Collectors.groupingBy(
                                                AgendaBlockItemRequest::appointmentDate));

                for (Map.Entry<LocalDate, List<AgendaBlockItemRequest>> entry : blocksByDate.entrySet()) {

                        LocalDate appointmentDate = entry.getKey();

                        List<AgendaBlockEntity> existingBlocks = agendaBlockRepository
                                        .findByMedicalAgendaIdAndAppointmentDateAndRecordStatusOrderByStartTimeAsc(
                                                        medicalAgenda.getId(),
                                                        appointmentDate,
                                                        RecordStatus.ACTIVE);

                        validateAgainstExistingBlocks(
                                        entry.getValue(),
                                        existingBlocks);
                }
        }

        private void validateAgainstExistingBlocks(
                        List<AgendaBlockItemRequest> requestedBlocks,
                        List<AgendaBlockEntity> existingBlocks) {
                for (AgendaBlockItemRequest requestedBlock : requestedBlocks) {

                        for (AgendaBlockEntity existingBlock : existingBlocks) {

                                if (isSameSchedule(
                                                requestedBlock.startTime(),
                                                requestedBlock.endTime(),
                                                existingBlock.getStartTime(),
                                                existingBlock.getEndTime())) {
                                        throw new ConflictException(
                                                        DUPLICATED_AGENDA_BLOCK);
                                }

                                if (isOverlapping(
                                                requestedBlock.startTime(),
                                                requestedBlock.endTime(),
                                                existingBlock.getStartTime(),
                                                existingBlock.getEndTime())) {
                                        throw new ConflictException(
                                                        OVERLAPPING_AGENDA_BLOCK);
                                }
                        }
                }
        }

        private boolean isSameSchedule(
                        java.time.LocalTime firstStart,
                        java.time.LocalTime firstEnd,
                        java.time.LocalTime secondStart,
                        java.time.LocalTime secondEnd) {
                return firstStart.equals(secondStart)
                                && firstEnd.equals(secondEnd);
        }

        private boolean isOverlapping(
                        java.time.LocalTime firstStart,
                        java.time.LocalTime firstEnd,
                        java.time.LocalTime secondStart,
                        java.time.LocalTime secondEnd) {
                return firstStart.isBefore(secondEnd)
                                && firstEnd.isAfter(secondStart);
        }

        private AgendaBlockEntity createBlockEntity(
                        MedicalAgendaEntity medicalAgenda,
                        AgendaBlockItemRequest block) {
                AgendaBlockEntity agendaBlock = new AgendaBlockEntity();

                agendaBlock.setMedicalAgenda(medicalAgenda);
                agendaBlock.setAppointmentDate(
                                block.appointmentDate());
                agendaBlock.setStartTime(block.startTime());
                agendaBlock.setEndTime(block.endTime());
                agendaBlock.setAvailable(true);

                return agendaBlock;
        }

        private AgendaBlockResponse toResponse(
                        AgendaBlockEntity agendaBlock) {
                return new AgendaBlockResponse(
                                agendaBlock.getId(),
                                agendaBlock.getAppointmentDate(),
                                agendaBlock.getStartTime(),
                                agendaBlock.getEndTime(),
                                Boolean.TRUE.equals(
                                                agendaBlock.getAvailable()));
        }
}