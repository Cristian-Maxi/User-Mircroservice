package com.microservice.user.dtos.UserEntityDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UserEntityUpdateDTO(
        @Schema(description = "ID of the user to update", example = "1")
        @NotNull(message = "ID must not be null")
        Long id,

        @Schema(description = "Updated first name", example = "Jane")
        String name,

        @Schema(description = "Updated last name", example = "Smith")
        String lastname
) {
}