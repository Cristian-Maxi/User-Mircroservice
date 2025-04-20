package com.microservice.user.dtos.UserEntityDTO;

import io.swagger.v3.oas.annotations.media.Schema;

public record JWTTokenDTO(
        @Schema(description = "JWT token for authentication", example = "eyJhbGciOiJIUzI1NiIsIn...")
        String jwtToken
) {
}