package com.socialNetwork.server.auth.controllers;

import com.socialNetwork.server.auth.email.EmailManager;
import com.socialNetwork.server.auth.security.JwtService;
import com.socialNetwork.server.auth.services.AuthCookieService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final JwtService jwtService;
    private final AuthCookieService authCookieService;
    @Autowired
    private EmailManager emailService;

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

    @GetMapping("/test2")
    public String test2(@RequestParam String email) {
        emailService.sendLoginCode(email);
        return "email sent";
    }
}//