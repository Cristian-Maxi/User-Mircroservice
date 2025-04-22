package com.microservice.user.testDTOS;

import com.microservice.user.dtos.UserEntityDTO.UserEntityRequestDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserEntityRequestDTOTest {

    private final Validator validator;

    public UserEntityRequestDTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidDTO() {
        UserEntityRequestDTO dto = new UserEntityRequestDTO(
                "user@example.com",
                "Cristian",
                "Gomez",
                "SecureP@ss123"
        );

        Set<ConstraintViolation<UserEntityRequestDTO>> violations = validator.validate(dto);
        assertEquals(0, violations.size(), "DTO should be valid");
    }

    @Test
    public void testInvalidEmail() {
        UserEntityRequestDTO dto = new UserEntityRequestDTO(
                "invalid-email",
                "Cristian",
                "Gomez",
                "SecureP@ss123"
        );

        Set<ConstraintViolation<UserEntityRequestDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size(), "Email should be valid");
    }

    @Test
    public void testEmptyName() {
        UserEntityRequestDTO dto = new UserEntityRequestDTO(
                "user@example.com",
                "",
                "Gomez",
                "SecureP@ss123"
        );

        Set<ConstraintViolation<UserEntityRequestDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size(), "Name should not be empty");
    }

    @Test
    public void testEmptyLastname() {
        UserEntityRequestDTO dto = new UserEntityRequestDTO(
                "user@example.com",
                "Cristian",
                "",
                "SecureP@ss123"
        );

        Set<ConstraintViolation<UserEntityRequestDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size(), "Lastname should not be empty");
    }

    @Test
    public void testShortPassword() {
        UserEntityRequestDTO dto = new UserEntityRequestDTO(
                "user@example.com",
                "Cristian",
                "Gomez",
                "short"
        );

        Set<ConstraintViolation<UserEntityRequestDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size(), "Password should be at least 6 characters long");
    }

    @Test
    public void testNullPassword() {
        UserEntityRequestDTO dto = new UserEntityRequestDTO(
                "user@example.com",
                "Cristian",
                "Gomez",
                null
        );

        Set<ConstraintViolation<UserEntityRequestDTO>> violations = validator.validate(dto);
        assertEquals(1, violations.size(), "Password should not be null");
    }
}
