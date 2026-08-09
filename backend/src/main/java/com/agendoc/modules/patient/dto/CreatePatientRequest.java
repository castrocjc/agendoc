package com.agendoc.modules.patient.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * Request contract for registering a patient.
 */
public record CreatePatientRequest(

        @NotBlank(message = "Los nombres son obligatorios.")
        @Size(max = 100, message = "Los nombres no pueden superar 100 caracteres.")
        String firstName,

        @NotBlank(message = "Los apellidos son obligatorios.")
        @Size(max = 100, message = "Los apellidos no pueden superar 100 caracteres.")
        String lastName,

        @NotBlank(message = "El tipo de documento es obligatorio.")
        @Size(max = 30, message = "El tipo de documento no puede superar 30 caracteres.")
        String documentType,

        @NotBlank(message = "El número de documento es obligatorio.")
        @Size(max = 50, message = "El número de documento no puede superar 50 caracteres.")
        String documentNumber,

        @NotNull(message = "La fecha de nacimiento es obligatoria.")
        @PastOrPresent(message = "La fecha de nacimiento no puede ser futura.")
        LocalDate birthDate,

        @NotBlank(message = "El teléfono es obligatorio.")
        @Size(max = 30, message = "El teléfono no puede superar 30 caracteres.")
        String phone,

        @Email(message = "El correo no tiene un formato válido.")
        @Size(max = 150, message = "El correo no puede superar 150 caracteres.")
        String email,

        @Size(max = 250, message = "La dirección no puede superar 250 caracteres.")
        String address
) {
}