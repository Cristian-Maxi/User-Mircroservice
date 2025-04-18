package com.microservice.user.dtos.UserEntityDTO;

import jakarta.validation.constraints.*;

public record UserEntityRequestDTO(
        @Email @NotBlank(message = "Email must not be empty or null")
        String email,
        @NotBlank(message = "Name must not be empty")
        String name,
        @NotBlank(message = "Lastname must not be empty")
        String lastname,
        @Size(min = 6, message = "Password must be at least 6 characters long.")
        String password
) {
}