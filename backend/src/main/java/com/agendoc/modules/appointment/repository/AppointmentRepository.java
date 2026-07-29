package com.agendoc.modules.appointment.repository;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.modules.appointment.entity.AppointmentEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for appointment persistence operations.
 */
public interface AppointmentRepository
        extends JpaRepository<AppointmentEntity, Long> {

    Optional<AppointmentEntity> findByIdAndRecordStatus(
            Long id,
            RecordStatus recordStatus
    );

    boolean existsByAgendaBlockIdAndRecordStatus(
            Long agendaBlockId,
            RecordStatus recordStatus
    );
}