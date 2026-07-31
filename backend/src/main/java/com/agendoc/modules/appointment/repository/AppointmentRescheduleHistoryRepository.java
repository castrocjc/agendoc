package com.agendoc.modules.appointment.repository;

import com.agendoc.modules.appointment.entity.AppointmentRescheduleHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Persistence repository for appointment reschedule history.
 */
@Repository
public interface AppointmentRescheduleHistoryRepository
        extends JpaRepository<AppointmentRescheduleHistoryEntity, Long> {
}