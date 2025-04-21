package com.microservice.user.dtos.UserEntityDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record UserAuthenticationData(
        @Schema(description = "User's email for login", example = "login@example.com")
        @NotBlank(message = "Email must not be empty")
        String email,

        @Schema(description = "User's login password", example = "password123")
        @NotBlank(message = "Password must not be empty")
        String password
) {
}