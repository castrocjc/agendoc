package com.agendoc.modules.appointment.entity;

import com.agendoc.common.entity.BaseEntity;
import com.agendoc.modules.agenda.entity.AgendaBlockEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Persistent trace of an appointment reschedule operation.
 *
 * Each record preserves the previous and new agenda blocks,
 * including their dates and time ranges.
 */
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "appointment_reschedule_history")
public class AppointmentRescheduleHistoryEntity extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "appointment_id",
            nullable = false
    )
    private AppointmentEntity appointment;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "previous_agenda_block_id",
            nullable = false
    )
    private AgendaBlockEntity previousAgendaBlock;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "new_agenda_block_id",
            nullable = false
    )
    private AgendaBlockEntity newAgendaBlock;

    @Column(
            name = "previous_appointment_date",
            nullable = false
    )
    private LocalDate previousAppointmentDate;

    @Column(
            name = "previous_start_time",
            nullable = false
    )
    private LocalTime previousStartTime;

    @Column(
            name = "previous_end_time",
            nullable = false
    )
    private LocalTime previousEndTime;

    @Column(
            name = "new_appointment_date",
            nullable = false
    )
    private LocalDate newAppointmentDate;

    @Column(
            name = "new_start_time",
            nullable = false
    )
    private LocalTime newStartTime;

    @Column(
            name = "new_end_time",
            nullable = false
    )
    private LocalTime newEndTime;
}