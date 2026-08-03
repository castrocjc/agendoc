package com.agendoc.modules.appointment.service;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.common.exception.BadRequestException;
import com.agendoc.common.exception.ConflictException;
import com.agendoc.common.exception.ResourceNotFoundException;
import com.agendoc.modules.agenda.entity.AgendaBlockEntity;
import com.agendoc.modules.agenda.repository.AgendaBlockRepository;
import com.agendoc.modules.appointment.dto.AppointmentResponse;
import com.agendoc.modules.appointment.dto.CreateAppointmentRequest;
import com.agendoc.modules.appointment.dto.AppointmentAgendaResponse;
import com.agendoc.modules.appointment.dto.CancelAppointmentRequest;
import com.agendoc.modules.appointment.dto.RescheduleAppointmentRequest;
import com.agendoc.modules.appointment.dto.RegisterAppointmentNoShowRequest;
import com.agendoc.modules.appointment.entity.AppointmentEntity;
import com.agendoc.modules.appointment.entity.AppointmentStatusCode;
import com.agendoc.modules.appointment.entity.AppointmentStatusEntity;
import com.agendoc.modules.appointment.entity.AppointmentRescheduleHistoryEntity;
import com.agendoc.modules.appointment.repository.AppointmentRepository;
import com.agendoc.modules.appointment.repository.AppointmentStatusRepository;
import com.agendoc.modules.appointment.repository.AppointmentRescheduleHistoryRepository;
import com.agendoc.modules.appointment.authorization.AppointmentAuthorizationPolicy;
import com.agendoc.security.authorization.AuthenticatedUserAuthorization;
import com.agendoc.security.authorization.SecurityRoleCode;
import com.agendoc.security.context.AuthenticatedUserContext;
import com.agendoc.modules.clinic.entity.ClinicEntity;
import com.agendoc.modules.clinic.repository.ClinicRepository;
import com.agendoc.modules.doctor.entity.DoctorEntity;
import com.agendoc.modules.doctor.repository.DoctorRepository;
import com.agendoc.modules.patient.entity.PatientEntity;
import com.agendoc.modules.patient.repository.PatientRepository;
import com.agendoc.modules.doctor.entity.MedicalSpecialtyEntity;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
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

        private static final String APPOINTMENT_NOT_AVAILABLE = "La cita seleccionada no está disponible.";

        private static final String CANCELLATION_STATUS_NOT_AVAILABLE = "El estado de cancelación de la cita no está disponible.";

        private static final String CONFIRMATION_STATUS_NOT_AVAILABLE = "El estado de confirmación de la cita no está disponible.";

        private static final String NO_SHOW_STATUS_NOT_AVAILABLE = "El estado de inasistencia de la cita no está disponible.";

        private static final String APPOINTMENT_CANNOT_BE_CANCELLED = "La cita no puede ser cancelada en su estado actual.";

        private static final String APPOINTMENT_CANNOT_BE_CONFIRMED = "La llegada del paciente no puede ser confirmada en el estado actual de la cita.";

        private static final String APPOINTMENT_CANNOT_BE_MARKED_AS_NO_SHOW = "La inasistencia no puede registrarse en el estado actual de la cita.";

        private static final String APPOINTMENT_HAS_NOT_STARTED = "La cita solo puede marcarse como no asistida cuando haya comenzado su horario.";

        private static final String CONFIRMED_CANCELLATION_REASON_REQUIRED = "El motivo de cancelación es obligatorio para una cita confirmada.";

        private static final String APPOINTMENT_CANNOT_BE_RESCHEDULED = "La cita no puede ser reprogramada en su estado actual.";

        private static final String SAME_AGENDA_BLOCK = "El nuevo bloque de agenda debe ser diferente al bloque actual.";

        private static final String APPOINTMENT_STATUS_NOT_AVAILABLE = "El estado de la cita no está disponible.";

        private final AppointmentRepository appointmentRepository;
        private final AppointmentRescheduleHistoryRepository appointmentRescheduleHistoryRepository;
        private final AppointmentStatusRepository appointmentStatusRepository;
        private final AgendaBlockRepository agendaBlockRepository;
        private final PatientRepository patientRepository;
        private final DoctorRepository doctorRepository;
        private final ClinicRepository clinicRepository;
        private final AuthenticatedUserAuthorization authenticatedUserAuthorization;
        private final AppointmentAuthorizationPolicy appointmentAuthorizationPolicy;

        @Override
        @Transactional
        public AppointmentResponse createAppointment(
                CreateAppointmentRequest request) {

                AuthenticatedUserContext context =
                        authenticatedUserAuthorization.requireRole(
                                SecurityRoleCode.RECEPTIONIST
                        );

                ClinicEntity clinic =
                        findActiveClinic(context.clinicId());

                PatientEntity patient =
                        findActivePatient(
                                request.patientId(),
                                clinic
                        );

                DoctorEntity doctor =
                        findActiveDoctor(
                                request.doctorId(),
                                clinic
                        );

                AgendaBlockEntity agendaBlock =
                        findAndLockActiveAgendaBlock(
                                request.agendaBlockId()
                        );

                validateAgendaBlock(
                        agendaBlock,
                        doctor,
                        clinic
                );

                validatePatientScheduleConflict(
                        patient,
                        agendaBlock
                );

                AppointmentStatusEntity initialStatus =
                        findInitialAppointmentStatus();

                AppointmentEntity appointment =
                        createEntity(
                                request,
                                clinic,
                                patient,
                                doctor,
                                agendaBlock,
                                initialStatus
                        );

                agendaBlock.setAvailable(false);

                AppointmentEntity savedAppointment =
                        appointmentRepository.save(appointment);

                return toResponse(savedAppointment);
        }

        @Override
        @Transactional
        public AppointmentResponse cancelAppointment(
                Long appointmentId,
                CancelAppointmentRequest request) {

                AuthenticatedUserContext context =
                        authenticatedUserAuthorization.requireAnyRole(
                                SecurityRoleCode.RECEPTIONIST,
                                SecurityRoleCode.PATIENT
                        );

                SecurityRoleCode currentRole =
                        SecurityRoleCode.from(context.roleCode());

                AppointmentEntity appointment =
                        findAppointmentForCancellation(
                                appointmentId,
                                context,
                                currentRole
                        );

                appointmentAuthorizationPolicy.requireSameClinic(
                        context,
                        appointment
                );

                if (currentRole == SecurityRoleCode.PATIENT) {
                        appointmentAuthorizationPolicy.requirePatientOwnership(
                                context,
                                appointment
                        );

                        validatePatientCancellation(appointment);
                } else {
                        validateCancellation(
                                appointment,
                                request
                        );
                }

                AppointmentStatusEntity cancelledStatus =
                        findAppointmentStatus(
                                AppointmentStatusCode.CANCELADA
                        );

                appointment.setStatus(cancelledStatus);
                appointment.setCancellationReason(
                        normalizeOptional(request.reason())
                );
                appointment.setCancelledAt(LocalDateTime.now());
                appointment.getAgendaBlock().setAvailable(true);

                AppointmentEntity savedAppointment =
                        appointmentRepository.save(appointment);

                return toResponse(savedAppointment);
        }

        @Override
        @Transactional
        public AppointmentResponse confirmArrival(
                Long appointmentId) {

                AuthenticatedUserContext context =
                        authenticatedUserAuthorization.requireRole(
                                SecurityRoleCode.RECEPTIONIST
                        );

                AppointmentEntity appointment = appointmentRepository
                        .findByIdAndClinicIdAndRecordStatusForUpdate(
                                appointmentId,
                                context.clinicId(),
                                RecordStatus.ACTIVE
                        )
                        .orElseThrow(() -> new ResourceNotFoundException(
                                APPOINTMENT_NOT_AVAILABLE
                        ));

                appointmentAuthorizationPolicy.requireSameClinic(
                        context,
                        appointment
                );

                validateArrivalConfirmationStatus(
                        appointment
                );

                AppointmentStatusEntity confirmedStatus =
                        findAppointmentStatus(
                                AppointmentStatusCode.CONFIRMADA
                        );

                appointment.setStatus(confirmedStatus);
                appointment.setConfirmedAt(OffsetDateTime.now());
                appointment.setConfirmedBy(context.username());

                AppointmentEntity savedAppointment =
                        appointmentRepository.save(appointment);

                return toResponse(savedAppointment);
        }

        @Override
        @Transactional
        public AppointmentResponse registerNoShow(
                Long appointmentId,
                RegisterAppointmentNoShowRequest request) {

                AuthenticatedUserContext context =
                        authenticatedUserAuthorization.requireRole(
                                SecurityRoleCode.RECEPTIONIST
                        );

                AppointmentEntity appointment = appointmentRepository
                        .findByIdAndClinicIdAndRecordStatusForUpdate(
                                appointmentId,
                                context.clinicId(),
                                RecordStatus.ACTIVE
                        )
                        .orElseThrow(() -> new ResourceNotFoundException(
                                APPOINTMENT_NOT_AVAILABLE
                        ));

                appointmentAuthorizationPolicy.requireSameClinic(
                        context,
                        appointment
                );

                validateNoShowRegistration(appointment);

                AppointmentStatusEntity noShowStatus =
                        findAppointmentStatus(
                                AppointmentStatusCode.NO_ASISTIO
                        );

                appointment.setStatus(noShowStatus);
                appointment.setNoShowAt(OffsetDateTime.now());
                appointment.setNoShowBy(context.username());
                appointment.setNoShowComment(
                        normalizeOptional(request.comment())
                );

                AppointmentEntity savedAppointment =
                        appointmentRepository.save(appointment);

                return toResponse(savedAppointment);
        }

        @Override
        @Transactional
        public AppointmentResponse rescheduleAppointment(
                Long appointmentId,
                RescheduleAppointmentRequest request) {

                AuthenticatedUserContext context =
                        authenticatedUserAuthorization.requireRole(
                                SecurityRoleCode.RECEPTIONIST
                        );

                AppointmentEntity appointment = appointmentRepository
                        .findByIdAndClinicIdAndRecordStatusForUpdate(
                                appointmentId,
                                context.clinicId(),
                                RecordStatus.ACTIVE
                        )
                        .orElseThrow(() -> new ResourceNotFoundException(
                                APPOINTMENT_NOT_AVAILABLE
                        ));

                appointmentAuthorizationPolicy.requireSameClinic(
                        context,
                        appointment
                );

                validateReschedulingStatus(appointment);

                AgendaBlockEntity previousAgendaBlock =
                        appointment.getAgendaBlock();

                AgendaBlockEntity newAgendaBlock =
                        findAndLockActiveAgendaBlock(
                                request.agendaBlockId()
                        );

                ClinicEntity clinic =
                        findActiveClinic(context.clinicId());

                validateRescheduleAgendaBlock(
                        appointment,
                        previousAgendaBlock,
                        newAgendaBlock,
                        clinic
                );

                validatePatientScheduleConflictExcludingAppointment(
                        appointment,
                        newAgendaBlock
                );

                AppointmentRescheduleHistoryEntity history =
                        createRescheduleHistory(
                                appointment,
                                previousAgendaBlock,
                                newAgendaBlock
                        );

                previousAgendaBlock.setAvailable(true);
                newAgendaBlock.setAvailable(false);

                appointment.setAgendaBlock(newAgendaBlock);

                AppointmentEntity savedAppointment =
                        appointmentRepository.save(appointment);

                appointmentRescheduleHistoryRepository.save(history);

                return toResponse(savedAppointment);
        }

        @Override
        @Transactional(readOnly = true)
        public List<AppointmentAgendaResponse> findAppointments(
                LocalDate appointmentDate,
                Long doctorId,
                String statusCode) {

                AuthenticatedUserContext context =
                        authenticatedUserAuthorization.requireRole(
                                SecurityRoleCode.RECEPTIONIST
                        );

                String normalizedStatusCode =
                        normalizeStatusCode(statusCode);

                return appointmentRepository
                        .findClinicAppointments(
                                context.clinicId(),
                                appointmentDate,
                                doctorId,
                                normalizedStatusCode,
                                RecordStatus.ACTIVE
                        )
                        .stream()
                        .map(this::toAppointmentAgendaResponse)
                        .toList();
        }

        private AppointmentEntity findAppointmentForCancellation(
                Long appointmentId,
                AuthenticatedUserContext context,
                SecurityRoleCode currentRole) {

                if (currentRole == SecurityRoleCode.PATIENT) {
                        return appointmentRepository
                                .findByIdAndClinicIdAndPatientIdAndRecordStatusForUpdate(
                                        appointmentId,
                                        context.clinicId(),
                                        context.patientId(),
                                        RecordStatus.ACTIVE
                                )
                                .orElseThrow(() -> new ResourceNotFoundException(
                                        APPOINTMENT_NOT_AVAILABLE
                                ));
                }

                return appointmentRepository
                        .findByIdAndClinicIdAndRecordStatusForUpdate(
                                appointmentId,
                                context.clinicId(),
                                RecordStatus.ACTIVE
                        )
                        .orElseThrow(() -> new ResourceNotFoundException(
                                APPOINTMENT_NOT_AVAILABLE
                        ));
        }

        private void validatePatientCancellation(
                AppointmentEntity appointment) {

                String currentStatusCode =
                        appointment.getStatus().getCode();

                boolean scheduled =
                        AppointmentStatusCode.PROGRAMADA
                                .name()
                                .equals(currentStatusCode);

                if (!scheduled) {
                        throw new ConflictException(
                                APPOINTMENT_CANNOT_BE_CANCELLED
                        );
                }
        }

        private void validateCancellation(
                        AppointmentEntity appointment,
                        CancelAppointmentRequest request) {
                String currentStatusCode = appointment.getStatus().getCode();

                boolean scheduled = AppointmentStatusCode.PROGRAMADA
                                .name()
                                .equals(currentStatusCode);

                boolean confirmed = AppointmentStatusCode.CONFIRMADA
                                .name()
                                .equals(currentStatusCode);

                if (!scheduled && !confirmed) {
                        throw new ConflictException(
                                        APPOINTMENT_CANNOT_BE_CANCELLED);
                }

                if (confirmed
                                && (request.reason() == null
                                                || request.reason().isBlank())) {
                        throw new BadRequestException(
                                        CONFIRMED_CANCELLATION_REASON_REQUIRED);
                }
        }

        private void validateArrivalConfirmationStatus(
                        AppointmentEntity appointment) {

                String currentStatusCode = appointment
                                .getStatus()
                                .getCode();

                boolean scheduled = AppointmentStatusCode.PROGRAMADA
                                .name()
                                .equals(currentStatusCode);

                if (!scheduled) {
                        throw new ConflictException(
                                        APPOINTMENT_CANNOT_BE_CONFIRMED);
                }
        }

        private void validateNoShowRegistration(
                        AppointmentEntity appointment) {

                String currentStatusCode = appointment
                                .getStatus()
                                .getCode();

                boolean scheduled = AppointmentStatusCode.PROGRAMADA
                                .name()
                                .equals(currentStatusCode);

                if (!scheduled) {
                        throw new ConflictException(
                                        APPOINTMENT_CANNOT_BE_MARKED_AS_NO_SHOW);
                }

                AgendaBlockEntity agendaBlock = appointment.getAgendaBlock();

                LocalDateTime appointmentStart = LocalDateTime.of(
                                agendaBlock.getAppointmentDate(),
                                agendaBlock.getStartTime());

                if (appointmentStart.isAfter(LocalDateTime.now())) {
                        throw new BadRequestException(
                                        APPOINTMENT_HAS_NOT_STARTED);
                }
        }

        private void validateReschedulingStatus(
                        AppointmentEntity appointment) {

                String currentStatusCode = appointment.getStatus().getCode();

                boolean scheduled = AppointmentStatusCode.PROGRAMADA
                                .name()
                                .equals(currentStatusCode);

                if (!scheduled) {
                        throw new ConflictException(
                                        APPOINTMENT_CANNOT_BE_RESCHEDULED);
                }
        }

        private void validateRescheduleAgendaBlock(
                        AppointmentEntity appointment,
                        AgendaBlockEntity previousAgendaBlock,
                        AgendaBlockEntity newAgendaBlock,
                        ClinicEntity clinic) {

                validateDifferentAgendaBlock(
                                previousAgendaBlock,
                                newAgendaBlock);

                validateAgendaBlockClinic(
                                newAgendaBlock,
                                clinic);

                validateAgendaBlockDoctor(
                                newAgendaBlock,
                                appointment.getDoctor());

                validateAgendaBlockAvailability(
                                newAgendaBlock);

                validateAppointmentDateTime(
                                newAgendaBlock);
        }

        private void validateDifferentAgendaBlock(
                        AgendaBlockEntity previousAgendaBlock,
                        AgendaBlockEntity newAgendaBlock) {

                if (previousAgendaBlock
                                .getId()
                                .equals(newAgendaBlock.getId())) {

                        throw new BadRequestException(
                                        SAME_AGENDA_BLOCK);
                }
        }

        private void validatePatientScheduleConflictExcludingAppointment(
                        AppointmentEntity appointment,
                        AgendaBlockEntity newAgendaBlock) {

                boolean conflict = appointmentRepository
                                .existsPatientScheduleConflictExcludingAppointment(
                                                appointment.getId(),
                                                appointment.getPatient().getId(),
                                                newAgendaBlock.getAppointmentDate(),
                                                newAgendaBlock.getStartTime(),
                                                newAgendaBlock.getEndTime(),
                                                List.of(
                                                                AppointmentStatusCode.PROGRAMADA.name(),
                                                                AppointmentStatusCode.CONFIRMADA.name()),
                                                RecordStatus.ACTIVE);

                if (conflict) {
                        throw new ConflictException(
                                        PATIENT_ALREADY_HAS_APPOINTMENT);
                }
        }

        private AppointmentRescheduleHistoryEntity createRescheduleHistory(
                        AppointmentEntity appointment,
                        AgendaBlockEntity previousAgendaBlock,
                        AgendaBlockEntity newAgendaBlock) {

                AppointmentRescheduleHistoryEntity history = new AppointmentRescheduleHistoryEntity();

                history.setAppointment(appointment);

                history.setPreviousAgendaBlock(
                                previousAgendaBlock);

                history.setNewAgendaBlock(
                                newAgendaBlock);

                history.setPreviousAppointmentDate(
                                previousAgendaBlock.getAppointmentDate());

                history.setPreviousStartTime(
                                previousAgendaBlock.getStartTime());

                history.setPreviousEndTime(
                                previousAgendaBlock.getEndTime());

                history.setNewAppointmentDate(
                                newAgendaBlock.getAppointmentDate());

                history.setNewStartTime(
                                newAgendaBlock.getStartTime());

                history.setNewEndTime(
                                newAgendaBlock.getEndTime());

                return history;
        }

        private ClinicEntity findActiveClinic(
                Long clinicId) {

                return clinicRepository
                        .findByIdAndRecordStatus(
                                clinicId,
                                RecordStatus.ACTIVE
                        )
                        .orElseThrow(() -> new ResourceNotFoundException(
                                CLINIC_NOT_AVAILABLE
                        ));
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

                return doctorRepository
                        .findByIdAndClinicIdAndRecordStatus(
                                doctorId,
                                clinic.getId(),
                                RecordStatus.ACTIVE
                        )
                        .orElseThrow(() -> new ResourceNotFoundException(
                                DOCTOR_NOT_AVAILABLE
                        ));
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
                        AgendaBlockEntity agendaBlock) {

                boolean conflict = appointmentRepository.existsPatientScheduleConflict(
                                patient.getId(),
                                agendaBlock.getAppointmentDate(),
                                agendaBlock.getStartTime(),
                                agendaBlock.getEndTime(),
                                List.of(
                                                AppointmentStatusCode.PROGRAMADA.name(),
                                                AppointmentStatusCode.CONFIRMADA.name()),
                                RecordStatus.ACTIVE);

                if (conflict) {
                        throw new ConflictException(
                                        PATIENT_ALREADY_HAS_APPOINTMENT);
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
                return findAppointmentStatus(
                                AppointmentStatusCode.PROGRAMADA);
        }

        private AppointmentStatusEntity findAppointmentStatus(
                        AppointmentStatusCode statusCode) {

                return appointmentStatusRepository
                                .findByCodeAndRecordStatus(
                                                statusCode.name(),
                                                RecordStatus.ACTIVE)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                getAppointmentStatusNotAvailableMessage(
                                                                statusCode)));
        }

        private String getAppointmentStatusNotAvailableMessage(
                        AppointmentStatusCode statusCode) {

                return switch (statusCode) {
                        case PROGRAMADA -> INITIAL_STATUS_NOT_AVAILABLE;
                        case CONFIRMADA -> CONFIRMATION_STATUS_NOT_AVAILABLE;
                        case CANCELADA -> CANCELLATION_STATUS_NOT_AVAILABLE;
                        case NO_ASISTIO -> NO_SHOW_STATUS_NOT_AVAILABLE;
                        default -> APPOINTMENT_STATUS_NOT_AVAILABLE;
                };
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
                        AppointmentEntity appointment) {
                AgendaBlockEntity agendaBlock = appointment.getAgendaBlock();

                PatientEntity patient = appointment.getPatient();

                DoctorEntity doctor = appointment.getDoctor();

                MedicalSpecialtyEntity specialty = doctor.getSpecialty();

                AppointmentStatusEntity status = appointment.getStatus();

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
                                appointment.getReason());
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