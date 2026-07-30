package com.agendoc.modules.appointment.repository;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.modules.appointment.entity.AppointmentEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for appointment persistence operations.
 */
public interface AppointmentRepository
                extends JpaRepository<AppointmentEntity, Long> {

        Optional<AppointmentEntity> findByIdAndRecordStatus(
                        Long id,
                        RecordStatus recordStatus);

        boolean existsByAgendaBlockIdAndRecordStatus(
                        Long agendaBlockId,
                        RecordStatus recordStatus);

        @Query("""
                        SELECT COUNT(a) > 0
                        FROM AppointmentEntity a
                        WHERE a.recordStatus = :recordStatus
                        AND a.patient.id = :patientId
                        AND a.status.code IN :statusCodes
                        AND a.agendaBlock.appointmentDate = :appointmentDate
                        AND a.agendaBlock.startTime < :endTime
                        AND a.agendaBlock.endTime > :startTime
                        """)
        boolean existsPatientScheduleConflict(
                        Long patientId,
                        LocalDate appointmentDate,
                        LocalTime startTime,
                        LocalTime endTime,
                        Collection<String> statusCodes,
                        RecordStatus recordStatus);
}