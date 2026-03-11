package com.socialNetwork.server.login.controllers;

import com.socialNetwork.server.login.requests.LoginRequest;
import com.socialNetwork.server.login.responses.LoginResponse;
import com.socialNetwork.server.login.requests.RegisterRequest;
import com.socialNetwork.server.login.responses.RegisterResponse;
import com.socialNetwork.server.login.services.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

//    @RequestMapping("/login")
//    public LoginResponse login(@RequestBody LoginRequest request){
//        return authService.login(request);
//    }
}