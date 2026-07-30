package com.agendoc.modules.appointment.service;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.common.exception.BadRequestException;
import com.agendoc.common.exception.ConflictException;
import com.agendoc.common.exception.ResourceNotFoundException;
import com.agendoc.modules.agenda.entity.AgendaBlockEntity;
import com.agendoc.modules.agenda.repository.AgendaBlockRepository;
import com.agendoc.modules.appointment.dto.AppointmentResponse;
import com.agendoc.modules.appointment.dto.CreateAppointmentRequest;
import com.agendoc.modules.appointment.entity.AppointmentEntity;
import com.agendoc.modules.appointment.entity.AppointmentStatusCode;
import com.agendoc.modules.appointment.entity.AppointmentStatusEntity;
import com.agendoc.modules.appointment.repository.AppointmentRepository;
import com.agendoc.modules.appointment.repository.AppointmentStatusRepository;
import com.agendoc.modules.clinic.entity.ClinicEntity;
import com.agendoc.modules.clinic.repository.ClinicRepository;
import com.agendoc.modules.doctor.entity.DoctorEntity;
import com.agendoc.modules.doctor.repository.DoctorRepository;
import com.agendoc.modules.patient.entity.PatientEntity;
import com.agendoc.modules.patient.repository.PatientRepository;
import com.agendoc.modules.appointment.dto.AppointmentAgendaResponse;
import com.agendoc.modules.doctor.entity.MedicalSpecialtyEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of medical appointment management use cases.
 */
@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

        private static final String CLINIC_NOT_AVAILABLE = "No existe un consultorio activo disponible.";

        private static final String PATIENT_NOT_AVAILABLE = "El paciente seleccionado no está disponible.";

        private static final String DOCTOR_NOT_AVAILABLE = "El médico seleccionado no está disponible.";

        private static final String AGENDA_BLOCK_NOT_AVAILABLE = "El bloque de agenda seleccionado no está disponible.";

        private static final String AGENDA_BLOCK_ALREADY_RESERVED = "El bloque de agenda seleccionado ya fue reservado.";

        private static final String INVALID_AGENDA_BLOCK_DOCTOR = "El bloque de agenda no corresponde al médico seleccionado.";

        private static final String PAST_APPOINTMENT = "No se puede crear una cita en una fecha u hora pasada.";

        private static final String INITIAL_STATUS_NOT_AVAILABLE = "El estado inicial de la cita no está disponible.";

        private static final String PATIENT_ALREADY_HAS_APPOINTMENT = "El paciente ya tiene una cita programada en el horario seleccionado.";

        private final AppointmentRepository appointmentRepository;
        private final AppointmentStatusRepository appointmentStatusRepository;
        private final AgendaBlockRepository agendaBlockRepository;
        private final PatientRepository patientRepository;
        private final DoctorRepository doctorRepository;
        private final ClinicRepository clinicRepository;

        @Override
        @Transactional
        public AppointmentResponse createAppointment(
                        CreateAppointmentRequest request) {
                ClinicEntity clinic = findActiveClinic();

                PatientEntity patient = findActivePatient(
                                request.patientId(),
                                clinic);

                DoctorEntity doctor = findActiveDoctor(
                                request.doctorId(),
                                clinic);

                /*
                 * The block is retrieved using a pessimistic write lock.
                 * This prevents two concurrent transactions from reserving
                 * the same agenda block.
                 */
                AgendaBlockEntity agendaBlock = findAndLockActiveAgendaBlock(
                                request.agendaBlockId());

                validateAgendaBlock(
                                agendaBlock,
                                doctor,
                                clinic);

                validatePatientScheduleConflict(
                        patient,
                        agendaBlock
                );

                AppointmentStatusEntity initialStatus = findInitialAppointmentStatus();

                AppointmentEntity appointment = createEntity(
                                request,
                                clinic,
                                patient,
                                doctor,
                                agendaBlock,
                                initialStatus);

                /*
                 * The block remains unavailable after the appointment
                 * is successfully created. The update and appointment insert
                 * are committed in the same transaction.
                 */
                agendaBlock.setAvailable(false);

                AppointmentEntity savedAppointment = appointmentRepository.save(appointment);

                return toResponse(savedAppointment);
        }

        @Override
        @Transactional(readOnly = true)
        public List<AppointmentAgendaResponse> findAppointments(
                LocalDate appointmentDate,
                Long doctorId,
                String statusCode
        ) {
        ClinicEntity clinic = findActiveClinic();

        String normalizedStatusCode =
                normalizeStatusCode(statusCode);

        return appointmentRepository
                .findClinicAppointments(
                        clinic.getId(),
                        appointmentDate,
                        doctorId,
                        normalizedStatusCode,
                        RecordStatus.ACTIVE
                )
                .stream()
                .map(this::toAppointmentAgendaResponse)
                .toList();
        }

        private ClinicEntity findActiveClinic() {
                return clinicRepository
                                .findFirstByRecordStatusOrderByIdAsc(
                                                RecordStatus.ACTIVE)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                CLINIC_NOT_AVAILABLE));
        }

        private PatientEntity findActivePatient(
                        Long patientId,
                        ClinicEntity clinic) {
                PatientEntity patient = patientRepository
                                .findByIdAndRecordStatus(
                                                patientId,
                                                RecordStatus.ACTIVE)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                PATIENT_NOT_AVAILABLE));

                if (!patient.getClinic().getId().equals(clinic.getId())) {
                        throw new ResourceNotFoundException(
                                        PATIENT_NOT_AVAILABLE);
                }

                return patient;
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

        private AgendaBlockEntity findAndLockActiveAgendaBlock(
                        Long agendaBlockId) {
                return agendaBlockRepository
                                .findByIdAndRecordStatusForUpdate(
                                                agendaBlockId,
                                                RecordStatus.ACTIVE)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                AGENDA_BLOCK_NOT_AVAILABLE));
        }

        private void validateAgendaBlock(
                        AgendaBlockEntity agendaBlock,
                        DoctorEntity doctor,
                        ClinicEntity clinic) {
                validateAgendaBlockClinic(
                                agendaBlock,
                                clinic);

                validateAgendaBlockDoctor(
                                agendaBlock,
                                doctor);

                validateAgendaBlockAvailability(agendaBlock);

                validateAppointmentDateTime(agendaBlock);
        }

        private void validatePatientScheduleConflict(
                PatientEntity patient,
                AgendaBlockEntity agendaBlock
        ) {

        boolean conflict =
                appointmentRepository.existsPatientScheduleConflict(
                        patient.getId(),
                        agendaBlock.getAppointmentDate(),
                        agendaBlock.getStartTime(),
                        agendaBlock.getEndTime(),
                        List.of(
                                AppointmentStatusCode.PROGRAMADA.name(),
                                AppointmentStatusCode.CONFIRMADA.name()
                        ),
                        RecordStatus.ACTIVE
                );

        if (conflict) {
                throw new ConflictException(
                        PATIENT_ALREADY_HAS_APPOINTMENT
                );
        }
        }

        private void validateAgendaBlockClinic(
                        AgendaBlockEntity agendaBlock,
                        ClinicEntity clinic) {
                Long agendaClinicId = agendaBlock
                                .getMedicalAgenda()
                                .getClinic()
                                .getId();

                if (!agendaClinicId.equals(clinic.getId())) {
                        throw new ResourceNotFoundException(
                                        AGENDA_BLOCK_NOT_AVAILABLE);
                }
        }

        private void validateAgendaBlockDoctor(
                        AgendaBlockEntity agendaBlock,
                        DoctorEntity doctor) {
                Long agendaDoctorId = agendaBlock
                                .getMedicalAgenda()
                                .getDoctor()
                                .getId();

                if (!agendaDoctorId.equals(doctor.getId())) {
                        throw new BadRequestException(
                                        INVALID_AGENDA_BLOCK_DOCTOR);
                }
        }

        private void validateAgendaBlockAvailability(
                        AgendaBlockEntity agendaBlock) {
                if (!Boolean.TRUE.equals(agendaBlock.getAvailable())) {
                        throw new ConflictException(
                                        AGENDA_BLOCK_ALREADY_RESERVED);
                }
        }

        private void validateAppointmentDateTime(
                        AgendaBlockEntity agendaBlock) {
                LocalDateTime appointmentStart = LocalDateTime.of(
                                agendaBlock.getAppointmentDate(),
                                agendaBlock.getStartTime());

                if (!appointmentStart.isAfter(LocalDateTime.now())) {
                        throw new BadRequestException(
                                        PAST_APPOINTMENT);
                }
        }

        private AppointmentStatusEntity findInitialAppointmentStatus() {
                return appointmentStatusRepository
                                .findByCodeAndRecordStatus(
                                                AppointmentStatusCode.PROGRAMADA.name(),
                                                RecordStatus.ACTIVE)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                INITIAL_STATUS_NOT_AVAILABLE));
        }

        private AppointmentEntity createEntity(
                        CreateAppointmentRequest request,
                        ClinicEntity clinic,
                        PatientEntity patient,
                        DoctorEntity doctor,
                        AgendaBlockEntity agendaBlock,
                        AppointmentStatusEntity status) {
                AppointmentEntity appointment = new AppointmentEntity();

                appointment.setClinic(clinic);
                appointment.setPatient(patient);
                appointment.setDoctor(doctor);
                appointment.setAgendaBlock(agendaBlock);
                appointment.setStatus(status);
                appointment.setReason(
                                normalizeOptional(request.reason()));
                appointment.setNotes(
                                normalizeOptional(request.notes()));

                return appointment;
        }

        private String normalizeStatusCode(String statusCode) {
        if (statusCode == null || statusCode.isBlank()) {
                return null;
        }

        return statusCode
                .trim()
                .toUpperCase(Locale.ROOT);
        }

        private String normalizeOptional(String value) {
                if (value == null || value.isBlank()) {
                        return null;
                }

                return value.trim();
        }

        private AppointmentAgendaResponse toAppointmentAgendaResponse(
                AppointmentEntity appointment
        ) {
        AgendaBlockEntity agendaBlock =
                appointment.getAgendaBlock();

        PatientEntity patient =
                appointment.getPatient();

        DoctorEntity doctor =
                appointment.getDoctor();

        MedicalSpecialtyEntity specialty =
                doctor.getSpecialty();

        AppointmentStatusEntity status =
                appointment.getStatus();

        return new AppointmentAgendaResponse(
                appointment.getId(),
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                specialty.getId(),
                specialty.getName(),
                agendaBlock.getId(),
                agendaBlock.getAppointmentDate(),
                agendaBlock.getStartTime(),
                agendaBlock.getEndTime(),
                status.getCode(),
                status.getName(),
                appointment.getReason()
        );
        }

        private AppointmentResponse toResponse(
                        AppointmentEntity appointment) {
                AgendaBlockEntity agendaBlock = appointment.getAgendaBlock();

                PatientEntity patient = appointment.getPatient();

                DoctorEntity doctor = appointment.getDoctor();

                AppointmentStatusEntity status = appointment.getStatus();

                return new AppointmentResponse(
                                appointment.getId(),
                                appointment.getClinic().getId(),
                                patient.getId(),
                                patient.getFirstName(),
                                patient.getLastName(),
                                doctor.getId(),
                                doctor.getFirstName(),
                                doctor.getLastName(),
                                agendaBlock.getId(),
                                agendaBlock.getAppointmentDate(),
                                agendaBlock.getStartTime(),
                                agendaBlock.getEndTime(),
                                status.getCode(),
                                status.getName(),
                                appointment.getReason(),
                                appointment.getNotes(),
                                appointment.getRecordStatus().name());
        }
}