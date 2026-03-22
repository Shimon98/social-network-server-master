//
//package com.socialNetwork.server.login.services;
//
//import com.socialNetwork.server.login.dataBase.DBManager;
//import com.socialNetwork.server.login.entity.User;
//import com.socialNetwork.server.login.requests.LoginRequest;
//import com.socialNetwork.server.login.responses.LoginResponse;
//
//import com.socialNetwork.server.login.utils.ConstantLogger;
//import com.socialNetwork.server.login.utils.Errors;
//import com.socialNetwork.server.login.validators.AuthValidator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//@Service
//public class LoginService {
//
//    private static final Logger logger = LoggerFactory.getLogger(LoginService.class);
//
//    private DBManager dbManager;
//    private  TokenService tokenService;
//    private AuthCommonService authCommonService;
//
//    public LoginService(DBManager dbManager, TokenService tokenService, AuthCommonService authCommonService) {
//        this.dbManager = dbManager;
//        this.tokenService = tokenService;
//        this.authCommonService = authCommonService;
//    }
//
//    public LoginResponse login(LoginRequest request) {
//        Integer validationErrorCode = AuthValidator.validateLoginRequest(request);
//        if (validationErrorCode != null) {
//            return loginFailure(validationErrorCode);
//        }
//        String normalizedUsername = authCommonService.normalizeUsername(request.getUsername());
//        String normalizedPassword = authCommonService.normalizePassword(request.getPassword());
//        try {
//            User user = dbManager.findUserByUsername(normalizedUsername);
//            if (user == null) {
//                return loginFailure(Errors.INVALID_CREDENTIALS);
//            }
//            if (!authCommonService.isPasswordMatch(normalizedUsername,
//                    normalizedPassword, user.getPasswordHash())) {
//                return loginFailure(Errors.INVALID_CREDENTIALS);
//            }
//            return tokenService.createLoginTokens(user);// פה צריך לשנות את במקום לשלוח טוקן צריך לשלוח הודעה שנישלח קוד למייל וטוקן זמני
//
//        } catch (Exception e) {
//            logger.error(ConstantLogger.LOG_LOGIN_FAILED_USERNAME, normalizedUsername, e);
//            return loginFailure(Errors.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    // לייצר מתודה שמקבלת קוד אימות וטוקן זמני מהצד לקוח  ואז היא עושה את הבדיקות עם איימיל מנגר ואז עם כן מחזירה את הטוקנים התקינים
//
//
//    private LoginResponse loginFailure(Integer errorCode) {
//        return new LoginResponse(false, errorCode, null, null);
//    }
//}

package com.socialNetwork.server.login.services;

import com.socialNetwork.server.login.dataBase.DBManager;
import com.socialNetwork.server.login.email.EmailManager;
import com.socialNetwork.server.login.entity.User;
import com.socialNetwork.server.login.requests.LoginCodeAnswer;
import com.socialNetwork.server.login.requests.LoginRequest;
import com.socialNetwork.server.login.responses.LoginResponse;
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
            emailManager.sendLoginCode(user.getEmail());
            String pendingLoginToken = jwtService.generatePendingLoginToken(
                    (long) user.getId(),
                    user.getUsername(),
                    user.getEmail()
            );
            return new PendingLoginResponse(true, null, pendingLoginToken);

        } catch (Exception e) {
            logger.error(ConstantLogger.LOG_LOGIN_FAILED_USERNAME, request.getUsername(), e);
            return new PendingLoginResponse(false, Errors.INTERNAL_SERVER_ERROR, null);
        }
    }

    public LoginResponse verifyLoginCode(LoginCodeAnswer request) {
        try {
            if (request == null || request.getPendingLoginToken() == null || request.getCode() == null) {
                return loginFailure(Errors.INVALID_CREDENTIALS);
            }
            if (!jwtService.isTokenValid(request.getPendingLoginToken())) {
                return loginFailure(Errors.INVALID_CREDENTIALS);
            }
            if (!"pending_login".equals(jwtService.extractTokenType(request.getPendingLoginToken()))) {
                return loginFailure(Errors.INVALID_CREDENTIALS);
            }
            String usernameFromToken = jwtService.extractUsername(request.getPendingLoginToken());
            String emailFromToken = jwtService.extractEmail(request.getPendingLoginToken());
            String normalizedUsername = authCommonService.normalizeUsername(usernameFromToken);
            User user = dbManager.findUserByUsername(normalizedUsername);
            if (user == null) {
                return loginFailure(Errors.INVALID_CREDENTIALS);
            }
            if (!emailFromToken.equalsIgnoreCase(user.getEmail())) {
                return loginFailure(Errors.INVALID_CREDENTIALS);
            }
            boolean validCode = emailManager.verifyLoginCode(emailFromToken, request.getCode());
            if (!validCode) {
                return loginFailure(Errors.INVALID_CREDENTIALS);
            }
            return tokenService.createLoginTokens(user);

        } catch (Exception e) {
            logger.error("Failed to verify login code", e);
            return loginFailure(Errors.INTERNAL_SERVER_ERROR);
        }
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

    private LoginResponse loginFailure(Integer errorCode) {
        return new LoginResponse(false, errorCode, null, null);
    }
}