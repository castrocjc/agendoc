package com.agendoc.modules.doctor.repository;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.modules.doctor.entity.MedicalSpecialtyEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for medical specialty persistence operations.
 */
public interface MedicalSpecialtyRepository
                extends JpaRepository<MedicalSpecialtyEntity, Long> {

        Optional<MedicalSpecialtyEntity> findByIdAndRecordStatus(
                        Long id,
                        RecordStatus recordStatus);

        List<MedicalSpecialtyEntity> findAllByRecordStatusOrderByNameAsc(
                        RecordStatus recordStatus);

        @Query("""
                        SELECT DISTINCT specialty
                        FROM DoctorEntity doctor
                        JOIN doctor.specialty specialty
                        WHERE doctor.clinic.id = :clinicId
                          AND doctor.recordStatus = :recordStatus
                          AND specialty.recordStatus = :recordStatus
                        ORDER BY specialty.name ASC
                        """)
        List<MedicalSpecialtyEntity> findAllAssociatedWithActiveDoctorsByClinicId(
                        @Param("clinicId") Long clinicId,
                        @Param("recordStatus") RecordStatus recordStatus);
}