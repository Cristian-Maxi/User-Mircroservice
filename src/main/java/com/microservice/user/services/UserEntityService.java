package com.microservice.user.services;

import com.microservice.user.dtos.UserEntityDTO.UserEntityRequestDTO;
import com.microservice.user.dtos.UserEntityDTO.UserEntityResponseDTO;
import com.microservice.user.dtos.UserEntityDTO.UserEntityUpdateDTO;

import java.util.List;

public interface UserEntityService {
    UserEntityResponseDTO createUser(UserEntityRequestDTO userEntityRequestDTO);
    List<UserEntityResponseDTO> getAll();
    UserEntityResponseDTO update(UserEntityUpdateDTO userEntityUpdateDTO);
    void delete(Long id);
    Long findIdByEmail(String email);
}
