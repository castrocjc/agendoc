package com.agendoc.modules.doctor.entity;

import com.agendoc.common.entity.BaseEntity;
import com.agendoc.modules.clinic.entity.ClinicEntity;
import com.agendoc.modules.user.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Persistent representation of a doctor associated with a clinic.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "doctors")
public class DoctorEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "clinic_id", nullable = false)
    private ClinicEntity clinic;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "specialty_id", nullable = false)
    private MedicalSpecialtyEntity specialty;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "document_type", nullable = false, length = 30)
    private String documentType;

    @Column(name = "document_number", nullable = false, unique = true, length = 50)
    private String documentNumber;

    @Column(name = "medical_license_number", nullable = false, unique = true, length = 50)
    private String medicalLicenseNumber;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;
}