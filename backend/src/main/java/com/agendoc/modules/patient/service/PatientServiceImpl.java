package com.agendoc.modules.patient.service;

import java.time.LocalDate;
import java.util.Locale;
import java.util.List;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.common.exception.ConflictException;
import com.agendoc.common.exception.ResourceNotFoundException;
import com.agendoc.common.exception.BadRequestException;
import com.agendoc.modules.clinic.entity.ClinicEntity;
import com.agendoc.modules.clinic.repository.ClinicRepository;
import com.agendoc.modules.patient.dto.CreatePatientRequest;
import com.agendoc.modules.patient.dto.PatientResponse;
import com.agendoc.modules.patient.entity.PatientEntity;
import com.agendoc.modules.patient.repository.PatientRepository;
import com.agendoc.security.authorization.AuthenticatedUserAuthorization;
import com.agendoc.security.authorization.SecurityRoleCode;
import com.agendoc.security.context.AuthenticatedUserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.PageRequest;

/**
 * Default implementation of patient management use cases.
 */
@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

        private static final String CLINIC_NOT_AVAILABLE = "No existe un consultorio activo disponible.";

        private static final String DUPLICATED_DOCUMENT = "Ya existe un paciente registrado con ese número de documento.";

        private static final String DUPLICATED_EMAIL = "Ya existe un paciente registrado con ese correo.";

        private final PatientRepository patientRepository;
        private final ClinicRepository clinicRepository;
        private final AuthenticatedUserAuthorization authenticatedUserAuthorization;

        private static final int MINIMUM_SEARCH_LENGTH = 2;
        private static final int MAXIMUM_SEARCH_RESULTS = 20;

        private static final String INVALID_SEARCH_TERM = "Ingresa al menos 2 caracteres para buscar pacientes.";

        @Override
        @Transactional
        public PatientResponse createPatient(
                        CreatePatientRequest request) {

                AuthenticatedUserContext context =
                                authenticatedUserAuthorization.requireRole(
                                                SecurityRoleCode.RECEPTIONIST);

                ClinicEntity clinic =
                                findActiveClinic(context.clinicId());

                NormalizedPatientData data = normalize(request);

                validateDuplicates(data);

                PatientEntity patient = createEntity(
                                data,
                                clinic);

                PatientEntity savedPatient =
                                patientRepository.save(patient);

                return toResponse(savedPatient);
        }

        @Override
        @Transactional(readOnly = true)
        public List<PatientResponse> searchPatients(
                        String query) {

                AuthenticatedUserContext context =
                                authenticatedUserAuthorization.requireRole(
                                                SecurityRoleCode.RECEPTIONIST);

                String searchTerm =
                                normalizeSearchTerm(query);

                return patientRepository
                                .searchByClinicAndTerm(
                                                context.clinicId(),
                                                RecordStatus.ACTIVE,
                                                searchTerm,
                                                PageRequest.of(
                                                                0,
                                                                MAXIMUM_SEARCH_RESULTS))
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        private NormalizedPatientData normalize(
                        CreatePatientRequest request) {
                return new NormalizedPatientData(
                                request.firstName().trim(),
                                request.lastName().trim(),
                                request.documentType()
                                                .trim()
                                                .toUpperCase(Locale.ROOT),
                                request.documentNumber()
                                                .trim()
                                                .toUpperCase(Locale.ROOT),
                                request.birthDate(),
                                request.phone().trim(),
                                normalizeEmail(request.email()),
                                normalizeOptional(request.address()));
        }

        private String normalizeEmail(String value) {
                if (value == null || value.isBlank()) {
                        return null;
                }

                return value
                                .trim()
                                .toLowerCase(Locale.ROOT);
        }

        private String normalizeOptional(String value) {
                if (value == null || value.isBlank()) {
                        return null;
                }

                return value.trim();
        }

        private void validateDuplicates(
                        NormalizedPatientData data) {
                if (patientRepository.existsByDocumentNumberIgnoreCase(
                                data.documentNumber())) {
                        throw new ConflictException(DUPLICATED_DOCUMENT);
                }

                if (data.email() != null
                                && patientRepository.existsByEmailIgnoreCase(
                                                data.email())) {
                        throw new ConflictException(DUPLICATED_EMAIL);
                }
        }

        private ClinicEntity findActiveClinic(
                        Long clinicId) {

                return clinicRepository
                                .findByIdAndRecordStatus(
                                                clinicId,
                                                RecordStatus.ACTIVE)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                CLINIC_NOT_AVAILABLE));
        }

        private PatientEntity createEntity(
                        NormalizedPatientData data,
                        ClinicEntity clinic) {
                PatientEntity patient = new PatientEntity();

                patient.setClinic(clinic);
                patient.setUser(null);
                patient.setFirstName(data.firstName());
                patient.setLastName(data.lastName());
                patient.setDocumentType(data.documentType());
                patient.setDocumentNumber(data.documentNumber());
                patient.setBirthDate(data.birthDate());
                patient.setPhone(data.phone());
                patient.setEmail(data.email());
                patient.setAddress(data.address());

                return patient;
        }

        private PatientResponse toResponse(
                        PatientEntity patient) {
                return new PatientResponse(
                                patient.getId(),
                                patient.getClinic().getId(),
                                patient.getFirstName(),
                                patient.getLastName(),
                                patient.getDocumentType(),
                                patient.getDocumentNumber(),
                                patient.getBirthDate(),
                                patient.getPhone(),
                                patient.getEmail(),
                                patient.getAddress(),
                                patient.getRecordStatus().name());
        }

        /**
         * Internal normalized representation used during patient registration.
         */
        private record NormalizedPatientData(
                        String firstName,
                        String lastName,
                        String documentType,
                        String documentNumber,
                        LocalDate birthDate,
                        String phone,
                        String email,
                        String address) {
        }

        private String normalizeSearchTerm(String query) {
                if (query == null) {
                        throw new BadRequestException(INVALID_SEARCH_TERM);
                }

                String normalizedQuery = query.trim();

                if (normalizedQuery.length() < MINIMUM_SEARCH_LENGTH) {
                        throw new BadRequestException(INVALID_SEARCH_TERM);
                }

                return normalizedQuery;
        }
}