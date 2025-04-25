package com.microservice.user.testUtils;

import com.microservice.user.enums.RoleEnum;
import com.microservice.user.exceptions.AccessDeniedException;
import com.microservice.user.utils.RoleValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RoleValidatorTest {

    private RoleValidator roleValidator;

    @Test
    void testValidateRole_WhenHeaderDoesNotContainAllowedRoles_ThrowsAccessDenied() {
        assertThrows(AccessDeniedException.class, () ->
                RoleValidator.validateRole("ROLE_CLIENT", "No access", RoleEnum.ADMIN));
    }
}
