package com.agendoc.modules.doctor.service;

import java.util.List;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.common.exception.ConflictException;
import com.agendoc.common.exception.ResourceNotFoundException;
import com.agendoc.modules.clinic.entity.ClinicEntity;
import com.agendoc.modules.clinic.repository.ClinicRepository;
import com.agendoc.modules.doctor.dto.CreateDoctorRequest;
import com.agendoc.modules.doctor.dto.DoctorResponse;
import com.agendoc.modules.doctor.entity.DoctorEntity;
import com.agendoc.modules.doctor.entity.MedicalSpecialtyEntity;
import com.agendoc.modules.doctor.repository.DoctorRepository;
import com.agendoc.modules.doctor.repository.MedicalSpecialtyRepository;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of doctor management use cases.
 */
@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

        private static final String CLINIC_NOT_AVAILABLE = "No existe un consultorio activo disponible.";

        private static final String SPECIALTY_NOT_AVAILABLE = "La especialidad seleccionada no está disponible.";

        private static final String DUPLICATED_DOCUMENT = "Ya existe un médico registrado con ese número de documento.";

        private static final String DUPLICATED_LICENSE = "Ya existe un médico registrado con ese número de colegiatura.";

        private static final String DUPLICATED_EMAIL = "Ya existe un médico registrado con ese correo.";

        private final DoctorRepository doctorRepository;
        private final MedicalSpecialtyRepository medicalSpecialtyRepository;
        private final ClinicRepository clinicRepository;

        @Override
        @Transactional
        public DoctorResponse createDoctor(CreateDoctorRequest request) {

                NormalizedDoctorData data = normalize(request);

                validateDuplicates(data);

                ClinicEntity clinic = findActiveClinic();

                MedicalSpecialtyEntity specialty = findActiveSpecialty(data.specialtyId());

                DoctorEntity doctor = createEntity(
                                data,
                                clinic,
                                specialty);

                DoctorEntity savedDoctor = doctorRepository.save(doctor);

                return toResponse(savedDoctor);
        }

        @Override
        @Transactional(readOnly = true)
        public List<DoctorResponse> findDoctors() {

                ClinicEntity clinic = findActiveClinic();

                return doctorRepository
                                .findAllByClinicIdAndRecordStatusOrderByLastNameAscFirstNameAsc(
                                                clinic.getId(),
                                                RecordStatus.ACTIVE)
                                .stream()
                                .map(this::toResponse)
                                .toList();
        }

        private NormalizedDoctorData normalize(
                        CreateDoctorRequest request) {
                return new NormalizedDoctorData(
                                request.firstName().trim(),
                                request.lastName().trim(),
                                request.documentType()
                                                .trim()
                                                .toUpperCase(Locale.ROOT),
                                request.documentNumber()
                                                .trim()
                                                .toUpperCase(Locale.ROOT),
                                request.medicalLicenseNumber()
                                                .trim()
                                                .toUpperCase(Locale.ROOT),
                                request.specialtyId(),
                                normalizeOptional(request.phone()),
                                request.email()
                                                .trim()
                                                .toLowerCase(Locale.ROOT));
        }

        private String normalizeOptional(String value) {
                if (value == null || value.isBlank()) {
                        return null;
                }

                return value.trim();
        }

        private void validateDuplicates(
                        NormalizedDoctorData data) {
                if (doctorRepository.existsByDocumentNumberIgnoreCase(
                                data.documentNumber())) {
                        throw new ConflictException(DUPLICATED_DOCUMENT);
                }

                if (doctorRepository.existsByMedicalLicenseNumberIgnoreCase(
                                data.medicalLicenseNumber())) {
                        throw new ConflictException(DUPLICATED_LICENSE);
                }

                if (doctorRepository.existsByEmailIgnoreCase(data.email())) {
                        throw new ConflictException(DUPLICATED_EMAIL);
                }
        }

        private ClinicEntity findActiveClinic() {
                return clinicRepository
                                .findFirstByRecordStatusOrderByIdAsc(
                                                RecordStatus.ACTIVE)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                CLINIC_NOT_AVAILABLE));
        }

        private MedicalSpecialtyEntity findActiveSpecialty(
                        Long specialtyId) {
                return medicalSpecialtyRepository
                                .findByIdAndRecordStatus(
                                                specialtyId,
                                                RecordStatus.ACTIVE)
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                SPECIALTY_NOT_AVAILABLE));
        }

        private DoctorEntity createEntity(
                        NormalizedDoctorData data,
                        ClinicEntity clinic,
                        MedicalSpecialtyEntity specialty) {
                DoctorEntity doctor = new DoctorEntity();

                doctor.setClinic(clinic);
                doctor.setUser(null);
                doctor.setSpecialty(specialty);
                doctor.setFirstName(data.firstName());
                doctor.setLastName(data.lastName());
                doctor.setDocumentType(data.documentType());
                doctor.setDocumentNumber(data.documentNumber());
                doctor.setMedicalLicenseNumber(
                                data.medicalLicenseNumber());
                doctor.setPhone(data.phone());
                doctor.setEmail(data.email());

                return doctor;
        }

        private DoctorResponse toResponse(DoctorEntity doctor) {
                return new DoctorResponse(
                                doctor.getId(),
                                doctor.getClinic().getId(),
                                doctor.getSpecialty().getId(),
                                doctor.getSpecialty().getName(),
                                doctor.getFirstName(),
                                doctor.getLastName(),
                                doctor.getDocumentType(),
                                doctor.getDocumentNumber(),
                                doctor.getMedicalLicenseNumber(),
                                doctor.getPhone(),
                                doctor.getEmail(),
                                doctor.getRecordStatus().name());
        }

        /**
         * Internal normalized representation used during doctor registration.
         */
        private record NormalizedDoctorData(
                        String firstName,
                        String lastName,
                        String documentType,
                        String documentNumber,
                        String medicalLicenseNumber,
                        Long specialtyId,
                        String phone,
                        String email) {
        }
}