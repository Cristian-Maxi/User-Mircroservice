package com.microservice.user.testDTOS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.microservice.user.dtos.UserEntityDTO.JWTTokenDTO;
import org.junit.jupiter.api.Test;

public class JWTTokenDTOTest {

    @Test
    public void testJWTTokenDTO() {
        String exampleToken = "eyJhbGciOiJIUzI1NiIsIn...";
        JWTTokenDTO dto = new JWTTokenDTO(exampleToken);

        assertNotNull(dto.jwtToken(), "JWT Token should not be null");
        assertEquals(exampleToken, dto.jwtToken(), "JWT Token should be the same as the provided token");
    }
}
