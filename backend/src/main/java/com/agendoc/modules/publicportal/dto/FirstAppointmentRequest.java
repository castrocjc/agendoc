package com.agendoc.modules.publicportal.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request used by the Digital Reception to register a new patient
 * and reserve the first medical appointment.
 */
public record FirstAppointmentRequest(

        @NotBlank
        @Size(max = 100)
        String firstName,

        @NotBlank
        @Size(max = 100)
        String lastName,

        @Email
        @NotBlank
        @Size(max = 150)
        String email,

        @NotBlank
        @Size(max = 30)
        String phone,

        @NotBlank
        @Size(min = 8, max = 100)
        String password,

        @NotBlank
        @Size(min = 8, max = 100)
        String passwordConfirmation,

        @NotNull
        Long doctorId,

        @NotNull
        Long agendaBlockId,

        @Size(max = 500)
        String reason
) {
}