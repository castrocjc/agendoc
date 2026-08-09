package com.agendoc.modules.clinic.repository;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.modules.clinic.entity.ClinicEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for clinic persistence operations.
 */
public interface ClinicRepository extends JpaRepository<ClinicEntity, Long> {

        Optional<ClinicEntity> findByIdAndRecordStatus(
                        Long id,
                        RecordStatus recordStatus);

        Optional<ClinicEntity> findBySlugAndRecordStatus(
                        String slug,
                        RecordStatus recordStatus);
}