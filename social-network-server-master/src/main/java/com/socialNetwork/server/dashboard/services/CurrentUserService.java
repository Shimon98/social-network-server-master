package com.socialNetwork.server.dashboard.services;

import com.socialNetwork.server.auth.security.JwtService;
import com.socialNetwork.server.auth.services.AuthCookieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {

    private final JwtService jwtService;
    private final AuthCookieService authCookieService;

    public CurrentUserService(JwtService jwtService, AuthCookieService authCookieService) {
        this.jwtService = jwtService;
        this.authCookieService = authCookieService;
    }

    public Long extractCurrentUserId(HttpServletRequest request) {
        String token = authCookieService.getAccessTokenFromCookies(request);

        if (token == null || token.isBlank()) {
            throw new RuntimeException("Missing token");
        }

        if (!jwtService.isTokenValid(token)) {
            throw new RuntimeException("Invalid token");
        }

        if (!"access".equals(jwtService.extractTokenType(token))) {
            throw new RuntimeException("Wrong token type");
        }

        return jwtService.extractUserId(token);
    }
}