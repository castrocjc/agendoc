package com.agendoc.modules.clinic.entity;

import com.agendoc.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Persistent representation of an AgenDoc clinic.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "clinics")
public class ClinicEntity extends BaseEntity {

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "address", length = 250)
    private String address;
}