package com.agendoc.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Credentials required to authenticate a user.
 */
public record LoginRequest(

        @NotBlank(message = "El usuario o correo es obligatorio.")
        String identifier,

        @NotBlank(message = "La contraseña es obligatoria.")
        String password

) {
}