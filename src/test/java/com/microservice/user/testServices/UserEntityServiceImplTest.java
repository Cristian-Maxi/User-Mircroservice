package com.microservice.user.testServices;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import com.microservice.user.dtos.UserEntityDTO.UserEntityRequestDTO;
import com.microservice.user.dtos.UserEntityDTO.UserEntityResponseDTO;
import com.microservice.user.dtos.UserEntityDTO.UserEntityUpdateDTO;
import com.microservice.user.enums.RoleEnum;
import com.microservice.user.exceptions.ApplicationException;
import com.microservice.user.mappers.UserEntityMapper;
import com.microservice.user.models.UserEntity;
import com.microservice.user.repositories.UserEntityRepository;
import com.microservice.user.services.impl.UserEntityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserEntityServiceImplTest {

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserEntityMapper userEntityMapper;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private UserEntityServiceImpl userEntityService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser() {
        UserEntityRequestDTO requestDTO = new UserEntityRequestDTO("user@example.com", "Cristian", "Gomez", "SecureP@ss123");
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(requestDTO.email());
        userEntity.setName(requestDTO.name());
        userEntity.setLastname(requestDTO.lastname());
        userEntity.setPassword("encodedPassword");
        userEntity.setRole(RoleEnum.CLIENT);

        when(userEntityRepository.existsByEmail(requestDTO.email())).thenReturn(false);
        when(passwordEncoder.encode(requestDTO.password())).thenReturn("encodedPassword");
        when(userEntityMapper.toEntity(requestDTO)).thenReturn(userEntity);
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userEntityMapper.toResponseDTO(userEntity)).thenReturn(new UserEntityResponseDTO(1L, "user@example.com", "Cristian", "Gomez", RoleEnum.CLIENT));

        UserEntityResponseDTO responseDTO = userEntityService.createUser(requestDTO);

        assertNotNull(responseDTO);
        assertEquals("user@example.com", responseDTO.email());
        assertEquals("Cristian", responseDTO.name());
        assertEquals("Gomez", responseDTO.lastname());
        assertEquals(RoleEnum.CLIENT, responseDTO.role());

        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), any(Object.class));
    }

    @Test
    public void testCreateUser_EmailAlreadyExists() {
        UserEntityRequestDTO requestDTO = new UserEntityRequestDTO("user@example.com", "Cristian", "Gomez", "SecureP@ss123");

        when(userEntityRepository.existsByEmail(requestDTO.email())).thenReturn(true);

        ApplicationException exception = assertThrows(ApplicationException.class, () -> userEntityService.createUser(requestDTO));

        assertEquals("Email does not exist in the database.", exception.getMessage());
    }

    @Test
    public void testUpdateUser() {
        UserEntityUpdateDTO updateDTO = new UserEntityUpdateDTO(1L, "Jane", "Smith");
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setName("Cristian");
        userEntity.setLastname("Gomez");

        when(userEntityRepository.findById(updateDTO.id())).thenReturn(Optional.of(userEntity));
        when(userEntityRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(userEntityMapper.toResponseDTO(userEntity)).thenReturn(new UserEntityResponseDTO(1L, "user@example.com", "Jane", "Smith", RoleEnum.CLIENT));

        UserEntityResponseDTO responseDTO = userEntityService.update(updateDTO);

        assertNotNull(responseDTO);
        assertEquals("Jane", responseDTO.name());
        assertEquals("Smith", responseDTO.lastname());
    }

    @Test
    public void testDeleteUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEnabled(true);

        when(userEntityRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        userEntityService.delete(1L);

        assertFalse(userEntity.isEnabled());
        verify(userEntityRepository, times(1)).save(userEntity);
    }

    @Test
    public void testFindIdByEmail() {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setEmail("user@example.com");

        when(userEntityRepository.findByEmail("user@example.com")).thenReturn(Optional.of(userEntity));

        Long userId = userEntityService.findIdByEmail("user@example.com");

        assertEquals(1L, userId);
    }
}
