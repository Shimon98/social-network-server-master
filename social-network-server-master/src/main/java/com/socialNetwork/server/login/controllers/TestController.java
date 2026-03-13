package com.socialNetwork.server.login.controllers;

import com.socialNetwork.server.login.security.JwtService;
import com.socialNetwork.server.login.services.AuthCookieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final JwtService jwtService;
    private final AuthCookieService authCookieService;

    public TestController(JwtService jwtService, AuthCookieService authCookieService) {
        this.jwtService = jwtService;
        this.authCookieService = authCookieService;
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(HttpServletRequest request) {
        String token = authCookieService.getAccessTokenFromCookies(request);

        if (token == null || token.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing token");
        }

        if (!jwtService.isTokenValid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        if (!"access".equals(jwtService.extractTokenType(token))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong token type");
        }

        Long userId = jwtService.extractUserId(token);
        String username = jwtService.extractUsername(token);

        return ResponseEntity.ok("server works. userId=" + userId + ", username=" + username);
    }
}