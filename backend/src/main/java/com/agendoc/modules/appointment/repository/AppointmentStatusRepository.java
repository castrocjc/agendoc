package com.agendoc.modules.appointment.repository;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.modules.appointment.entity.AppointmentStatusEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for appointment status persistence operations.
 */
public interface AppointmentStatusRepository
        extends JpaRepository<AppointmentStatusEntity, Long> {

    Optional<AppointmentStatusEntity> findByCodeAndRecordStatus(
            String code,
            RecordStatus recordStatus
    );
}