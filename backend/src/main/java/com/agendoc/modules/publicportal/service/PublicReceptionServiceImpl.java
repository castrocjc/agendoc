package com.agendoc.modules.publicportal.service;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.common.exception.BadRequestException;
import com.agendoc.common.exception.ConflictException;
import com.agendoc.common.exception.ResourceNotFoundException;
import com.agendoc.modules.agenda.entity.AgendaBlockEntity;
import com.agendoc.modules.agenda.entity.MedicalAgendaEntity;
import com.agendoc.modules.agenda.repository.MedicalAgendaRepository;
import com.agendoc.modules.agenda.repository.AgendaBlockRepository;
import com.agendoc.modules.clinic.entity.ClinicEntity;
import com.agendoc.modules.clinic.repository.ClinicRepository;
import com.agendoc.modules.doctor.entity.MedicalSpecialtyEntity;
import com.agendoc.modules.doctor.entity.DoctorEntity;
import com.agendoc.modules.doctor.repository.MedicalSpecialtyRepository;
import com.agendoc.modules.doctor.repository.DoctorRepository;
import com.agendoc.modules.publicportal.dto.FirstAppointmentRequest;
import com.agendoc.modules.publicportal.dto.FirstAppointmentResponse;
import com.agendoc.modules.publicportal.dto.PublicAgendaAvailabilityResponse;
import com.agendoc.modules.publicportal.dto.PublicClinicResponse;
import com.agendoc.modules.publicportal.dto.PublicDoctorSummaryResponse;
import com.agendoc.modules.publicportal.dto.PublicSpecialtyResponse;
import com.agendoc.modules.appointment.entity.AppointmentEntity;
import com.agendoc.modules.appointment.entity.AppointmentStatusCode;
import com.agendoc.modules.appointment.entity.AppointmentStatusEntity;
import com.agendoc.modules.appointment.repository.AppointmentRepository;
import com.agendoc.modules.appointment.repository.AppointmentStatusRepository;
import com.agendoc.modules.patient.entity.PatientEntity;
import com.agendoc.modules.patient.repository.PatientRepository;
import com.agendoc.modules.role.entity.RoleEntity;
import com.agendoc.modules.role.repository.RoleRepository;
import com.agendoc.modules.user.entity.UserEntity;
import com.agendoc.modules.user.repository.UserRepository;
import com.agendoc.security.authorization.SecurityRoleCode;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Default implementation of the Digital Reception public use cases.
 */
@Service
@RequiredArgsConstructor
public class PublicReceptionServiceImpl
                implements PublicReceptionService {

        private static final String CLINIC_NOT_AVAILABLE = "El consultorio solicitado no está disponible.";
        private static final String DOCTOR_NOT_AVAILABLE = "El médico solicitado no está disponible.";
        private static final String AGENDA_BLOCK_NOT_AVAILABLE = "El horario seleccionado no está disponible.";
        private static final String AGENDA_BLOCK_ALREADY_RESERVED = "El horario seleccionado ya fue reservado.";
        private static final String PAST_APPOINTMENT = "No se puede reservar una cita en una fecha u hora pasada.";
        private static final String PASSWORDS_DO_NOT_MATCH = "La contraseña y su confirmación no coinciden.";
        private static final String USER_ALREADY_EXISTS = "Ya existe un registro asociado a este correo en el consultorio.";
        private static final String PATIENT_ROLE_NOT_AVAILABLE = "El rol de paciente no está disponible.";

        private final ClinicRepository clinicRepository;
        private final MedicalSpecialtyRepository medicalSpecialtyRepository;
        private final DoctorRepository doctorRepository;
        private final MedicalAgendaRepository medicalAgendaRepository;
        private final AgendaBlockRepository agendaBlockRepository;
        private final UserRepository userRepository;
        private final PatientRepository patientRepository;
        private final RoleRepository roleRepository;
        private final AppointmentRepository appointmentRepository;
        private final AppointmentStatusRepository appointmentStatusRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        @Transactional(readOnly = true)
        public PublicClinicResponse getClinic(
                        String clinicSlug) {
                ClinicEntity clinic = findPublicClinic(clinicSlug);

                return toPublicClinicResponse(clinic);
        }

        @Override
        @Transactional(readOnly = true)
        public List<PublicSpecialtyResponse> getSpecialties(
                        String clinicSlug) {
                ClinicEntity clinic = findPublicClinic(clinicSlug);

                return medicalSpecialtyRepository
                                .findAllAssociatedWithActiveDoctorsByClinicId(
                                                clinic.getId(),
                                                RecordStatus.ACTIVE)
                                .stream()
                                .map(this::toPublicSpecialtyResponse)
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<PublicDoctorSummaryResponse> getDoctors(
                        String clinicSlug,
                        Long specialtyId) {
                ClinicEntity clinic = findPublicClinic(clinicSlug);

                List<DoctorEntity> doctors;

                if (specialtyId == null) {
                        doctors = doctorRepository
                                        .findAllByClinicIdAndRecordStatusOrderByLastNameAscFirstNameAsc(
                                                        clinic.getId(),
                                                        RecordStatus.ACTIVE);
                } else {
                        doctors = doctorRepository
                                        .findAllByClinicIdAndSpecialtyIdAndRecordStatusOrderByLastNameAscFirstNameAsc(
                                                        clinic.getId(),
                                                        specialtyId,
                                                        RecordStatus.ACTIVE);
                }

                return doctors
                                .stream()
                                .map(this::toPublicDoctorSummaryResponse)
                                .toList();
        }

        @Override
        @Transactional(readOnly = true)
        public List<PublicAgendaAvailabilityResponse> getAvailability(
                        String clinicSlug,
                        Long doctorId,
                        LocalDate appointmentDate) {
                ClinicEntity clinic = findPublicClinic(clinicSlug);

                if (doctorId == null || appointmentDate == null) {
                        return List.of();
                }

                DoctorEntity doctor = doctorRepository
                                .findByIdAndClinicIdAndRecordStatus(
                                                doctorId,
                                                clinic.getId(),
                                                RecordStatus.ACTIVE)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "El médico solicitado no está disponible."));

                MedicalAgendaEntity agenda = medicalAgendaRepository
                                .findByDoctorIdAndClinicIdAndActiveTrueAndRecordStatus(
                                                doctor.getId(),
                                                clinic.getId(),
                                                RecordStatus.ACTIVE)
                                .orElse(null);

                if (agenda == null) {
                        return List.of();
                }

                LocalDate today = LocalDate.now();

                if (appointmentDate.isBefore(today)) {
                        return List.of();
                }

                return agendaBlockRepository
                                .findByMedicalAgendaIdAndAppointmentDateAndAvailableTrueAndRecordStatusOrderByStartTimeAsc(
                                                agenda.getId(),
                                                appointmentDate,
                                                RecordStatus.ACTIVE)
                                .stream()
                                .filter(block -> isFutureAvailability(
                                                block,
                                                today))
                                .map(this::toPublicAgendaAvailabilityResponse)
                                .toList();
        }

        @Override
        @Transactional
        public FirstAppointmentResponse createFirstAppointment(
                        String clinicSlug,
                        FirstAppointmentRequest request) {
                ClinicEntity clinic = findPublicClinic(clinicSlug);

                NormalizedFirstAppointmentData data = normalizeFirstAppointmentRequest(request);

                validatePasswordConfirmation(data);

                validatePublicRegistrationDuplicates(
                                clinic,
                                data);

                DoctorEntity doctor = findActiveDoctor(
                                data.doctorId(),
                                clinic);

                AgendaBlockEntity agendaBlock = findAndLockPublicAgendaBlock(
                                data.agendaBlockId(),
                                doctor,
                                clinic);

                validateReservableAgendaBlock(
                                agendaBlock);

                validateNoActiveAppointmentForBlock(
                        agendaBlock
                );

                RoleEntity patientRole = findPatientRole();

                AppointmentStatusEntity initialStatus = findInitialAppointmentStatus();

                UserEntity user = createPatientUser(
                                clinic,
                                patientRole,
                                data);

                PatientEntity patient = createPublicPatient(
                                clinic,
                                user,
                                data);

                AppointmentEntity appointment = createPublicAppointment(
                                clinic,
                                patient,
                                doctor,
                                agendaBlock,
                                initialStatus,
                                data.reason());

                agendaBlock.setAvailable(false);

                AppointmentEntity savedAppointment = appointmentRepository.save(
                                appointment);

                return toFirstAppointmentResponse(
                                savedAppointment);
        }

        private ClinicEntity findPublicClinic(
                        String clinicSlug) {
                String normalizedSlug = normalizeClinicSlug(clinicSlug);

                ClinicEntity clinic = clinicRepository
                                .findBySlugAndRecordStatus(
                                                normalizedSlug,
                                                RecordStatus.ACTIVE)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                CLINIC_NOT_AVAILABLE));

                if (!clinic.isPublicPortalEnabled()) {
                        throw new ResourceNotFoundException(
                                        CLINIC_NOT_AVAILABLE);
                }

                return clinic;
        }

        private String normalizeClinicSlug(
                        String clinicSlug) {
                if (clinicSlug == null || clinicSlug.isBlank()) {
                        throw new ResourceNotFoundException(
                                        CLINIC_NOT_AVAILABLE);
                }

                return clinicSlug
                                .trim()
                                .toLowerCase(Locale.ROOT);
        }

        private PublicClinicResponse toPublicClinicResponse(
                        ClinicEntity clinic) {
                String publicName = clinic.getPublicName() == null
                                || clinic.getPublicName().isBlank()
                                                ? clinic.getName()
                                                : clinic.getPublicName().trim();

                return new PublicClinicResponse(
                                clinic.getSlug(),
                                publicName,
                                normalizeOptional(
                                                clinic.getPublicDescription()),
                                normalizeOptional(
                                                clinic.getLogoUrl()),
                                normalizeOptional(
                                                clinic.getPhone()),
                                normalizeOptional(
                                                clinic.getWhatsapp()),
                                normalizeOptional(
                                                clinic.getEmail()),
                                normalizeOptional(
                                                clinic.getAddress()),
                                normalizeOptional(
                                                clinic.getMapUrl()));
        }

        private PublicSpecialtyResponse toPublicSpecialtyResponse(
                        MedicalSpecialtyEntity specialty) {
                return new PublicSpecialtyResponse(
                                specialty.getId(),
                                specialty.getName());
        }

        private String normalizeOptional(
                        String value) {
                if (value == null || value.isBlank()) {
                        return null;
                }

                return value.trim();
        }

        private PublicDoctorSummaryResponse toPublicDoctorSummaryResponse(
                        DoctorEntity doctor) {
                MedicalSpecialtyEntity specialty = doctor.getSpecialty();

                return new PublicDoctorSummaryResponse(
                                doctor.getId(),
                                doctor.getFirstName(),
                                doctor.getLastName(),
                                specialty.getId(),
                                specialty.getName());
        }

        private boolean isFutureAvailability(
                        AgendaBlockEntity agendaBlock,
                        LocalDate today) {
                if (agendaBlock.getAppointmentDate().isAfter(today)) {
                        return true;
                }

                return agendaBlock
                                .getStartTime()
                                .isAfter(java.time.LocalTime.now());
        }

        private PublicAgendaAvailabilityResponse toPublicAgendaAvailabilityResponse(
                        AgendaBlockEntity agendaBlock) {
                return new PublicAgendaAvailabilityResponse(
                                agendaBlock.getId(),
                                agendaBlock.getAppointmentDate(),
                                agendaBlock.getStartTime(),
                                agendaBlock.getEndTime());
        }

        private DoctorEntity findActiveDoctor(
                        Long doctorId,
                        ClinicEntity clinic) {
                if (doctorId == null) {
                        throw new ResourceNotFoundException(
                                        DOCTOR_NOT_AVAILABLE);
                }

                return doctorRepository
                                .findByIdAndClinicIdAndRecordStatus(
                                                doctorId,
                                                clinic.getId(),
                                                RecordStatus.ACTIVE)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                DOCTOR_NOT_AVAILABLE));
        }

        private AgendaBlockEntity findAndLockPublicAgendaBlock(
                        Long agendaBlockId,
                        DoctorEntity doctor,
                        ClinicEntity clinic) {
                if (agendaBlockId == null) {
                        throw new ResourceNotFoundException(
                                        AGENDA_BLOCK_NOT_AVAILABLE);
                }

                return agendaBlockRepository
                                .findForUpdateByIdAndDoctorIdAndClinicId(
                                                agendaBlockId,
                                                doctor.getId(),
                                                clinic.getId(),
                                                RecordStatus.ACTIVE)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                AGENDA_BLOCK_NOT_AVAILABLE));
        }

        private void validateReservableAgendaBlock(
                        AgendaBlockEntity agendaBlock) {
                if (!Boolean.TRUE.equals(
                                agendaBlock.getAvailable())) {
                        throw new ConflictException(
                                        AGENDA_BLOCK_ALREADY_RESERVED);
                }

                LocalDateTime appointmentStart = LocalDateTime.of(
                                agendaBlock.getAppointmentDate(),
                                agendaBlock.getStartTime());

                if (!appointmentStart.isAfter(
                                LocalDateTime.now())) {
                        throw new BadRequestException(
                                        PAST_APPOINTMENT);
                }
        }

        private NormalizedFirstAppointmentData normalizeFirstAppointmentRequest(
                        FirstAppointmentRequest request) {
                return new NormalizedFirstAppointmentData(
                                request.firstName().trim(),
                                request.lastName().trim(),
                                request.email()
                                                .trim()
                                                .toLowerCase(Locale.ROOT),
                                request.phone().trim(),
                                request.password(),
                                request.passwordConfirmation(),
                                request.doctorId(),
                                request.agendaBlockId(),
                                normalizeOptional(request.reason()));
        }

        private void validatePasswordConfirmation(
                        NormalizedFirstAppointmentData data) {
                if (!data.password().equals(
                                data.passwordConfirmation())) {
                        throw new BadRequestException(
                                        PASSWORDS_DO_NOT_MATCH);
                }
        }

        private void validatePublicRegistrationDuplicates(
                        ClinicEntity clinic,
                        NormalizedFirstAppointmentData data) {
                boolean userExists = userRepository.existsByClinicIdAndEmail(
                                clinic.getId(),
                                data.email());

                boolean patientExists = patientRepository.existsByClinicIdAndEmail(
                                clinic.getId(),
                                data.email());

                if (userExists || patientExists) {
                        throw new ConflictException(
                                        USER_ALREADY_EXISTS);
                }
        }

        private record NormalizedFirstAppointmentData(
                        String firstName,
                        String lastName,
                        String email,
                        String phone,
                        String password,
                        String passwordConfirmation,
                        Long doctorId,
                        Long agendaBlockId,
                        String reason) {
        }

        private RoleEntity findPatientRole() {
                return roleRepository
                                .findByCodeAndRecordStatus(
                                                SecurityRoleCode.PATIENT.name(),
                                                RecordStatus.ACTIVE)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                PATIENT_ROLE_NOT_AVAILABLE));
        }

        private UserEntity createPatientUser(
                        ClinicEntity clinic,
                        RoleEntity patientRole,
                        NormalizedFirstAppointmentData data) {

                UserEntity user = new UserEntity();

                user.setClinic(clinic);
                user.setRole(patientRole);

                user.setUsername(
                                buildTechnicalUsername(
                                                clinic,
                                                data.email()));

                user.setEmail(
                                data.email());

                user.setPasswordHash(
                                passwordEncoder.encode(
                                                data.password()));

                user.setActive(true);

                return userRepository.save(user);
        }

        private String buildTechnicalUsername(
                        ClinicEntity clinic,
                        String normalizedEmail) {
                return clinic.getSlug()
                                + ":"
                                + normalizedEmail;
        }

        private PatientEntity createPublicPatient(
                        ClinicEntity clinic,
                        UserEntity user,
                        NormalizedFirstAppointmentData data) {
                PatientEntity patient = new PatientEntity();

                patient.setClinic(clinic);
                patient.setUser(user);
                patient.setFirstName(data.firstName());
                patient.setLastName(data.lastName());
                patient.setDocumentType(null);
                patient.setDocumentNumber(null);
                patient.setBirthDate(null);
                patient.setPhone(data.phone());
                patient.setEmail(data.email());
                patient.setAddress(null);

                return patientRepository.save(patient);
        }

        private AppointmentStatusEntity findInitialAppointmentStatus() {
                return appointmentStatusRepository
                                .findByCodeAndRecordStatus(
                                                AppointmentStatusCode.PROGRAMADA.name(),
                                                RecordStatus.ACTIVE)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "El estado inicial de la cita no está disponible."));
        }

        private AppointmentEntity createPublicAppointment(
                        ClinicEntity clinic,
                        PatientEntity patient,
                        DoctorEntity doctor,
                        AgendaBlockEntity agendaBlock,
                        AppointmentStatusEntity status,
                        String reason) {
                AppointmentEntity appointment = new AppointmentEntity();

                appointment.setClinic(clinic);
                appointment.setPatient(patient);
                appointment.setDoctor(doctor);
                appointment.setAgendaBlock(agendaBlock);
                appointment.setStatus(status);
                appointment.setReason(reason);
                appointment.setNotes(null);

                return appointment;
        }

        private FirstAppointmentResponse toFirstAppointmentResponse(
                        AppointmentEntity appointment) {
                AgendaBlockEntity block = appointment.getAgendaBlock();

                PatientEntity patient = appointment.getPatient();

                DoctorEntity doctor = appointment.getDoctor();

                MedicalSpecialtyEntity specialty = doctor.getSpecialty();

                AppointmentStatusEntity status = appointment.getStatus();

                return new FirstAppointmentResponse(
                                appointment.getId(),
                                patient.getFirstName(),
                                patient.getLastName(),
                                patient.getEmail(),
                                doctor.getId(),
                                doctor.getFirstName(),
                                doctor.getLastName(),
                                specialty.getId(),
                                specialty.getName(),
                                block.getAppointmentDate(),
                                block.getStartTime(),
                                block.getEndTime(),
                                status.getCode(),
                                status.getName());
        }

        private void validateNoActiveAppointmentForBlock(
                AgendaBlockEntity agendaBlock
        ) {
        boolean activeAppointmentExists =
                appointmentRepository
                        .existsActiveAppointmentByAgendaBlockId(
                                agendaBlock.getId(),
                                List.of(
                                        AppointmentStatusCode.PROGRAMADA.name(),
                                        AppointmentStatusCode.CONFIRMADA.name()
                                ),
                                RecordStatus.ACTIVE
                        );

        if (activeAppointmentExists) {
                throw new ConflictException(
                        AGENDA_BLOCK_ALREADY_RESERVED
                );
        }
        }
}