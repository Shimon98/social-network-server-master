package com.socialNetwork.server.login.controllers;

import com.socialNetwork.server.login.requests.*;
import com.socialNetwork.server.login.responses.*;
import com.socialNetwork.server.login.services.AuthCookieService;
import com.socialNetwork.server.login.services.AuthManeger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthManeger authManeger;
    private final AuthCookieService authCookieService;

    public AuthController(AuthManeger authManeger, AuthCookieService authCookieService) {
        this.authManeger = authManeger;
        this.authCookieService = authCookieService;
    }

    @PostMapping ("/register/send-code")// ראשון
    public BasicResponse sendRegisterCode(@RequestBody EmailRequest request) {
        return authManeger.sendRegisterCode(request);
    }

    @PostMapping("/register/verify-code")// שני
    public RegisterCodeVerifyResponse verifyRegisterCode(@RequestBody RegisterCodeRequest request) {
        return authManeger.verifyRegisterCode(request);
    }

    @PostMapping("/register")// שלישי
    public RegisterResponse register(@RequestBody RegisterCompleteRequest request) {
        return authManeger.register(request);
    }

    @PostMapping("/login") // ראשון
    public PendingLoginResponse login(@RequestBody LoginRequest request) {
        return authManeger.startLogin(request);
    }

    @PostMapping("/login/verify-code")// שני
    public BasicResponse verifyLoginCode(@RequestBody LoginCodeRequest request, HttpServletResponse response) {
        LoginResponse result = authManeger.verifyLoginCode(request);
        if (!result.isSuccess()) {
            return new BasicResponse(false, result.getErrorCode());
        }
        authCookieService.addAccessTokenCookie(response, result.getAccessToken());
        authCookieService.addRefreshTokenCookie(response, result.getRefreshToken());
        return new BasicResponse(true, null);
    }

    @PostMapping("/refresh")
    public BasicResponse refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = authCookieService.getRefreshTokenFromCookies(request);
        String newAccessToken = authManeger.refreshAccessToken(refreshToken);
        if (newAccessToken == null) {
            authCookieService.clearAuthCookies(response);
            return new BasicResponse(false, null);
        }
        authCookieService.addAccessTokenCookie(response, newAccessToken);
        return new BasicResponse(true, null);
    }

    @PostMapping("/logout")
    public BasicResponse logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = authCookieService.getRefreshTokenFromCookies(request);
        authManeger.logout(refreshToken);
        authCookieService.clearAuthCookies(response);
        return new BasicResponse(true, null);
    }
}