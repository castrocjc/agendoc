package com.agendoc.modules.appointment.repository;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.modules.appointment.entity.AppointmentEntity;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository for appointment persistence operations.
 */
public interface AppointmentRepository
                extends JpaRepository<AppointmentEntity, Long> {

        Optional<AppointmentEntity> findByIdAndRecordStatus(
                        Long id,
                        RecordStatus recordStatus);

        /**
         * Retrieves an active appointment using a pessimistic write lock.
         *
         * The lock prevents concurrent transactions from modifying the same
         * appointment while a state transition is being processed.
         */

        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("""
                SELECT a
                FROM AppointmentEntity a
                WHERE a.id = :id
                AND a.clinic.id = :clinicId
                AND a.recordStatus = :recordStatus
                """)
        Optional<AppointmentEntity> findByIdAndClinicIdAndRecordStatusForUpdate(
                Long id,
                Long clinicId,
                RecordStatus recordStatus
        );

        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("""
                SELECT a
                FROM AppointmentEntity a
                WHERE a.id = :id
                AND a.clinic.id = :clinicId
                AND a.patient.id = :patientId
                AND a.recordStatus = :recordStatus
                """)
        Optional<AppointmentEntity> findByIdAndClinicIdAndPatientIdAndRecordStatusForUpdate(
                Long id,
                Long clinicId,
                Long patientId,
                RecordStatus recordStatus
        );

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

        @Query("""
                        SELECT DISTINCT a
                        FROM AppointmentEntity a
                        JOIN FETCH a.patient patient
                        JOIN FETCH a.doctor doctor
                        JOIN FETCH doctor.specialty specialty
                        JOIN FETCH a.agendaBlock agendaBlock
                        JOIN FETCH a.status appointmentStatus
                        WHERE a.clinic.id = :clinicId
                        AND a.recordStatus = :recordStatus
                        AND agendaBlock.appointmentDate = :appointmentDate
                        AND (:doctorId IS NULL OR doctor.id = :doctorId)
                        AND (:statusCode IS NULL OR appointmentStatus.code = :statusCode)
                        ORDER BY
                            agendaBlock.startTime ASC,
                            doctor.lastName ASC,
                            doctor.firstName ASC
                        """)
        List<AppointmentEntity> findClinicAppointments(
                        Long clinicId,
                        LocalDate appointmentDate,
                        Long doctorId,
                        String statusCode,
                        RecordStatus recordStatus);

        @Query("""
                SELECT COUNT(a) > 0
                FROM AppointmentEntity a
                WHERE a.recordStatus = :recordStatus
                AND a.patient.id = :patientId
                AND a.id <> :appointmentId
                AND a.status.code IN :statusCodes
                AND a.agendaBlock.appointmentDate = :appointmentDate
                AND a.agendaBlock.startTime < :endTime
                AND a.agendaBlock.endTime > :startTime
                """)
        boolean existsPatientScheduleConflictExcludingAppointment(
                Long appointmentId,
                Long patientId,
                LocalDate appointmentDate,
                LocalTime startTime,
                LocalTime endTime,
                Collection<String> statusCodes,
                RecordStatus recordStatus
        );

        @Query("""
                SELECT COUNT(appointment) > 0
                FROM AppointmentEntity appointment
                WHERE appointment.agendaBlock.id = :agendaBlockId
                AND appointment.recordStatus = :recordStatus
                AND appointment.status.code IN :statusCodes
                """)
        boolean existsActiveAppointmentByAgendaBlockId(
                Long agendaBlockId,
                Collection<String> statusCodes,
                RecordStatus recordStatus
        );
}