package com.agendoc.modules.doctor.repository;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.modules.doctor.entity.MedicalSpecialtyEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for medical specialty persistence operations.
 */
public interface MedicalSpecialtyRepository
        extends JpaRepository<MedicalSpecialtyEntity, Long> {

    Optional<MedicalSpecialtyEntity> findByIdAndRecordStatus(
            Long id,
            RecordStatus recordStatus
    );

    List<MedicalSpecialtyEntity> findAllByRecordStatusOrderByNameAsc(
            RecordStatus recordStatus
    );
}