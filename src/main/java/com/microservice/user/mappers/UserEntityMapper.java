package com.microservice.user.mappers;

import com.microservice.user.dtos.userDTO.UserEntityRequestDTO;
import com.microservice.user.dtos.userDTO.UserEntityResponseDTO;
import com.microservice.user.models.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    UserEntityMapper INSTANCE = Mappers.getMapper(UserEntityMapper.class);

    UserEntityResponseDTO toResponseDTO(UserEntity userEntity);

    UserEntity toEntity(UserEntityRequestDTO userEntityRequestDTO);

    List<UserEntityResponseDTO> toResponseListDTO(List<UserEntity> userEntity);
}