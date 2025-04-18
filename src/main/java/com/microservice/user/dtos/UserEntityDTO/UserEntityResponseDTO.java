package com.microservice.user.dtos.UserEntityDTO;

import com.microservice.user.enums.RoleEnum;

public record UserEntityResponseDTO(
        Long id,
        String email,
        String username,
        RoleEnum role
) {
}
