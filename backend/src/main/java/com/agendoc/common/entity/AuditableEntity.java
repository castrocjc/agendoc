package com.agendoc.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

/**
 * Base class for entities that require logical status and audit fields.
 */
@Getter
@Setter
@MappedSuperclass
public abstract class AuditableEntity {

    private static final String SYSTEM_USER = "SYSTEM";

    @Enumerated(EnumType.STRING)
    @Column(name = "record_status", nullable = false, length = 20)
    private RecordStatus recordStatus = RecordStatus.ACTIVE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "created_by", nullable = false, updatable = false, length = 100)
    private String createdBy;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @PrePersist
    protected void onCreate() {
        Instant now = Instant.now();

        if (recordStatus == null) {
            recordStatus = RecordStatus.ACTIVE;
        }

        if (createdAt == null) {
            createdAt = now;
        }

        if (createdBy == null || createdBy.isBlank()) {
            createdBy = SYSTEM_USER;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();

        if (updatedBy == null || updatedBy.isBlank()) {
            updatedBy = SYSTEM_USER;
        }
    }
}