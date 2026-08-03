package com.agendoc.modules.appointment.authorization;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.agendoc.common.exception.ResourceNotFoundException;
import com.agendoc.modules.appointment.entity.AppointmentEntity;
import com.agendoc.modules.clinic.entity.ClinicEntity;
import com.agendoc.modules.doctor.entity.DoctorEntity;
import com.agendoc.modules.patient.entity.PatientEntity;
import com.agendoc.security.authorization.SecurityRoleCode;
import com.agendoc.security.context.AuthenticatedUserContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppointmentAuthorizationPolicyTest {

    private AppointmentAuthorizationPolicy authorizationPolicy;

    @BeforeEach
    void setUp() {
        authorizationPolicy =
                new AppointmentAuthorizationPolicy();
    }

    @Test
    void shouldAuthorizeAppointmentFromSameClinic() {

        AppointmentEntity appointment =
                createAppointment(1L, 10L, 20L);

        AuthenticatedUserContext context =
                createReceptionistContext(1L);

        assertThatCode(() ->
                authorizationPolicy.requireSameClinic(
                        context,
                        appointment
                )
        ).doesNotThrowAnyException();
    }

    @Test
    void shouldRejectAppointmentFromAnotherClinic() {

        AppointmentEntity appointment =
                createAppointment(2L, 10L, 20L);

        AuthenticatedUserContext context =
                createReceptionistContext(1L);

        assertThatThrownBy(() ->
                authorizationPolicy.requireSameClinic(
                        context,
                        appointment
                )
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "La cita seleccionada no está disponible."
                );
    }

    @Test
    void shouldAuthorizeAppointmentOwnedByPatient() {

        AppointmentEntity appointment =
                createAppointment(1L, 10L, 20L);

        AuthenticatedUserContext context =
                createPatientContext(1L, 10L);

        assertThatCode(() ->
                authorizationPolicy.requirePatientOwnership(
                        context,
                        appointment
                )
        ).doesNotThrowAnyException();
    }

    @Test
    void shouldRejectAppointmentOwnedByAnotherPatient() {

        AppointmentEntity appointment =
                createAppointment(1L, 11L, 20L);

        AuthenticatedUserContext context =
                createPatientContext(1L, 10L);

        assertThatThrownBy(() ->
                authorizationPolicy.requirePatientOwnership(
                        context,
                        appointment
                )
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "La cita seleccionada no está disponible."
                );
    }

    @Test
    void shouldRejectPatientWithoutBusinessProfile() {

        AppointmentEntity appointment =
                createAppointment(1L, 10L, 20L);

        AuthenticatedUserContext context =
                new AuthenticatedUserContext(
                        100L,
                        "patient.user",
                        1L,
                        SecurityRoleCode.PATIENT.name(),
                        null,
                        null
                );

        assertThatThrownBy(() ->
                authorizationPolicy.requirePatientOwnership(
                        context,
                        appointment
                )
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "La cita seleccionada no está disponible."
                );
    }

    @Test
    void shouldAuthorizeAppointmentAssignedToDoctor() {

        AppointmentEntity appointment =
                createAppointment(1L, 10L, 20L);

        AuthenticatedUserContext context =
                createDoctorContext(1L, 20L);

        assertThatCode(() ->
                authorizationPolicy.requireAssignedDoctor(
                        context,
                        appointment
                )
        ).doesNotThrowAnyException();
    }

    @Test
    void shouldRejectAppointmentAssignedToAnotherDoctor() {

        AppointmentEntity appointment =
                createAppointment(1L, 10L, 21L);

        AuthenticatedUserContext context =
                createDoctorContext(1L, 20L);

        assertThatThrownBy(() ->
                authorizationPolicy.requireAssignedDoctor(
                        context,
                        appointment
                )
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "La cita seleccionada no está disponible."
                );
    }

    @Test
    void shouldRejectDoctorWithoutBusinessProfile() {

        AppointmentEntity appointment =
                createAppointment(1L, 10L, 20L);

        AuthenticatedUserContext context =
                new AuthenticatedUserContext(
                        100L,
                        "doctor.user",
                        1L,
                        SecurityRoleCode.DOCTOR.name(),
                        null,
                        null
                );

        assertThatThrownBy(() ->
                authorizationPolicy.requireAssignedDoctor(
                        context,
                        appointment
                )
        )
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(
                        "La cita seleccionada no está disponible."
                );
    }

    private AppointmentEntity createAppointment(
            Long clinicId,
            Long patientId,
            Long doctorId
    ) {

        ClinicEntity clinic = new ClinicEntity();
        clinic.setId(clinicId);

        PatientEntity patient = new PatientEntity();
        patient.setId(patientId);
        patient.setClinic(clinic);

        DoctorEntity doctor = new DoctorEntity();
        doctor.setId(doctorId);
        doctor.setClinic(clinic);

        AppointmentEntity appointment =
                new AppointmentEntity();

        appointment.setId(1000L);
        appointment.setClinic(clinic);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);

        return appointment;
    }

    private AuthenticatedUserContext createReceptionistContext(
            Long clinicId
    ) {
        return new AuthenticatedUserContext(
                100L,
                "receptionist.user",
                clinicId,
                SecurityRoleCode.RECEPTIONIST.name(),
                null,
                null
        );
    }

    private AuthenticatedUserContext createPatientContext(
            Long clinicId,
            Long patientId
    ) {
        return new AuthenticatedUserContext(
                101L,
                "patient.user",
                clinicId,
                SecurityRoleCode.PATIENT.name(),
                patientId,
                null
        );
    }

    private AuthenticatedUserContext createDoctorContext(
            Long clinicId,
            Long doctorId
    ) {
        return new AuthenticatedUserContext(
                102L,
                "doctor.user",
                clinicId,
                SecurityRoleCode.DOCTOR.name(),
                null,
                doctorId
        );
    }
}