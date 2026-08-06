package com.agendoc.modules.appointment.service;

import com.agendoc.modules.appointment.dto.AppointmentAgendaResponse;
import com.agendoc.modules.appointment.dto.AppointmentResponse;
import com.agendoc.modules.appointment.dto.CancelAppointmentRequest;
import com.agendoc.modules.appointment.dto.CreateAppointmentRequest;
import com.agendoc.modules.appointment.dto.CreatePatientAppointmentRequest;
import com.agendoc.modules.appointment.dto.MedicalObservationResponse;
import com.agendoc.modules.appointment.dto.PatientAppointmentResponse;
import com.agendoc.modules.appointment.dto.RegisterAppointmentNoShowRequest;
import com.agendoc.modules.appointment.dto.RegisterMedicalObservationRequest;
import com.agendoc.modules.appointment.dto.RescheduleAppointmentRequest;

import java.time.LocalDate;
import java.util.List;

/**
 * Defines medical appointment management use cases.
 */
public interface AppointmentService {

        AppointmentResponse createAppointment(
                CreateAppointmentRequest request);

        AppointmentResponse createPatientAppointment(
                CreatePatientAppointmentRequest request);

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

        List<PatientAppointmentResponse> findPatientAppointments();

        List<AppointmentAgendaResponse> findAppointments(
                LocalDate appointmentDate,
                Long doctorId,
                String statusCode);

        List<AppointmentAgendaResponse> findDoctorAppointments(
                LocalDate appointmentDate,
                String statusCode);

        MedicalObservationResponse findMedicalObservation(
                Long appointmentId);

        MedicalObservationResponse registerMedicalObservation(
                Long appointmentId,
                RegisterMedicalObservationRequest request);

        AppointmentResponse markAppointmentAsAttended(
                Long appointmentId);
}
