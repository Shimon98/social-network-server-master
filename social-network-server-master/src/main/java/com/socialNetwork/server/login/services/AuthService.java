package com.socialNetwork.server.login.services;

import com.socialNetwork.server.login.dataBase.DBManager;
import com.socialNetwork.server.login.entity.User;
import com.socialNetwork.server.login.hashing.PasswordHashUtil;
import com.socialNetwork.server.login.requests.LoginRequest;
import com.socialNetwork.server.login.requests.RegisterRequest;
import com.socialNetwork.server.login.responses.LoginResponse;
import com.socialNetwork.server.login.responses.RegisterResponse;
import com.socialNetwork.server.login.utils.Errors;
import com.socialNetwork.server.login.validators.AuthValidator;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final DBManager dbManager;

    public AuthService(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public LoginResponse login(LoginRequest request) {
        Integer validationErrorCode = AuthValidator.validateLoginRequest(request);
        if (validationErrorCode != null) {
            return loginFailure(validationErrorCode);
        }

        User user = dbManager.findUserByUsername(request.getUsername());
        if (user == null) {
            return loginFailure(Errors.INVALID_CREDENTIALS);
        }

        boolean passwordMatches = PasswordHashUtil.hashPassword(
                request.getUsername(),
                request.getPassword()
        ).equals(user.getPasswordHash());

        if (!passwordMatches) {
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

        try {
            if (dbManager.userExists(request.getUsername(), request.getEmail())) {
                return registerFailure(Errors.USER_ALREADY_EXISTS);
            }

            String hashedPassword = PasswordHashUtil.hashPassword(
                    request.getUsername(),
                    request.getPassword()
            );

            User user = createUserFromRequest(request, hashedPassword);
            boolean inserted = dbManager.createUserOnDb(user);

            if (!inserted) {
                return registerFailure(Errors.REGISTRATION_FAILED);
            }

            return new RegisterResponse(true, null);

        } catch (Exception e) {
            e.printStackTrace();
            return registerFailure(Errors.INTERNAL_SERVER_ERROR);
        }
    }





    private LoginResponse loginFailure(Integer errorCode) {
        return new LoginResponse(false, errorCode, null, null);
    }

    private RegisterResponse registerFailure(Integer errorCode) {
        return new RegisterResponse(false, errorCode);
    }

    private User createUserFromRequest(RegisterRequest request, String passwordHash) {
        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setEmail(request.getEmail().trim());
        user.setPasswordHash(passwordHash);
        return user;
    }
}