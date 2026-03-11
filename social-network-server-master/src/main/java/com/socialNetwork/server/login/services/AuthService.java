package com.socialNetwork.server.login.services;

import com.socialNetwork.server.login.dataBase.DBManager;
import com.socialNetwork.server.login.entity.User;
import com.socialNetwork.server.login.hashing.PasswordHashUtil;
import com.socialNetwork.server.login.requests.LoginRequest;
import com.socialNetwork.server.login.requests.RegisterRequest;
import com.socialNetwork.server.login.responses.LoginResponse;
import com.socialNetwork.server.login.responses.RegisterResponse;
import com.socialNetwork.server.login.utils.ConstantLogger;
import com.socialNetwork.server.login.utils.Constants;
import com.socialNetwork.server.login.utils.Errors;
import com.socialNetwork.server.login.validators.AuthValidator;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final DBManager dbManager;


    public AuthService(DBManager dbManager) {
        this.dbManager = dbManager;
    }


    public LoginResponse login(LoginRequest request) {
        Integer validationErrorCode = AuthValidator.validateLoginRequest(request);
        if (validationErrorCode != null) {
            return loginFailure(validationErrorCode);
        }
        String normalizedUsername = request.getUsername().trim();
        String normalizedPassword = request.getPassword().trim();
        User user = dbManager.findUserByUsername(normalizedUsername);
        if (user == null) {
            return loginFailure(Errors.INVALID_CREDENTIALS);
        }
        if (!isPasswordMatch(normalizedUsername, normalizedPassword, user.getPasswordHash())) {
            return loginFailure(Errors.INVALID_CREDENTIALS);
        }
        String accessToken = "VVVVV";
        String refreshToken = "TODO_REFRESH_TOKEN";
        return new LoginResponse(true, null, accessToken, refreshToken);
    }


    public RegisterResponse register(RegisterRequest request) {
        Integer validationErrorCode = AuthValidator.validateRegisterRequest(request);
        if (validationErrorCode != null) {
            return registerFailure(validationErrorCode);
        }
        String normalizedUsername = request.getUsername().trim();
        String normalizedEmail = request.getEmail().trim();
        String normalizedPassword = request.getPassword().trim();
        try {
            if (dbManager.userExists(normalizedUsername, normalizedEmail)) {
                return registerFailure(Errors.USER_ALREADY_EXISTS);
            }
            String passwordHash = hashPassword(normalizedUsername, normalizedPassword);
            User user = createUser(normalizedUsername, normalizedEmail, passwordHash);
            boolean inserted = dbManager.createUserOnDb(user);
            if (!inserted) {
                return registerFailure(Errors.REGISTRATION_FAILED);
            }
            return new RegisterResponse(true, null);
        } catch (Exception e) {
            logger.error(ConstantLogger.LOG_REGISTER_UNEXPECTED_ERROR, normalizedUsername, e);
            return registerFailure(Errors.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean isPasswordMatch(String username, String password, String savedPasswordHash) {
        String requestPasswordHash = hashPassword(username, password);
        return requestPasswordHash.equals(savedPasswordHash);
    }

    private String hashPassword(String username, String password) {
        return PasswordHashUtil.hashPassword(username, password);
    }

    private LoginResponse loginFailure(Integer errorCode) {
        return new LoginResponse(false, errorCode, null, null);
    }

    private RegisterResponse registerFailure(Integer errorCode) {
        return new RegisterResponse(false, errorCode);
    }

    private User createUser(String username, String email, String passwordHash) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        return user;
    }
}