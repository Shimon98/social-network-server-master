//package com.socialNetwork.server.login.services;
//
//import com.socialNetwork.server.login.dataBase.DBManager;
//import com.socialNetwork.server.login.entity.User;
//import com.socialNetwork.server.login.hashing.PasswordHashUtil;
//import com.socialNetwork.server.login.requests.RegisterRequest;
//import com.socialNetwork.server.login.responses.RegisterResponse;
//import com.socialNetwork.server.login.utils.ConstantLogger;
//import com.socialNetwork.server.login.utils.Errors;
//import com.socialNetwork.server.login.validators.AuthValidator;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//
//@Service
//public class RegisterService {
//
//    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);
//
//    private final DBManager dbManager;
//
//    public RegisterService(DBManager dbManager) {
//        this.dbManager = dbManager;
//    }
//
//    public RegisterResponse register(RegisterRequest request) {
//        System.out.println("Register request received");
//        Integer validationErrorCode = AuthValidator.validateRegisterRequest(request);
//        if (validationErrorCode != null) {
//            return registerFailure(validationErrorCode);
//        }
//
//        String normalizedUsername = request.getUsername().trim();
//        String normalizedEmail = request.getEmail().trim();
//        String normalizedPassword = request.getPassword().trim();
//
//        try {
//            if (dbManager.userExists(normalizedUsername, normalizedEmail)) {
//                return registerFailure(Errors.USER_ALREADY_EXISTS);
//            }
//
//            String passwordHash = hashPassword(normalizedUsername, normalizedPassword);
//            User user = createUser(normalizedUsername, normalizedEmail, passwordHash);
//
//            boolean inserted = dbManager.createUserOnDb(user);
//            if (!inserted) {
//                return registerFailure(Errors.REGISTRATION_FAILED);
//            }
//
//            return new RegisterResponse(true, null);
//
//        } catch (Exception e) {
//            logger.error(ConstantLogger.LOG_REGISTER_UNEXPECTED_ERROR, normalizedUsername, e);
//            return registerFailure(Errors.INTERNAL_SERVER_ERROR);
//        }
//    }
//
//    private String hashPassword(String username, String password) {
//        return PasswordHashUtil.hashPassword(username, password);
//    }
//
//    private User createUser(String username, String email, String passwordHash) {
//        User user = new User();
//        user.setUsername(username);
//        user.setEmail(email);
//        user.setPasswordHash(passwordHash);
//        return user;
//    }


package com.socialNetwork.server.login.services;

import com.socialNetwork.server.login.dataBase.DBManager;
import com.socialNetwork.server.login.entity.User;
import com.socialNetwork.server.login.requests.RegisterRequest;
import com.socialNetwork.server.login.responses.RegisterResponse;
import com.socialNetwork.server.login.utils.ConstantLogger;
import com.socialNetwork.server.login.utils.Errors;
import com.socialNetwork.server.login.validators.AuthValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {
    private static final Logger logger = LoggerFactory.getLogger(RegisterService.class);

    private  DBManager dbManager;
    private  AuthCommonService authCommonService;

    public RegisterService(DBManager dbManager, AuthCommonService authCommonService) {
        this.dbManager = dbManager;
        this.authCommonService = authCommonService;
    }
    public RegisterResponse register(RegisterRequest request) {
        Integer validationErrorCode = AuthValidator.validateRegisterRequest(request);
        if (validationErrorCode != null) {
            return registerFailure(validationErrorCode);
        }
        String normalizedUsername = authCommonService.normalizeUsername(request.getUsername());
        String normalizedEmail = authCommonService.normalizeEmail(request.getEmail());
        String normalizedPassword = authCommonService.normalizePassword(request.getPassword());
        try {
            if (dbManager.userExists(normalizedUsername, normalizedEmail)) {
                return registerFailure(Errors.USER_ALREADY_EXISTS);
            }
//
            if (!ifInsertedNewUser(normalizedUsername, normalizedEmail, normalizedPassword)) {
                return registerFailure(Errors.REGISTRATION_FAILED);
            }
            return new RegisterResponse(true, null);
        } catch (Exception e) {
            logger.error(ConstantLogger.LOG_REGISTER_UNEXPECTED_ERROR, normalizedUsername, e);
            return registerFailure(Errors.INTERNAL_SERVER_ERROR);
        }
    }

    private Boolean ifInsertedNewUser(String normalizedUsername, String normalizedEmail,  String normalizedPassword) {
        String passwordHash = authCommonService.hashPassword(normalizedUsername, normalizedPassword);
        User user = authCommonService.createUser(normalizedUsername, normalizedEmail, passwordHash);
        return dbManager.createUserOnDb(user);
    }


    private RegisterResponse registerFailure(Integer errorCode) {
        return new RegisterResponse(false, errorCode);
    }
}