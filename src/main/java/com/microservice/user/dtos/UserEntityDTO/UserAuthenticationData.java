package com.microservice.user.dtos.UserEntityDTO;

import jakarta.validation.constraints.NotBlank;

public record UserAuthenticationData(
        @NotBlank(message = "Email must not be empty")
        String email,
        @NotBlank(message = "Password must not be empty")
        String password
) {
}
