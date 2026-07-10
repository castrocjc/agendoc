package com.agendoc.modules.role.repository;

import com.agendoc.modules.role.entity.RoleEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for role persistence operations.
 */
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByCode(String code);
}