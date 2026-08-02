package com.agendoc.modules.doctor.repository;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.modules.doctor.entity.DoctorEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for doctor persistence operations.
 */
public interface DoctorRepository extends JpaRepository<DoctorEntity, Long> {

        boolean existsByDocumentNumberIgnoreCase(String documentNumber);

        boolean existsByMedicalLicenseNumberIgnoreCase(
                        String medicalLicenseNumber);

        boolean existsByEmailIgnoreCase(String email);

        Optional<DoctorEntity> findByIdAndRecordStatus(
                        Long id,
                        RecordStatus recordStatus);

        Optional<DoctorEntity> findByUserIdAndClinicIdAndRecordStatus(
                Long userId,
                Long clinicId,
                RecordStatus recordStatus
        );

        @EntityGraph(attributePaths = { "specialty" })
        List<DoctorEntity> findAllByClinicIdAndRecordStatusOrderByLastNameAscFirstNameAsc(
                        Long clinicId,
                        RecordStatus recordStatus);
}