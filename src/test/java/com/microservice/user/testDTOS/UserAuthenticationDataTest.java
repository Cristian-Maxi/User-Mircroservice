package com.microservice.user.testDTOS;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.microservice.user.dtos.UserEntityDTO.UserAuthenticationData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

public class UserAuthenticationDataTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidDTO() {
        UserAuthenticationData dto = new UserAuthenticationData("login@example.com", "password123");
        Set<ConstraintViolation<UserAuthenticationData>> violations = validator.validate(dto);
        assertEquals(0, violations.size(), "DTO should be valid");
    }

    @Test
    public void testInvalidEmailNull() {
        UserAuthenticationData dto = new UserAuthenticationData(null, "password123");
        Set<ConstraintViolation<UserAuthenticationData>> violations = validator.validate(dto);

        assertEquals(1, violations.size(), "Email must not be empty");
        assertTrue(violations.iterator().next().getMessage().contains("Email must not be empty"));
    }

    @Test
    public void testInvalidPasswordNull() {
        UserAuthenticationData dto = new UserAuthenticationData("login@example.com", null);
        Set<ConstraintViolation<UserAuthenticationData>> violations = validator.validate(dto);

        assertEquals(1, violations.size(), "Password must not be empty");
        assertTrue(violations.iterator().next().getMessage().contains("Password must not be empty"));
    }

    @Test
    public void testInvalidEmailAndPasswordNull() {
        UserAuthenticationData dto = new UserAuthenticationData(null, null);
        Set<ConstraintViolation<UserAuthenticationData>> violations = validator.validate(dto);
        assertEquals(2, violations.size(), "Both email and password must not be empty");
    }

    @Test
    public void testEmptyEmail() {
        UserAuthenticationData dto = new UserAuthenticationData("", "password123");
        Set<ConstraintViolation<UserAuthenticationData>> violations = validator.validate(dto);

        assertEquals(1, violations.size(), "Email must not be empty");
        assertTrue(violations.iterator().next().getMessage().contains("Email must not be empty"));
    }

    @Test
    public void testEmptyPassword() {
        UserAuthenticationData dto = new UserAuthenticationData("login@example.com", "");
        Set<ConstraintViolation<UserAuthenticationData>> violations = validator.validate(dto);

        assertEquals(1, violations.size(), "Password must not be empty");
        assertTrue(violations.iterator().next().getMessage().contains("Password must not be empty"));
    }
}
