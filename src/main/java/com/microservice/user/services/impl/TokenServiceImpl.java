package com.microservice.user.services.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.microservice.user.models.UserEntity;
import com.microservice.user.repositories.UserEntityRepository;
import com.microservice.user.security.variablesEnv.SecretKeyConfig;
import com.microservice.user.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    private SecretKeyConfig secretKeyConfig;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Override
    public String generateToken(Authentication authentication) {
        try{
            String username = authentication.getName();

            UserEntity userEntity = userEntityRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not Found"));

            List<String> authorities = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            System.out.println("Generating token...");
            Algorithm algorithm = Algorithm.HMAC256(secretKeyConfig.getSECRET_KEY());
            return JWT.create()
                    .withIssuer("Java-Shark")
                    .withSubject(username)
                    .withClaim("id", userEntity.getId())
                    .withClaim("authorities", authorities)
                    .withExpiresAt(Date.from(generateExpirationDate()))
                    .sign(algorithm);

        }catch (JWTCreationException e){
            throw new RuntimeException("Error al crear el token");
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-05:00"));
    }

}
