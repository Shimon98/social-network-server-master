package com.socialNetwork.server.login.services;

import com.socialNetwork.server.login.dataBase.DBManager;
import com.socialNetwork.server.login.hashing.PasswordHashUtil;
import com.socialNetwork.server.login.entity.User;
import com.socialNetwork.server.login.requests.LoginRequest;
import com.socialNetwork.server.login.requests.RegisterRequest;
import com.socialNetwork.server.login.responses.LoginResponse;
import com.socialNetwork.server.login.responses.RegisterResponse;
import com.socialNetwork.server.login.validators.AuthValidator;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final DBManager dbManager;

    public AuthService(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public LoginResponse login(LoginRequest request) {
        String validationError = AuthValidator.validateLoginRequest(request);

        if (validationError != null) {
            return new LoginResponse(false,
                    validationError,
                    null, null);
        }
        User user = dbManager.findUserByUsername(request.getUsername());

        if (user == null) {
            return new LoginResponse(false, "One or more of your credentials is wrong", null, null); //מטעמי אבטחה אני לא אומרת שהמשתמש לא קיים אני אומרת שאחד או יותר מהפרטים שהוקלדו שגויים
        }
        boolean passwordMatches = PasswordHashUtil.hashPassword(
                request.getUsername(),
                request.getPassword()
        ).equals(user.getPasswordHash());

        if (!passwordMatches){
            return new LoginResponse(false,"One or more of your credentials is wrong", null,null);
        }
        String accessToken = "";
        String refreshToken = ""; //שמתי את זה פה כדי לזכור שבעתיד צריך לגנרט את שתי אלו

        return new LoginResponse(true, "Login success" , accessToken, refreshToken);
    }

    public RegisterResponse register(RegisterRequest request) {
        String validationError = AuthValidator.validateRegisterRequest(request);

        if (validationError != null) {
            return new RegisterResponse(validationError, false);
        }

        try {
            if (dbManager.userExists(request.getUsername(), request.getEmail())) {
                return new RegisterResponse("User exists", false);
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