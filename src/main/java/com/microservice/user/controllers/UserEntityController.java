package com.microservice.user.controllers;


import com.microservice.user.dtos.ApiResponseDTO;
import com.microservice.user.dtos.UserEntityDTO.UserEntityResponseDTO;
import com.microservice.user.dtos.UserEntityDTO.UserEntityUpdateDTO;
import com.microservice.user.enums.RoleEnum;
import com.microservice.user.exceptions.ApplicationException;
import com.microservice.user.services.UserEntityService;
import com.microservice.user.utils.RoleValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User", description = "Endpoints for managing user entities")
public class UserEntityController {

    private final UserEntityService userEntityService;

    public UserEntityController(UserEntityService userEntityService) {
        this.userEntityService = userEntityService;
    }

    @PatchMapping("/update")
    @Operation(summary = "Update User", description = "Updates the data of a user entity. Only CLIENT role is allowed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "400", description = "Invalid request")})
    public ResponseEntity<ApiResponseDTO<UserEntityResponseDTO>> updateUserEntity(@Valid @RequestBody UserEntityUpdateDTO userEntityUpdateDTO,
                                                                                  @RequestHeader("X-User-Authorities") String roles) {
        RoleValidator.validateRole(roles,"Access denied: You must be a CLIENT to update a user", RoleEnum.CLIENT);
        UserEntityResponseDTO userEntityResponseDTO = userEntityService.update(userEntityUpdateDTO);
        String message = "Client Updated Successfully";
        return new ResponseEntity<>(new ApiResponseDTO<>(true, message, userEntityResponseDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Delete User", description = "Deletes a user entity by ID. Only ADMIN role is allowed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    public ResponseEntity<?> deleteUserEntity(@PathVariable Long id, @RequestHeader("X-User-Authorities") String roles) {
        RoleValidator.validateRole(roles,"Access denied: You must be an ADMIN or CLIENT to delete a user", RoleEnum.ADMIN, RoleEnum.CLIENT);
        userEntityService.delete(id);
        String message = "User Successfully Deleted";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    @Operation(summary = "Get All Users", description = "Returns a list of all registered users. Only ADMIN role is allowed.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users returned successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied")})
    public ResponseEntity<ApiResponseDTO<UserEntityResponseDTO>> getAllUsers(@RequestHeader("X-User-Authorities") String roles) {
        RoleValidator.validateRole(roles,"Access denied: You must be an ADMIN to see all Users in Database", RoleEnum.ADMIN);
        try {
            List<UserEntityResponseDTO> userEntityResponseDTO = userEntityService.getAll();
            if (userEntityResponseDTO.isEmpty()) {
                return new ResponseEntity<>(new ApiResponseDTO<>(false, "There are no registered users", userEntityResponseDTO), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ApiResponseDTO<>(true, "Registered Users", userEntityResponseDTO), HttpStatus.OK);
            }
        } catch (ApplicationException e) {
            throw new ApplicationException(" An error has occurred " + e.getMessage());
        }
    }

    @GetMapping("/validateUserByEmail/{email}")
    @Operation(summary = "Validate User by Email", description = "Returns the ID of a user by their email, or 404 if not found.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    public Long validateUserByEmail(@Parameter(description = "Email of the user to validate") @PathVariable String email) {
        try {
            return userEntityService.findIdByEmail(email);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
