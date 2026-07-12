package com.agendoc.modules.patient.repository;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.modules.patient.entity.PatientEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for patient persistence operations.
 */
public interface PatientRepository
        extends JpaRepository<PatientEntity, Long> {

    boolean existsByDocumentNumberIgnoreCase(
            String documentNumber
    );

    boolean existsByEmailIgnoreCase(
            String email
    );

    List<PatientEntity>
            findAllByClinicIdAndRecordStatusOrderByLastNameAscFirstNameAsc(
                    Long clinicId,
                    RecordStatus recordStatus
            );
}