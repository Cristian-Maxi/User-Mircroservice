package com.microservice.user.controllers;

import com.microservice.user.dtos.UserEntityDTO.JWTTokenDTO;
import com.microservice.user.dtos.UserEntityDTO.UserAuthenticationData;
import com.microservice.user.dtos.UserEntityDTO.UserEntityRequestDTO;
import com.microservice.user.dtos.UserEntityDTO.UserEntityResponseDTO;
import com.microservice.user.exceptions.ApplicationException;
import com.microservice.user.services.AuthenticationService;
import com.microservice.user.services.UserEntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration and authentication")
public class AuthController {

    private final UserEntityService userEntityService;
    private final AuthenticationService authenticationService;

    public AuthController(UserEntityService userEntityService, AuthenticationService authenticationService) {
        this.userEntityService = userEntityService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/create")
    @Operation(summary = "Create User", description = "Creates a new user account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "500", description = "Internal server error")})
    public ResponseEntity<UserEntityResponseDTO> createUserEntity(@Valid @RequestBody UserEntityRequestDTO userEntityRequestDTO) {
        try{
            UserEntityResponseDTO userEntityResponseDTO = userEntityService.createUser(userEntityRequestDTO);
            return new ResponseEntity<>(userEntityResponseDTO, HttpStatus.CREATED);
        } catch (ApplicationException e) {
            throw new ApplicationException(" An error has occurred in the field " + e.getCampo() + ", Description: "+e.getMessage());
        }
    }

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticates user and returns a JWT token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials"),
            @ApiResponse(responseCode = "400", description = "Invalid request")})
    public ResponseEntity<JWTTokenDTO> authenticateUser(@RequestBody @Valid UserAuthenticationData userAuthenticationData){
        JWTTokenDTO jwtTokenDTO = authenticationService.authenticate(userAuthenticationData);
        return ResponseEntity.ok(jwtTokenDTO);
    }
}