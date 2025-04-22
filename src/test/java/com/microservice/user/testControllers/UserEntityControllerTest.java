package com.microservice.user.testControllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.microservice.user.controllers.UserEntityController;
import com.microservice.user.dtos.UserEntityDTO.UserEntityResponseDTO;
import com.microservice.user.dtos.UserEntityDTO.UserEntityUpdateDTO;
import com.microservice.user.enums.RoleEnum;
import com.microservice.user.services.UserEntityService;
import jakarta.persistence.EntityNotFoundException;
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

import java.util.Arrays;
import java.util.List;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(UserEntityController.class)
public class UserEntityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserEntityService userEntityService;

    @InjectMocks
    private UserEntityController userEntityController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateUserEntity() throws Exception {
        UserEntityUpdateDTO updateDTO = new UserEntityUpdateDTO(1L, "Jane", "Smith");
        UserEntityResponseDTO responseDTO = new UserEntityResponseDTO(1L, "user@example.com", "Jane", "Smith", RoleEnum.CLIENT);

        when(userEntityService.update(any(UserEntityUpdateDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(patch("/api/user/update")
                        .header("X-User-Authorities", "CLIENT")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Jane\",\"lastname\":\"Smith\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value("user@example.com"))
                .andExpect(jsonPath("$.data.name").value("Jane"))
                .andExpect(jsonPath("$.data.lastname").value("Smith"))
                .andExpect(jsonPath("$.data.role").value("CLIENT"));
    }

    @Test
    public void testDeleteUserEntity() throws Exception {
        mockMvc.perform(delete("/api/user/delete/1")
                        .header("X-User-Authorities", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(content().string("User Successfully Deleted"));

        verify(userEntityService, times(1)).delete(1L);
    }

    @Test
    public void testGetAllUsers() throws Exception {
        UserEntityResponseDTO user1 = new UserEntityResponseDTO(1L, "admin@example.com", "Admin", "User", RoleEnum.ADMIN);
        UserEntityResponseDTO user2 = new UserEntityResponseDTO(2L, "client@example.com", "Client", "User", RoleEnum.CLIENT);
        List<UserEntityResponseDTO> users = Arrays.asList(user1, user2);

        when(userEntityService.getAll()).thenReturn(users);

        mockMvc.perform(get("/api/user/getAll")
                        .header("X-User-Authorities", "ADMIN"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataIterable[0].email").value("admin@example.com"))
                .andExpect(jsonPath("$.dataIterable[1].email").value("client@example.com"));
    }

    @Test
    public void testValidateUserByEmail() throws Exception {
        when(userEntityService.findIdByEmail("user@example.com")).thenReturn(1L);

        mockMvc.perform(get("/api/user/validateUserByEmail/user@example.com"))
                .andExpect(status().isOk())
                .andExpect(content().string("1"));
    }

    @Test
    public void testValidateUserByEmail_NotFound() throws Exception {
        when(userEntityService.findIdByEmail("nonexistent@example.com"))
                .thenThrow(new EntityNotFoundException("User with Email nonexistent@example.com not Found."));

        mockMvc.perform(get("/api/user/validateUserByEmail/nonexistent@example.com"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User with Email nonexistent@example.com not Found."));
    }
}
