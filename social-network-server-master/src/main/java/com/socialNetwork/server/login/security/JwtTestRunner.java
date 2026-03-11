package com.socialNetwork.server.login.security;

import com.socialNetwork.server.login.security.JwtService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class JwtTestRunner implements CommandLineRunner { //מחלקת בדיקה למחוק!!!!!!!!

    private final JwtService jwtService;

    public JwtTestRunner(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void run(String... args) {

        String token = jwtService.generateAccessToken(1L, "testUser");

        System.out.println("TOKEN:");
        System.out.println(token);

        System.out.println("USERNAME:");
        System.out.println(jwtService.extractUsername(token));

        System.out.println("USER ID:");
        System.out.println(jwtService.extractUserId(token));
        System.out.println(
                "EXPIRED: " + jwtService.isTokenExpired(token)
        );
    }
}