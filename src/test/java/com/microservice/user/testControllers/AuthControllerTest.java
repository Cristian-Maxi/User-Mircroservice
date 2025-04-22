package com.microservice.user.testControllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.microservice.user.controllers.AuthController;
import com.microservice.user.dtos.UserEntityDTO.JWTTokenDTO;
import com.microservice.user.dtos.UserEntityDTO.UserAuthenticationData;
import com.microservice.user.dtos.UserEntityDTO.UserEntityRequestDTO;
import com.microservice.user.dtos.UserEntityDTO.UserEntityResponseDTO;
import com.microservice.user.enums.RoleEnum;
import com.microservice.user.services.AuthenticationService;
import com.microservice.user.services.UserEntityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserEntityService userEntityService;

    @MockBean
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUserEntity() throws Exception {
        UserEntityRequestDTO requestDTO = new UserEntityRequestDTO("user@example.com", "Cristian", "Gomez", "SecureP@ss123");
        UserEntityResponseDTO responseDTO = new UserEntityResponseDTO(1L, "user@example.com", "Cristian", "Gomez", RoleEnum.CLIENT);

        when(userEntityService.createUser(any(UserEntityRequestDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/auth/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@example.com\",\"name\":\"Cristian\",\"lastname\":\"Gomez\",\"password\":\"SecureP@ss123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.name").value("Cristian"))
                .andExpect(jsonPath("$.lastname").value("Gomez"))
                .andExpect(jsonPath("$.role").value("CLIENT"));
    }

    @Test
    public void testCreateUserEntity_InvalidRequest() throws Exception {
        mockMvc.perform(post("/api/auth/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"invalid-email\",\"name\":\"\",\"lastname\":\"\",\"password\":\"short\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAuthenticateUser() throws Exception {
        UserAuthenticationData authenticationData = new UserAuthenticationData("user@example.com", "password123");
        JWTTokenDTO jwtTokenDTO = new JWTTokenDTO("generatedToken");

        when(authenticationService.authenticate(any(UserAuthenticationData.class))).thenReturn(jwtTokenDTO);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user@example.com\",\"password\":\"password123\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jwtToken").value("generatedToken"));
    }

    @Test
    public void testAuthenticateUser_InvalidRequest() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"\",\"password\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}