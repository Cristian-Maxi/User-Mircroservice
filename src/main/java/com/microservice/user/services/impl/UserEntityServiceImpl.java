package com.microservice.user.services.impl;

import com.microservice.user.config.RabbitMQConfig;
import com.microservice.user.dtos.UserEntityDTO.UserEntityRequestDTO;
import com.microservice.user.dtos.UserEntityDTO.UserEntityResponseDTO;
import com.microservice.user.dtos.UserEntityDTO.UserEntityUpdateDTO;
import com.microservice.user.enums.RoleEnum;
import com.microservice.user.exceptions.ApplicationException;
import com.microservice.user.mappers.UserEntityMapper;
import com.microservice.user.models.UserEntity;
import com.microservice.user.repositories.UserEntityRepository;
import com.microservice.user.services.UserEntityService;
import com.microservice.user.utils.UserRegisteredEvent;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserEntityServiceImpl implements UserEntityService {

    @Autowired
    private UserEntityRepository userEntityRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserEntityMapper userEntityMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    //@Transactional(rollbackFor = Exception.class)
    @Override
    public UserEntityResponseDTO createUser(UserEntityRequestDTO userEntityRequestDTO) {
        if (userEntityRepository.existsByEmail(userEntityRequestDTO.email())) {
            throw new ApplicationException("email", "Email does not exist in the database.");
        }
        String encodedPassword = passwordEncoder.encode(userEntityRequestDTO.password());
        UserEntity userEntity = userEntityMapper.toEntity(userEntityRequestDTO);
        userEntity.setPassword(encodedPassword);
        userEntity.setRole(userEntity.getEmail().contains("@admin") ? RoleEnum.ADMIN : RoleEnum.CLIENT);
        userEntityRepository.save(userEntity);

        UserRegisteredEvent event = new UserRegisteredEvent(userEntity.getEmail(), userEntity.getName(),userEntity.getLastname());
        rabbitTemplate.convertAndSend(RabbitMQConfig.USER_EXCHANGE, RabbitMQConfig.USER_ROUTING_KEY, event);

        return userEntityMapper.toResponseDTO(userEntity);
    }

    @Override
    public List<UserEntityResponseDTO> getAll() {
        List<UserEntity> userEntities = userEntityRepository.findByIsEnabledTrue();
        return userEntityMapper.toResponseListDTO(userEntities);
    }

    @Transactional
    @Override
    public UserEntityResponseDTO update(UserEntityUpdateDTO userEntityUpdateDTO) {
        UserEntity userEntity = userEntityRepository.findById(userEntityUpdateDTO.id())
                .orElseThrow(() -> new EntityNotFoundException("User ID not Found"));
        if(userEntityUpdateDTO.name() != null && !userEntityUpdateDTO.name().isBlank()) {
            userEntity.setName(userEntityUpdateDTO.name());
        }
        if(userEntityUpdateDTO.lastname() != null && !userEntityUpdateDTO.lastname().isBlank()) {
            userEntity.setLastname(userEntityUpdateDTO.lastname());
        }
        userEntityRepository.save(userEntity);
        return userEntityMapper.toResponseDTO(userEntity);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        UserEntity userEntity = userEntityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("The entered User ID was not Found."));
        userEntity.setEnabled(false);
        userEntityRepository.save(userEntity);
    }


    @Override
    public Long findIdByEmail(String email) {
        UserEntity userEntity = userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with Email " + email + " not Found."));
        return userEntity.getId();
    }
}
