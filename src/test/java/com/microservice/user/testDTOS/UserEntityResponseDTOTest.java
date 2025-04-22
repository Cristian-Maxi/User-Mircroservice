package com.microservice.user.testDTOS;

import com.microservice.user.dtos.UserEntityDTO.UserEntityResponseDTO;
import com.microservice.user.enums.RoleEnum;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserEntityResponseDTOTest {

    @Test
    public void testUserEntityResponseDTO() {
        RoleEnum role = RoleEnum.ADMIN;

        UserEntityResponseDTO dto = new UserEntityResponseDTO(
                101L,
                "user@example.com",
                "Cristian",
                "Gomez",
                role
        );

        assertEquals(101L, dto.id(), "ID should be 101");
        assertEquals("user@example.com", dto.email(), "Email should be user@example.com");
        assertEquals("Cristian", dto.name(), "Name should be Cristian");
        assertEquals("Gomez", dto.lastname(), "Lastname should be Gomez");
        assertEquals(role, dto.role(), "Role should be ADMIN");
    }
}
