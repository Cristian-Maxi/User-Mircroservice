package com.microservice.user.controllers;


import com.microservice.user.dtos.ApiResponseDTO;
import com.microservice.user.dtos.userDTO.UserEntityResponseDTO;
import com.microservice.user.dtos.userDTO.UserEntityUpdateDTO;
import com.microservice.user.exceptions.ApplicationException;
import com.microservice.user.services.UserEntityService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserEntityController {

    @Autowired
    private UserEntityService userEntityService;

    @PatchMapping("/update")
    public ResponseEntity<ApiResponseDTO<UserEntityResponseDTO>> updateUserEntity(@Valid @RequestBody UserEntityUpdateDTO userEntityUpdateDTO) {
        UserEntityResponseDTO userEntityResponseDTO = userEntityService.update(userEntityUpdateDTO);
        String message = "Client Updated Successfully";
        return new ResponseEntity<>(new ApiResponseDTO<>(true, message, userEntityResponseDTO), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUserEntity(@PathVariable Long id) {
        userEntityService.delete(id);
        String message = "User Successfully Deleted";
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponseDTO<UserEntityResponseDTO>> getAllUsuarios() {
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

}
