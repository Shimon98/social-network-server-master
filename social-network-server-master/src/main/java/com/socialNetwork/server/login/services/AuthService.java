package com.socialNetwork.server.login.services;

import com.socialNetwork.server.login.dataBase.DBManager;
import com.socialNetwork.server.login.hashing.PasswordHashUtil;
import com.socialNetwork.server.login.model.User;
import com.socialNetwork.server.login.responses.RegisterRequest;
import com.socialNetwork.server.login.responses.RegisterResponse;
import com.socialNetwork.server.login.validators.AuthValidator;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final DBManager dbManager;

    public AuthService(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public RegisterResponse register(RegisterRequest request) {
        String validationError = AuthValidator.validateRegisterRequest(request);

        if (validationError != null) {
            return new RegisterResponse(validationError, false);
        }

        try {
            if (dbManager.userExists(request.getUsername(), request.getEmail())) {
                return new RegisterResponse("One or more of your credentials are wrong", false);
            }

            String hashedPassword = PasswordHashUtil.hashPassword(
                    request.getUsername(),
                    request.getPassword()
            );

            User user = createUserFromRequest(request, hashedPassword);

            boolean inserted = dbManager.createUserOnDb(user);

            if (inserted) {
                return new RegisterResponse("User registered successfully", true);
            }

            return new RegisterResponse("Registration failed", false);

        } catch (Exception e) {
            e.printStackTrace();
            return new RegisterResponse(e.getMessage(), false);
        }
    }

    private User createUserFromRequest(RegisterRequest request, String passwordHash) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordHash);
        return user;
    }
}