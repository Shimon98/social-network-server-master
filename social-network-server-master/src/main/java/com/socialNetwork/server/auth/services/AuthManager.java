
package com.socialNetwork.server.auth.services;

import com.socialNetwork.server.auth.requests.*;
import com.socialNetwork.server.auth.responses.BasicResponse;
import com.socialNetwork.server.auth.responses.LoginResponse;
import com.socialNetwork.server.auth.responses.PendingLoginResponse;
import com.socialNetwork.server.auth.responses.RegisterCodeVerifyResponse;
import com.socialNetwork.server.auth.responses.RegisterResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthManager {

    private final LoginService loginService;
    private final RegisterService registerService;
    private final TokenService tokenService;

    public AuthManager(LoginService loginService,
                       RegisterService registerService,
                       TokenService tokenService) {
        this.loginService = loginService;
        this.registerService = registerService;
        this.tokenService = tokenService;
    }

    public PendingLoginResponse startLogin(LoginRequest request) {
        return loginService.startLogin(request);
    }

    public LoginResponse verifyLoginCode(LoginCodeAnswer request) {
        return loginService.verifyLoginCode(request);
    }

    public BasicResponse sendRegisterCode(EmailRequest request) {
        return registerService.sendRegisterCode(request);
    }

    public RegisterCodeVerifyResponse verifyRegisterCode(RegisterCodeRequest request) {
        return registerService.verifyRegisterCode(request);
    }

    public RegisterResponse register(RegisterCompleteRequest request) {
        return registerService.register(request);
    }

    public String refreshAccessToken(String refreshTokenValue) {
        return tokenService.refreshAccessToken(refreshTokenValue);
    }

    public BasicResponse logout(String refreshTokenValue) {
        return tokenService.logout(refreshTokenValue);
    }

    public BasicResponse sendLoginCode  (LoginCodeRequest request) {
        return loginService.sendLoginCode(request);
    }

    public BasicResponse checkRegisterEmail( EmailRequest request){
        return this.registerService.checkMail(request);
    }
}