package com.microservice.user.testServices;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.microservice.user.dtos.UserEntityDTO.JWTTokenDTO;
import com.microservice.user.dtos.UserEntityDTO.UserAuthenticationData;
import com.microservice.user.services.TokenService;
import com.microservice.user.services.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAuthenticate() {
        UserAuthenticationData userAuthenticationData = new UserAuthenticationData("user@example.com", "password123");
        Authentication authentication = mock(Authentication.class);
        String token = "generatedToken";

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(tokenService.generateToken(authentication)).thenReturn(token);

        JWTTokenDTO jwtTokenDTO = authenticationService.authenticate(userAuthenticationData);

        assertNotNull(jwtTokenDTO);
        assertEquals(token, jwtTokenDTO.jwtToken());
        assertEquals(authentication, SecurityContextHolder.getContext().getAuthentication());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService, times(1)).generateToken(authentication);
    }
}
