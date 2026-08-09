package com.agendoc.modules.publicportal.service;

import com.agendoc.modules.publicportal.dto.FirstAppointmentRequest;
import com.agendoc.modules.publicportal.dto.FirstAppointmentResponse;
import com.agendoc.modules.publicportal.dto.PublicAgendaAvailabilityResponse;
import com.agendoc.modules.publicportal.dto.PublicClinicResponse;
import com.agendoc.modules.publicportal.dto.PublicDoctorSummaryResponse;
import com.agendoc.modules.publicportal.dto.PublicSpecialtyResponse;

import java.time.LocalDate;
import java.util.List;

/**
 * Application service responsible for the Digital Reception public experience.
 */
public interface PublicReceptionService {

    PublicClinicResponse getClinic(
            String clinicSlug
    );

    List<PublicSpecialtyResponse> getSpecialties(
            String clinicSlug
    );

    List<PublicDoctorSummaryResponse> getDoctors(
            String clinicSlug,
            Long specialtyId
    );

    List<PublicAgendaAvailabilityResponse> getAvailability(
            String clinicSlug,
            Long doctorId,
            LocalDate appointmentDate
    );

    FirstAppointmentResponse createFirstAppointment(
            String clinicSlug,
            FirstAppointmentRequest request
    );
}