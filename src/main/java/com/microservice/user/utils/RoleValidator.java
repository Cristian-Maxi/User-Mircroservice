package com.microservice.user.utils;

import com.microservice.user.exceptions.ApplicationException;

public class RoleValidator {
    public static void validateRole(String rolesHeader, String errorMessage, String... allowedRoles) {
        for (String allowed : allowedRoles) {
            if (rolesHeader.contains(allowed)) return;
        }
        throw new ApplicationException(errorMessage);
    }
}