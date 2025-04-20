package com.microservice.user.dtos.UserEntityDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record UserEntityRequestDTO(
        @Schema(description = "User email address", example = "user@example.com")
        @Email @NotBlank(message = "Email must not be empty or null")
        String email,

        @Schema(description = "User's first name", example = "Cristian")
        @NotBlank(message = "Name must not be empty")
        String name,

        @Schema(description = "User's last name", example = "Gomez")
        @NotBlank(message = "Lastname must not be empty")
        String lastname,

        @Schema(description = "User's password. Must be at least 6 characters", example = "SecureP@ss123")
        @Size(min = 6, message = "Password must be at least 6 characters long.")
        String password
) {
}