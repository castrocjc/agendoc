package com.agendoc.modules.doctor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Request contract for registering a doctor.
 */
public record CreateDoctorRequest(

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

        @NotBlank(message = "El número de colegiatura es obligatorio.")
        @Size(max = 50, message = "El número de colegiatura no puede superar 50 caracteres.")
        String medicalLicenseNumber,

        @NotNull(message = "La especialidad es obligatoria.")
        Long specialtyId,

        @Size(max = 30, message = "El teléfono no puede superar 30 caracteres.")
        String phone,

        @NotBlank(message = "El correo es obligatorio.")
        @Email(message = "El correo no tiene un formato válido.")
        @Size(max = 150, message = "El correo no puede superar 150 caracteres.")
        String email
) {
}