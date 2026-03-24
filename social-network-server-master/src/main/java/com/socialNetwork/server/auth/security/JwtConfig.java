package com.socialNetwork.server.auth.security;

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

    @Value("${jwt.pending-login-expiration}")
    private long pendingLoginExpiration;

    @Value("${jwt.pending-register-expiration}")
    private long pendingRegisterExpiration;


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

    public long getPendingLoginExpiration() {
        return pendingLoginExpiration;
    }

    public long getPendingRegisterExpiration() {
        return pendingRegisterExpiration;
    }
}