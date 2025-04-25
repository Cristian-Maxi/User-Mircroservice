package com.microservice.user.utils;

import com.microservice.user.enums.RoleEnum;
import com.microservice.user.exceptions.AccessDeniedException;

public class RoleValidator {
    public static void validateRole(String rolesHeader, String errorMessage, RoleEnum... allowedRoles) {
        for (RoleEnum role : allowedRoles) {
            if (rolesHeader.contains(role.name())) return;
        }
        throw new AccessDeniedException(errorMessage);
    }
}