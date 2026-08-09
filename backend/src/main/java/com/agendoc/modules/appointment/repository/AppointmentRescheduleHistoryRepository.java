package com.agendoc.modules.appointment.repository;

import com.agendoc.modules.appointment.entity.AppointmentRescheduleHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Persistence repository for appointment reschedule history.
 */
public interface AppointmentRescheduleHistoryRepository
        extends JpaRepository<AppointmentRescheduleHistoryEntity, Long> {
}