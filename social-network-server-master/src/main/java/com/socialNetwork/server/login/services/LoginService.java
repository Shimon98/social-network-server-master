
package com.socialNetwork.server.login.services;

import com.socialNetwork.server.login.database.DBManager;
import com.socialNetwork.server.login.email.EmailManager;
import com.socialNetwork.server.login.entity.User;
import com.socialNetwork.server.login.requests.LoginCodeAnswer;
import com.socialNetwork.server.login.requests.LoginCodeRequest;
import com.socialNetwork.server.login.requests.LoginRequest;
import com.socialNetwork.server.login.responses.BasicResponse;
import com.socialNetwork.server.login.responses.LoginResponse;
import com.socialNetwork.server.login.responses.MailResponse;
import com.socialNetwork.server.login.responses.PendingLoginResponse;
import com.socialNetwork.server.login.security.JwtService;
import com.socialNetwork.server.login.utils.ConstantLogger;
import com.socialNetwork.server.login.utils.Errors;
import com.socialNetwork.server.login.validators.AuthValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class LoginService {
    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);

    private DBManager dbManager;
    private TokenService tokenService;
    private AuthCommonService authCommonService;
    private EmailManager emailManager;
    private JwtService jwtService;

    public LoginService(DBManager dbManager, TokenService tokenService, AuthCommonService authCommonService, EmailManager emailManager, JwtService jwtService) {
        this.dbManager = dbManager;
        this.tokenService = tokenService;
        this.authCommonService = authCommonService;
        this.emailManager = emailManager;
        this.jwtService = jwtService;
    }

    public PendingLoginResponse startLogin(LoginRequest request) {
        try {
            User user = getValidUserForLogin(request);
            if (user == null) {
                return new PendingLoginResponse(false, Errors.INVALID_CREDENTIALS, null);
            }
            String pendingLoginToken = jwtService.generatePendingLoginToken((long) user.getId(), user.getUsername(),
                    user.getEmail()
            );
            return new PendingLoginResponse(true, null, pendingLoginToken);
        } catch (Exception e) {
            logger.error(ConstantLogger.LOG_LOGIN_FAILED_USERNAME, request.getUsername(), e);
        }return new PendingLoginResponse(false, Errors.INTERNAL_SERVER_ERROR, null);
    }

    private User getValidUserForLogin(LoginRequest request) {
        Integer validationErrorCode = AuthValidator.validateLoginRequest(request);
        if (validationErrorCode != null) {
            return null;
        }
        String normalizedUsername = authCommonService.normalizeUsername(request.getUsername());
        String normalizedPassword = authCommonService.normalizePassword(request.getPassword());
        User user = dbManager.findUserByUsername(normalizedUsername);
        if (user == null) {
            return null;
        }
        if (!authCommonService.isPasswordMatch(normalizedUsername, normalizedPassword,
                user.getPasswordHash())) {
            return null;
        }
        return user;
    }

    public BasicResponse sendLoginCode(LoginCodeRequest request) {
        if (request== null)
        { return sendCodeFailure(Errors.INVALID_SEND_CODE);}
        User user = authCommonService.getAuthUserFormToken(request.getPendingLoginToken());
        if (user==null) { return sendCodeFailure(Errors.INVALID_SEND_CODE);}
        emailManager.sendLoginCode(user.getEmail());
        String token= request.getPendingLoginToken();
        return new MailResponse(true, null,token);
    }

    private MailResponse sendCodeFailure(Integer errorCode) {
        return new MailResponse(false, errorCode, null);
    }







    public LoginResponse verifyLoginCode(LoginCodeAnswer request) {
        if (request== null)
        {return loginFailure(Errors.INVALID_CREDENTIALS);}
        if (request.getCode() == null) {  return loginFailure(Errors.INVALID_CREDENTIALS);}
        try {
            User user = authCommonService.getAuthUserFormToken(request.getPendingLoginToken());
            if (user==null) {
                return loginFailure(Errors.INVALID_CREDENTIALS);
            }
            boolean validCode = emailManager.verifyLoginCode(user.getEmail(), request.getCode());
            if (!validCode) {
                return loginFailure(Errors.INVALID_CREDENTIALS);
            }
            return tokenService.createLoginTokens(user);

        } catch (Exception e) {
            logger.error("Failed to verify login code", e);
            return loginFailure(Errors.INTERNAL_SERVER_ERROR);
        }
    }

    private LoginResponse loginFailure(Integer errorCode) {
        return new LoginResponse(false, errorCode, null, null);
    }





}