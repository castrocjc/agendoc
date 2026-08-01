package com.agendoc.modules.appointment.service;

import com.agendoc.modules.appointment.dto.AppointmentAgendaResponse;
import com.agendoc.modules.appointment.dto.AppointmentResponse;
import com.agendoc.modules.appointment.dto.CancelAppointmentRequest;
import com.agendoc.modules.appointment.dto.CreateAppointmentRequest;
import com.agendoc.modules.appointment.dto.RescheduleAppointmentRequest;
import com.agendoc.modules.appointment.dto.RegisterAppointmentNoShowRequest;
import java.time.LocalDate;
import java.util.List;

/**
 * Defines medical appointment management use cases.
 */
public interface AppointmentService {

        AppointmentResponse createAppointment(
                        CreateAppointmentRequest request);

        AppointmentResponse cancelAppointment(
                        Long appointmentId,
                        CancelAppointmentRequest request);

        AppointmentResponse confirmArrival(
                        Long appointmentId);

        AppointmentResponse registerNoShow(
                        Long appointmentId,
                        RegisterAppointmentNoShowRequest request);

        AppointmentResponse rescheduleAppointment(
                        Long appointmentId,
                        RescheduleAppointmentRequest request);

        List<AppointmentAgendaResponse> findAppointments(
                        LocalDate appointmentDate,
                        Long doctorId,
                        String statusCode);
}