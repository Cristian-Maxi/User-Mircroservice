package com.microservice.user.dtos.UserEntityDTO;

import com.microservice.user.enums.RoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserEntityResponseDTO(
        @Schema(description = "Unique identifier of the user", example = "101")
        Long id,

        @Schema(description = "User email address", example = "user@example.com")
        String email,

        @Schema(description = "Username displayed in the system", example = "Cristian")
        String name,

        @Schema(description = "Username displayed in the system", example = "Gomez")
        String lastname,

        @Schema(description = "Role assigned to the user", example = "ADMIN")
        RoleEnum role
) {
}
