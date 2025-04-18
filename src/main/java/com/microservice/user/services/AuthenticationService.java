package com.microservice.user.services;

import com.microservice.user.dtos.UserEntityDTO.JWTTokenDTO;
import com.microservice.user.dtos.UserEntityDTO.UserAuthenticationData;

public interface AuthenticationService {
    JWTTokenDTO authenticate(UserAuthenticationData userAuthenticationData);
}