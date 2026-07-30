package com.agendoc.modules.appointment.service;

import com.agendoc.modules.appointment.dto.AppointmentAgendaResponse;
import com.agendoc.modules.appointment.dto.AppointmentResponse;
import com.agendoc.modules.appointment.dto.CreateAppointmentRequest;
import java.time.LocalDate;
import java.util.List;

/**
 * Defines medical appointment management use cases.
 */
public interface AppointmentService {

    AppointmentResponse createAppointment(
            CreateAppointmentRequest request
    );

    List<AppointmentAgendaResponse> findAppointments(
            LocalDate appointmentDate,
            Long doctorId,
            String statusCode
    );
}