package com.socialNetwork.server.auth.controllers;

import com.socialNetwork.server.auth.requests.*;
import com.socialNetwork.server.auth.responses.*;
import com.socialNetwork.server.auth.services.AuthCookieService;
import com.socialNetwork.server.auth.services.AuthManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    private final AuthManager authManager;
    private final AuthCookieService authCookieService;

    public AuthController(AuthManager authManager, AuthCookieService authCookieService) {
        this.authManager = authManager;
        this.authCookieService = authCookieService;
    }

    @PostMapping ("/register/send-code")// ראשון
    public BasicResponse sendRegisterCode(@RequestBody EmailRequest request) {
        return authManager.sendRegisterCode(request);
    }

    @PostMapping("/register/verify-code")// שני
    public RegisterCodeVerifyResponse verifyRegisterCode(@RequestBody RegisterCodeRequest request) {
        return authManager.verifyRegisterCode(request);
    }

    @PostMapping("/register")// שלישי
    public RegisterResponse register(@RequestBody RegisterCompleteRequest request) {
        return authManager.register(request);
    }

    @PostMapping("/login") // ראשון
    public BasicResponse login(@RequestBody LoginRequest request) {
        return authManager.startLogin(request);
    }


    @PostMapping("/send-login-code")
    public  BasicResponse sendLoginCode(@RequestBody LoginCodeRequest request) {
        return authManager.sendLoginCode(request);
    }


    @PostMapping("/login/verify-code")
    public BasicResponse verifyLoginCode(@RequestBody LoginCodeAnswer request, HttpServletResponse response) {
        LoginResponse result = authManager.verifyLoginCode(request);
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
        String newAccessToken = authManager.refreshAccessToken(refreshToken);
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
        authManager.logout(refreshToken);
        authCookieService.clearAuthCookies(response);
        return new BasicResponse(true, null);
    }
}