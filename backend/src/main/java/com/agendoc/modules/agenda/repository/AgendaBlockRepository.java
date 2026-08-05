package com.agendoc.modules.agenda.repository;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.modules.agenda.entity.AgendaBlockEntity;
import jakarta.persistence.LockModeType;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AgendaBlockRepository
                extends JpaRepository<AgendaBlockEntity, Long> {

        List<AgendaBlockEntity> findByMedicalAgendaIdAndAppointmentDateAndRecordStatusOrderByStartTimeAsc(
                        Long medicalAgendaId,
                        LocalDate appointmentDate,
                        RecordStatus recordStatus);

        List<AgendaBlockEntity> findByMedicalAgendaIdAndAppointmentDateAndAvailableTrueAndRecordStatusOrderByStartTimeAsc(
                        Long medicalAgendaId,
                        LocalDate appointmentDate,
                        RecordStatus recordStatus);

        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("""
                        SELECT agendaBlock
                        FROM AgendaBlockEntity agendaBlock
                        JOIN FETCH agendaBlock.medicalAgenda medicalAgenda
                        JOIN FETCH medicalAgenda.doctor doctor
                        JOIN FETCH medicalAgenda.clinic clinic
                        WHERE agendaBlock.id = :agendaBlockId
                          AND agendaBlock.recordStatus = :recordStatus
                        """)
        Optional<AgendaBlockEntity> findByIdAndRecordStatusForUpdate(
                        @Param("agendaBlockId") Long agendaBlockId,
                        @Param("recordStatus") RecordStatus recordStatus);

        @Lock(LockModeType.PESSIMISTIC_WRITE)
        @Query("""
                        SELECT agendaBlock
                        FROM AgendaBlockEntity agendaBlock
                        JOIN FETCH agendaBlock.medicalAgenda medicalAgenda
                        JOIN FETCH medicalAgenda.doctor doctor
                        JOIN FETCH medicalAgenda.clinic clinic
                        WHERE agendaBlock.id = :agendaBlockId
                          AND doctor.id = :doctorId
                          AND clinic.id = :clinicId
                          AND medicalAgenda.active = true
                          AND agendaBlock.recordStatus = :recordStatus
                          AND medicalAgenda.recordStatus = :recordStatus
                          AND doctor.recordStatus = :recordStatus
                          AND clinic.recordStatus = :recordStatus
                        """)
        Optional<AgendaBlockEntity> findForUpdateByIdAndDoctorIdAndClinicId(
                        @Param("agendaBlockId") Long agendaBlockId,
                        @Param("doctorId") Long doctorId,
                        @Param("clinicId") Long clinicId,
                        @Param("recordStatus") RecordStatus recordStatus);
}