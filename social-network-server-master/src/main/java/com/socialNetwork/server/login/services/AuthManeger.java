//
//package com.socialNetwork.server.login.services;
//
//import com.socialNetwork.server.login.requests.LoginRequest;
//import com.socialNetwork.server.login.requests.RegisterRequest;
//import com.socialNetwork.server.login.responses.BasicResponse;
//import com.socialNetwork.server.login.responses.LoginResponse;
//
//import com.socialNetwork.server.login.responses.RegisterResponse;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthManeger {
//
//    private final LoginService loginService;
//    private final RegisterService registerService;
//    private final TokenService tokenService;
//
//    public AuthManeger(LoginService loginService, RegisterService registerService,
//                       TokenService tokenService) {
//        this.loginService = loginService;
//        this.registerService = registerService;
//        this.tokenService = tokenService;
//    }
//
//    public LoginResponse login(LoginRequest request) {
//        return loginService.login(request);
//    }
//
//    public RegisterResponse register(RegisterRequest request) {
//        return registerService.register(request);
//    }
//
//    public String refreshAccessToken(String refreshTokenValue) {
//        return tokenService.refreshAccessToken(refreshTokenValue);
//    }
//
//    public BasicResponse logout(String refreshTokenValue) {
//        return tokenService.logout(refreshTokenValue);
//    }
//}

package com.socialNetwork.server.login.services;

import com.socialNetwork.server.login.requests.EmailRequest;
import com.socialNetwork.server.login.requests.LoginCodeAnswer;
import com.socialNetwork.server.login.requests.LoginRequest;
import com.socialNetwork.server.login.requests.RegisterCodeRequest;
import com.socialNetwork.server.login.requests.RegisterCompleteRequest;
import com.socialNetwork.server.login.responses.BasicResponse;
import com.socialNetwork.server.login.responses.LoginResponse;
import com.socialNetwork.server.login.responses.PendingLoginResponse;
import com.socialNetwork.server.login.responses.RegisterCodeVerifyResponse;
import com.socialNetwork.server.login.responses.RegisterResponse;
import org.springframework.stereotype.Service;

@Service
public class AuthManeger {

    private final LoginService loginService;
    private final RegisterService registerService;
    private final TokenService tokenService;

    public AuthManeger(LoginService loginService,
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
}