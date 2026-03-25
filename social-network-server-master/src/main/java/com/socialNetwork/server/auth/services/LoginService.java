
package com.socialNetwork.server.auth.services;

import com.socialNetwork.server.auth.database.DBManager;
import com.socialNetwork.server.auth.email.EmailManager;
import com.socialNetwork.server.auth.entity.User;
import com.socialNetwork.server.auth.requests.LoginCodeAnswer;
import com.socialNetwork.server.auth.requests.LoginCodeRequest;
import com.socialNetwork.server.auth.requests.LoginRequest;
import com.socialNetwork.server.auth.responses.BasicResponse;
import com.socialNetwork.server.auth.responses.LoginResponse;
import com.socialNetwork.server.auth.responses.MailResponse;
import com.socialNetwork.server.auth.responses.PendingLoginResponse;
import com.socialNetwork.server.auth.security.JwtService;
import com.socialNetwork.server.auth.utils.ConstantLogger;
import com.socialNetwork.server.auth.utils.ErrorCodes;
import com.socialNetwork.server.auth.validators.AuthValidator;
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
                return new PendingLoginResponse(false, ErrorCodes.INVALID_CREDENTIALS, null);
            }
            String pendingLoginToken = jwtService.generatePendingLoginToken((long) user.getId(), user.getUsername(),
                    user.getEmail()
            );
            return new PendingLoginResponse(true, null, pendingLoginToken);
        } catch (Exception e) {
            logger.error(ConstantLogger.LOG_LOGIN_FAILED_USERNAME, request.getUsername(), e);
        }
        return new PendingLoginResponse(false, ErrorCodes.INTERNAL_SERVER_ERROR, null);
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
        if (request == null) {
            return sendCodeFailure(ErrorCodes.INVALID_SEND_CODE);
        }
        User user = authCommonService.getAuthUserFormToken(request.getTempToken());
        if (user == null) {
            return sendCodeFailure(ErrorCodes.INVALID_SEND_CODE);
        }
        emailManager.sendLoginCode(user.getEmail());
        String token = request.getTempToken();
        return new MailResponse(true, null, token);
    }

    private MailResponse sendCodeFailure(Integer errorCode) {
        return new MailResponse(false, errorCode, null);
    }


    public LoginResponse verifyLoginCode(LoginCodeAnswer request) {
        if (request == null) {
            return loginFailure(ErrorCodes.INVALID_CREDENTIALS);
        }
        if (request.getCode() == null) {
            return loginFailure(ErrorCodes.INVALID_CREDENTIALS);
        }
        try {
            User user = authCommonService.getAuthUserFormToken(request.getTempToken());
            if (user == null) {
                return loginFailure(ErrorCodes.INVALID_CREDENTIALS);
            }
            boolean validCode = emailManager.verifyLoginCode(user.getEmail(), request.getCode());
            if (!validCode) {
                return loginFailure(ErrorCodes.INVALID_CREDENTIALS);
            }
            return tokenService.createLoginTokens(user);

        } catch (Exception e) {
            logger.error("Failed to verify login code", e);
            return loginFailure(ErrorCodes.INTERNAL_SERVER_ERROR);
        }
    }

    private LoginResponse loginFailure(Integer errorCode) {
        return new LoginResponse(false, errorCode, null, null);
    }


}