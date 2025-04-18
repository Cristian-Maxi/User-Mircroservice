package com.microservice.user.controllers;

import com.microservice.user.dtos.UserEntityDTO.JWTTokenDTO;
import com.microservice.user.dtos.UserEntityDTO.UserAuthenticationData;
import com.microservice.user.dtos.UserEntityDTO.UserEntityRequestDTO;
import com.microservice.user.dtos.UserEntityDTO.UserEntityResponseDTO;
import com.microservice.user.exceptions.ApplicationException;
import com.microservice.user.services.AuthenticationService;
import com.microservice.user.services.UserEntityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserEntityService userEntityService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/create")
    public ResponseEntity<UserEntityResponseDTO> createUserEntity(@Valid @RequestBody UserEntityRequestDTO userEntityRequestDTO) {
        try{
            UserEntityResponseDTO userEntityResponseDTO = userEntityService.createUser(userEntityRequestDTO);
            return new ResponseEntity<>(userEntityResponseDTO, HttpStatus.CREATED);
        } catch (ApplicationException e) {
            throw new ApplicationException(" An error has occurred in the field " + e.getCampo() + ", Description: "+e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<JWTTokenDTO> authenticateUser(@RequestBody @Valid UserAuthenticationData userAuthenticationData){
        JWTTokenDTO jwtTokenDTO = authenticationService.authenticate(userAuthenticationData);
        return ResponseEntity.ok(jwtTokenDTO);
    }
}
