package com.agendoc.modules.patient.repository;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.modules.patient.entity.PatientEntity;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for patient persistence operations.
 */
public interface PatientRepository
        extends JpaRepository<PatientEntity, Long> {

    boolean existsByDocumentNumberIgnoreCase(
            String documentNumber
    );

    boolean existsByEmailIgnoreCase(
            String email
    );

    List<PatientEntity>
            findAllByClinicIdAndRecordStatusOrderByLastNameAscFirstNameAsc(
                    Long clinicId,
                    RecordStatus recordStatus
            );

    /**
     * Searches active patients from a clinic by first name,
     * last name, full name or document number.
     */
    @Query("""
            SELECT patient
            FROM PatientEntity patient
            WHERE patient.clinic.id = :clinicId
              AND patient.recordStatus = :recordStatus
              AND (
                    LOWER(patient.firstName)
                        LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                 OR LOWER(patient.lastName)
                        LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                 OR LOWER(CONCAT(
                        CONCAT(patient.firstName, ' '),
                        patient.lastName
                    ))
                        LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                 OR LOWER(patient.documentNumber)
                        LIKE LOWER(CONCAT('%', :searchTerm, '%'))
              )
            ORDER BY patient.lastName ASC,
                     patient.firstName ASC
            """)
    List<PatientEntity> searchByClinicAndTerm(
            @Param("clinicId") Long clinicId,
            @Param("recordStatus") RecordStatus recordStatus,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );
}