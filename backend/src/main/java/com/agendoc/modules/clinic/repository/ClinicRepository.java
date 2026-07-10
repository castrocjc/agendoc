package com.agendoc.modules.clinic.repository;

import com.agendoc.modules.clinic.entity.ClinicEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for clinic persistence operations.
 */
public interface ClinicRepository extends JpaRepository<ClinicEntity, Long> {
}