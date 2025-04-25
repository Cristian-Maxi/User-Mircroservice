package com.microservice.user.testServices;

import com.microservice.user.enums.RoleEnum;
import com.microservice.user.models.UserEntity;
import com.microservice.user.repositories.UserEntityRepository;
import com.microservice.user.services.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserEntityRepository userEntityRepository;

    UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetailsService = new UserDetailsServiceImpl(userEntityRepository);
    }

    @Test
    void testLoadUserByUsername_Success() {
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("password");
        userEntity.setRole(RoleEnum.CLIENT);
        userEntity.setEnabled(true);
        userEntity.setAccountNoExpired(true);
        userEntity.setCredentialNoExpired(true);
        userEntity.setAccountNoLocked(true);

        when(userEntityRepository.findByEmail("test@example.com"))
                .thenReturn(Optional.of(userEntity));

        UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

        assertEquals("test@example.com", userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isCredentialsNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("CLIENT", userDetails.getAuthorities().iterator().next().getAuthority());
    }


    @Test
    void testLoadUserByUsername_NotFound() {
        when(userEntityRepository.findByEmail("notfound@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () ->
                userDetailsService.loadUserByUsername("notfound@example.com"));
    }
}