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

    @Column(name = "slug", nullable = false, unique = true, length = 100)
    private String slug;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "public_name", length = 150)
    private String publicName;

    @Column(name = "public_description", length = 500)
    private String publicDescription;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "whatsapp", length = 30)
    private String whatsapp;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "address", length = 250)
    private String address;

    @Column(name = "map_url", length = 500)
    private String mapUrl;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "public_portal_enabled", nullable = false)
    private boolean publicPortalEnabled;
}