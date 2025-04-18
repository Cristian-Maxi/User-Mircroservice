package com.microservice.user.utils;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisteredEvent {
    private String email;
    private String username;
}
