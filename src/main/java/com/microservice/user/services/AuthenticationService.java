package com.microservice.user.services;

import com.microservice.user.dtos.userDTO.JWTTokenDTO;
import com.microservice.user.dtos.userDTO.UserAuthenticationData;

public interface AuthenticationService {
    JWTTokenDTO authenticate(UserAuthenticationData userAuthenticationData);
}