package com.socialNetwork.server.login.security;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfig {


    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    @PostConstruct
    public void validate() {
        if (secret == null || secret.isBlank()) {
            throw new RuntimeException("JWT_SECRET is missing");
        }
    }

    public String getSecret() {
        return secret;
    }

    public long getAccessExpiration() {
        return accessExpiration;
    }

    public long getRefreshExpiration() {
        return refreshExpiration;
    }
}