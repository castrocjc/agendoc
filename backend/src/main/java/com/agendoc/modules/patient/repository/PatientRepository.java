package com.agendoc.modules.patient.repository;

import com.agendoc.common.entity.RecordStatus;
import com.agendoc.modules.patient.entity.PatientEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repository for patient persistence operations.
 */
public interface PatientRepository
        extends JpaRepository<PatientEntity, Long> {

        boolean existsByClinicIdAndDocumentTypeAndDocumentNumber(
                Long clinicId,
                String documentType,
                String documentNumber);

        boolean existsByClinicIdAndEmail(
                Long clinicId,
                String email);

       Optional<PatientEntity> findByClinicIdAndEmailAndRecordStatus(
                     Long clinicId,
                     String email,
                     RecordStatus recordStatus);

       Optional<PatientEntity> findByIdAndRecordStatus(
                     Long id,
                     RecordStatus recordStatus);

       Optional<PatientEntity> findByIdAndClinicIdAndRecordStatus(
                     Long id,
                     Long clinicId,
                     RecordStatus recordStatus);

       Optional<PatientEntity> findByUserIdAndClinicIdAndRecordStatus(
                     Long userId,
                     Long clinicId,
                     RecordStatus recordStatus);

       List<PatientEntity> findAllByClinicIdAndRecordStatusOrderByLastNameAscFirstNameAsc(
                     Long clinicId,
                     RecordStatus recordStatus);

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
                     Pageable pageable);
}