package com.microservice.user.services.impl;

import com.microservice.user.models.UserEntity;
import com.microservice.user.repositories.UserEntityRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserEntityRepository userEntityRepository;

    public UserDetailsServiceImpl(UserEntityRepository userEntityRepository) {
        this.userEntityRepository = userEntityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userEntityRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email " + email + " does not exist in the database."));

        return new User(
                userEntity.getEmail(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoLocked(),
                List.of(new SimpleGrantedAuthority(userEntity.getRole().name())));
        }
    }
