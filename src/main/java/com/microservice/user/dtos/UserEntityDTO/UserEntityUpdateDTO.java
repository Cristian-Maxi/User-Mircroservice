package com.microservice.user.dtos.UserEntityDTO;

import jakarta.validation.constraints.NotNull;

public record UserEntityUpdateDTO(
        @NotNull(message = "ID must not be null")
        Long id,
        String name,
        String lastname
) {
}
