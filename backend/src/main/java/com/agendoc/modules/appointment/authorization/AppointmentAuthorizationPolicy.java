package com.agendoc.modules.appointment.authorization;

import com.agendoc.common.exception.ResourceNotFoundException;
import com.agendoc.modules.appointment.entity.AppointmentEntity;
import com.agendoc.security.context.AuthenticatedUserContext;
import org.springframework.stereotype.Component;

/**
 * Applies domain authorization rules over medical appointments.
 */
@Component
public class AppointmentAuthorizationPolicy {

    private static final String APPOINTMENT_NOT_AVAILABLE =
            "La cita seleccionada no está disponible.";

    /**
     * Validates that the appointment belongs to the authenticated clinic.
     *
     * @param context authenticated user context
     * @param appointment appointment being accessed
     */
    public void requireSameClinic(
            AuthenticatedUserContext context,
            AppointmentEntity appointment
    ) {

        Long appointmentClinicId =
                appointment.getClinic().getId();

        if (!context.clinicId().equals(appointmentClinicId)) {
            throw new ResourceNotFoundException(
                    APPOINTMENT_NOT_AVAILABLE
            );
        }
    }

    /**
     * Validates that the appointment belongs to the authenticated patient.
     *
     * @param context authenticated user context
     * @param appointment appointment being accessed
     */
    public void requirePatientOwnership(
            AuthenticatedUserContext context,
            AppointmentEntity appointment
    ) {

        Long appointmentPatientId =
                appointment.getPatient().getId();

        if (context.patientId() == null
                || !context.patientId().equals(appointmentPatientId)) {

            throw new ResourceNotFoundException(
                    APPOINTMENT_NOT_AVAILABLE
            );
        }
    }

    /**
     * Validates that the appointment is assigned to the authenticated doctor.
     *
     * @param context authenticated user context
     * @param appointment appointment being accessed
     */
    public void requireAssignedDoctor(
            AuthenticatedUserContext context,
            AppointmentEntity appointment
    ) {

        Long appointmentDoctorId =
                appointment.getDoctor().getId();

        if (context.doctorId() == null
                || !context.doctorId().equals(appointmentDoctorId)) {

            throw new ResourceNotFoundException(
                    APPOINTMENT_NOT_AVAILABLE
            );
        }
    }
}