package com.microservice.user.dtos.userDTO;

import jakarta.validation.constraints.*;

public record UserEntityRequestDTO(
        @Email @NotBlank(message = "El email es obligatorio")
        String email,
        @NotBlank(message = "El username es obligatorio")
        String username,
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String password
) {
}