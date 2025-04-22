package com.microservice.user.testDTOS;

import com.microservice.user.dtos.UserEntityDTO.UserEntityUpdateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

public class UserEntityUpdateDTOTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidDTO() {
        UserEntityUpdateDTO dto = new UserEntityUpdateDTO(1L, "Jane", "Smith");
        Set<ConstraintViolation<UserEntityUpdateDTO>> violations = validator.validate(dto);
        assertEquals(0, violations.size(), "DTO should be valid");
    }

    @Test
    public void testInvalidIdNull() {
        UserEntityUpdateDTO dto = new UserEntityUpdateDTO(null, "Jane", "Smith");

        Set<ConstraintViolation<UserEntityUpdateDTO>> violations = validator.validate(dto);

        assertEquals(1, violations.size(), "ID must not be null");
        assertTrue(violations.iterator().next().getMessage().contains("ID must not be null"));
    }

    @Test
    public void testInvalidIdValidName() {
        UserEntityUpdateDTO dto = new UserEntityUpdateDTO(1L, null, "Smith");
        Set<ConstraintViolation<UserEntityUpdateDTO>> violations = validator.validate(dto);
        assertEquals(0, violations.size(), "DTO should be valid even if name is null");
    }

    @Test
    public void testInvalidNameNull() {
        UserEntityUpdateDTO dto = new UserEntityUpdateDTO(1L, null, "Smith");
        Set<ConstraintViolation<UserEntityUpdateDTO>> violations = validator.validate(dto);
        assertEquals(0, violations.size(), "DTO should be valid even with a null name");
    }

    @Test
    public void testValidNameAndLastname() {
        UserEntityUpdateDTO dto = new UserEntityUpdateDTO(1L, "Jane", "Smith");
        Set<ConstraintViolation<UserEntityUpdateDTO>> violations = validator.validate(dto);
        assertEquals(0, violations.size(), "DTO should be valid with non-null name and lastname");
    }
}
