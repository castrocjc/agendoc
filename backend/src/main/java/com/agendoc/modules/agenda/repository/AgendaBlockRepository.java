package com.agendoc.modules.agenda.repository;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.modules.agenda.entity.AgendaBlockEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendaBlockRepository extends JpaRepository<AgendaBlockEntity, Long> {

        List<AgendaBlockEntity> findByMedicalAgendaIdAndAppointmentDateAndRecordStatusOrderByStartTimeAsc(
                        Long medicalAgendaId,
                        LocalDate appointmentDate,
                        RecordStatus recordStatus);

}