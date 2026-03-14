//package com.socialNetwork.server.login.services;
//
//import com.socialNetwork.server.login.dataBase.DBManager;
//import com.socialNetwork.server.login.entity.User;
//import com.socialNetwork.server.login.hashing.PasswordHashUtil;
//import com.socialNetwork.server.login.requests.LoginRequest;
//import com.socialNetwork.server.login.responses.BasicResponse;
//import com.socialNetwork.server.login.responses.LoginResponse;
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
//    private final DBManager dbManager;
//    private final TokenService tokenService;
//
//    public LoginService(DBManager dbManager, TokenService tokenService) {
//        this.dbManager = dbManager;
//        this.tokenService = tokenService;
//    }
//
//    public LoginResponse login(LoginRequest request) {
//        Integer validationErrorCode = AuthValidator.validateLoginRequest(request);
//        if (validationErrorCode != null) {
//            return loginFailure(validationErrorCode);
//        }
//
//        String normalizedUsername = request.getUsername().trim();
//        String normalizedPassword = request.getPassword().trim();
//
//        try {
//            User user = dbManager.findUserByUsername(normalizedUsername);
//            if (user == null) {
//                return loginFailure(Errors.INVALID_CREDENTIALS);
//            }
//
//            if (!isPasswordMatch(normalizedUsername, normalizedPassword, user.getPasswordHash())) {
//                return loginFailure(Errors.INVALID_CREDENTIALS);
//            }
//
//            return tokenService.createLoginTokens(user);
//
//        } catch (Exception e) {
//            logger.error(ConstantLogger.LOG_LOGIN_FAILED_USERNAME, normalizedUsername, e);
//            return loginFailure(Errors.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    private boolean isPasswordMatch(String username, String password, String savedPasswordHash) {
//        String requestPasswordHash = PasswordHashUtil.hashPassword(username, password);
//        return requestPasswordHash.equals(savedPasswordHash);
//    }



package com.socialNetwork.server.login.services;

import com.socialNetwork.server.login.dataBase.DBManager;
import com.socialNetwork.server.login.entity.User;
import com.socialNetwork.server.login.requests.LoginRequest;
import com.socialNetwork.server.login.responses.LoginResponse;

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
    private  TokenService tokenService;
    private AuthCommonService authCommonService;

    public LoginService(DBManager dbManager, TokenService tokenService, AuthCommonService authCommonService) {
        this.dbManager = dbManager;
        this.tokenService = tokenService;
        this.authCommonService = authCommonService;
    }

    public LoginResponse login(LoginRequest request) {
        Integer validationErrorCode = AuthValidator.validateLoginRequest(request);
        if (validationErrorCode != null) {
            return loginFailure(validationErrorCode);
        }
        String normalizedUsername = authCommonService.normalizeUsername(request.getUsername());
        String normalizedPassword = authCommonService.normalizePassword(request.getPassword());
        try {
            User user = dbManager.findUserByUsername(normalizedUsername);
            if (user == null) {
                return loginFailure(Errors.INVALID_CREDENTIALS);
            }
            if (!authCommonService.isPasswordMatch(normalizedUsername,
                    normalizedPassword, user.getPasswordHash())) {
                return loginFailure(Errors.INVALID_CREDENTIALS);
            }
            return tokenService.createLoginTokens(user);

        } catch (Exception e) {
            logger.error(ConstantLogger.LOG_LOGIN_FAILED_USERNAME, normalizedUsername, e);
            return loginFailure(Errors.INTERNAL_SERVER_ERROR);
        }
    }


    private LoginResponse loginFailure(Integer errorCode) {
        return new LoginResponse(false, errorCode, null, null);
    }
}