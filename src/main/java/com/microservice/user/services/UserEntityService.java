package com.microservice.user.services;

import com.microservice.user.dtos.userDTO.UserEntityRequestDTO;
import com.microservice.user.dtos.userDTO.UserEntityResponseDTO;
import com.microservice.user.dtos.userDTO.UserEntityUpdateDTO;

import java.util.List;

public interface UserEntityService {
    UserEntityResponseDTO createUser(UserEntityRequestDTO userEntityRequestDTO);
    List<UserEntityResponseDTO> getAll();
    UserEntityResponseDTO update(UserEntityUpdateDTO userEntityUpdateDTO);
    void delete(Long id);
    boolean existById(Long id);
    Long findIdByEmail(String email);
}
