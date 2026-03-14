
package com.socialNetwork.server.login.services;

import com.socialNetwork.server.login.requests.LoginRequest;
import com.socialNetwork.server.login.requests.RegisterRequest;
import com.socialNetwork.server.login.responses.BasicResponse;
import com.socialNetwork.server.login.responses.LoginResponse;

import com.socialNetwork.server.login.responses.RegisterResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthManeger {

    private final LoginService loginService;
    private final RegisterService registerService;
    private final TokenService tokenService;

    public AuthManeger(LoginService loginService, RegisterService registerService,
                       TokenService tokenService) {
        this.loginService = loginService;
        this.registerService = registerService;
        this.tokenService = tokenService;
    }

    public LoginResponse login(LoginRequest request) {
        return loginService.login(request);
    }

    public RegisterResponse register(RegisterRequest request) {
        return registerService.register(request);
    }

    public String refreshAccessToken(String refreshTokenValue) {
        return tokenService.refreshAccessToken(refreshTokenValue);
    }

    public BasicResponse logout(String refreshTokenValue) {
        return tokenService.logout(refreshTokenValue);
    }
}