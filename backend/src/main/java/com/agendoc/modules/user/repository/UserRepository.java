package com.agendoc.modules.user.repository;

import com.agendoc.modules.user.entity.UserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for user authentication and persistence operations.
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @EntityGraph(attributePaths = {"clinic", "role"})
    Optional<UserEntity> findByUsernameIgnoreCaseOrEmailIgnoreCase(
            String username,
            String email
    );

    @EntityGraph(attributePaths = {"clinic", "role"})
    Optional<UserEntity> findWithClinicAndRoleById(Long id);
}