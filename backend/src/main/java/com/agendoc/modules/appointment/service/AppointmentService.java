package com.agendoc.modules.appointment.service;

import com.agendoc.modules.appointment.dto.AppointmentResponse;
import com.agendoc.modules.appointment.dto.CreateAppointmentRequest;

/**
 * Defines medical appointment management use cases.
 */
public interface AppointmentService {

    AppointmentResponse createAppointment(
            CreateAppointmentRequest request
    );
}