package com.microservice.user.testServices;

import com.microservice.user.repositories.UserEntityRepository;
import com.microservice.user.security.variablesEnv.SecretKeyConfig;
import com.microservice.user.services.impl.TokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceImplTest {

    @Mock
    private SecretKeyConfig secretKeyConfig;

    @Mock
    private UserEntityRepository userEntityRepository;

    @Mock
    private Authentication authentication;

    TokenServiceImpl tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        tokenService = new TokenServiceImpl(secretKeyConfig, userEntityRepository);
    }

    @Test
    void testGenerateToken_UserNotFound() {
        when(authentication.getName()).thenReturn("missing@example.com");
        when(userEntityRepository.findByEmail("missing@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                tokenService.generateToken(authentication));
    }
}
