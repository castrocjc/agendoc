package com.agendoc.modules.agenda.repository;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.modules.agenda.entity.MedicalAgendaEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicalAgendaRepository
        extends JpaRepository<MedicalAgendaEntity, Long> {

    Optional<MedicalAgendaEntity> findByDoctorIdAndRecordStatus(
            Long doctorId,
            RecordStatus recordStatus
    );

    Optional<MedicalAgendaEntity>
    findByDoctorIdAndClinicIdAndActiveTrueAndRecordStatus(
            Long doctorId,
            Long clinicId,
            RecordStatus recordStatus
    );
}