package com.agendoc.modules.appointment.entity;

import com.agendoc.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Persistent representation of an appointment status.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "appointment_statuses")
public class AppointmentStatusEntity extends BaseEntity {

    @Column(
            name = "code",
            nullable = false,
            unique = true,
            length = 50
    )
    private String code;

    @Column(
            name = "name",
            nullable = false,
            unique = true,
            length = 100
    )
    private String name;

    @Column(
            name = "description",
            length = 250
    )
    private String description;
}