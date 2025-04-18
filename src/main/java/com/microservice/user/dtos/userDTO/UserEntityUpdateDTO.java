package com.microservice.user.dtos.userDTO;

import jakarta.validation.constraints.NotNull;

public record UserEntityUpdateDTO(
        @NotNull(message = "El ID no debe ser nulo")
        Long id,
        String username
) {
}
