package com.agendoc.modules.appointment.entity;

import com.agendoc.common.entity.BaseEntity;
import com.agendoc.modules.agenda.entity.AgendaBlockEntity;
import com.agendoc.modules.clinic.entity.ClinicEntity;
import com.agendoc.modules.doctor.entity.DoctorEntity;
import com.agendoc.modules.patient.entity.PatientEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

/**
 * Persistent representation of a medical appointment.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "appointments")
public class AppointmentEntity extends BaseEntity {

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "clinic_id", nullable = false)
        private ClinicEntity clinic;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "patient_id", nullable = false)
        private PatientEntity patient;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "doctor_id", nullable = false)
        private DoctorEntity doctor;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "agenda_block_id", nullable = false)
        private AgendaBlockEntity agendaBlock;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "status_id", nullable = false)
        private AppointmentStatusEntity status;

        @Column(name = "reason", length = 500)
        private String reason;

        @Column(name = "notes", length = 1000)
        private String notes;

        @Column(name = "cancellation_reason", length = 500)
        private String cancellationReason;

        @Column(name = "cancelled_at")
        private LocalDateTime cancelledAt;

        @Column(name = "confirmed_at")
        private OffsetDateTime confirmedAt;

        @Column(name = "confirmed_by", length = 100)
        private String confirmedBy;

        @Column(name = "no_show_at")
        private OffsetDateTime noShowAt;

        @Column(name = "no_show_by", length = 100)
        private String noShowBy;

        @Column(name = "no_show_comment", length = 500)
        private String noShowComment;

        @Column(name = "medical_observation", length = 2000)
        private String medicalObservation;

        @Column(name = "medical_observation_recorded_at")
        private OffsetDateTime medicalObservationRecordedAt;

        @Column(name = "medical_observation_recorded_by", length = 100)
        private String medicalObservationRecordedBy;
}