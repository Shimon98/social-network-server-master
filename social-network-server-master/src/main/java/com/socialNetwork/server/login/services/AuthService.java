package com.socialNetwork.server.login.services;

import com.socialNetwork.server.login.dataBase.DBManager;
import com.socialNetwork.server.login.entity.RefreshToken;
import com.socialNetwork.server.login.entity.User;
import com.socialNetwork.server.login.hashing.PasswordHashUtil;
import com.socialNetwork.server.login.requests.LoginRequest;
import com.socialNetwork.server.login.requests.RefreshRequest;
import com.socialNetwork.server.login.requests.RegisterRequest;
import com.socialNetwork.server.login.responses.BasicResponse;
import com.socialNetwork.server.login.responses.LoginResponse;
import com.socialNetwork.server.login.responses.RefreshResponse;
import com.socialNetwork.server.login.responses.RegisterResponse;
import com.socialNetwork.server.login.security.JwtService;
import com.socialNetwork.server.login.utils.ConstantLogger;
import com.socialNetwork.server.login.utils.Errors;
import com.socialNetwork.server.login.validators.AuthValidator;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final DBManager dbManager;
    private final JwtService jwtService;

    public AuthService(DBManager dbManager, JwtService jwtService) {
        this.dbManager = dbManager;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {
        Integer validationErrorCode = AuthValidator.validateLoginRequest(request);
        if (validationErrorCode != null) {
            return loginFailure(validationErrorCode);
        }

        String normalizedUsername = request.getUsername().trim();
        String normalizedPassword = request.getPassword().trim();

        try {
            User user = dbManager.findUserByUsername(normalizedUsername);
            if (user == null) {
                return loginFailure(Errors.INVALID_CREDENTIALS);
            }

            if (!isPasswordMatch(normalizedUsername, normalizedPassword, user.getPasswordHash())) {
                return loginFailure(Errors.INVALID_CREDENTIALS);
            }

            String accessToken = jwtService.generateAccessToken((long) user.getId(), user.getUsername());
            String refreshTokenValue = jwtService.generateRefreshToken((long) user.getId(), user.getUsername());

            RefreshToken refreshToken = createRefreshToken(user.getId(), refreshTokenValue);

            dbManager.deleteRefreshTokensByUserId(user.getId());

            boolean saved = dbManager.saveRefreshToken(refreshToken);
            if (!saved) {
                return loginFailure(Errors.INTERNAL_SERVER_ERROR);
            }

            return new LoginResponse(true, null, accessToken, refreshTokenValue);

        } catch (Exception e) {
            logger.error(ConstantLogger.LOG_LOGIN_FAILED_USERNAME, normalizedUsername, e);
            return loginFailure(Errors.INTERNAL_SERVER_ERROR);
        }
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
    public RefreshResponse refreshToken(RefreshRequest request) {

        String token = request.getRefreshToken();

        try {

            if (!jwtService.isTokenValid(token)) {
                return new RefreshResponse(false, Errors.INVALID_TOKEN, null);
            }

            RefreshToken savedToken = dbManager.findRefreshToken(token);

            if (savedToken == null) {
                return new RefreshResponse(false, Errors.INVALID_TOKEN, null);
            }

            Long userId = jwtService.extractUserId(token);
            String username = jwtService.extractUsername(token);

            String newAccessToken = jwtService.generateAccessToken(userId, username);

            return new RefreshResponse(true, null, newAccessToken);

        } catch (Exception e) {

            logger.error(ConstantLogger.LOG_REFRESH_TOKEN_ERROR, e);

            return new RefreshResponse(false, Errors.INTERNAL_SERVER_ERROR, null);
        }
    }

    public BasicResponse logout(RefreshRequest request) {
        boolean deleted = dbManager.deleteRefreshToken(request.getRefreshToken());
        if (!deleted) {
            return new BasicResponse(false, Errors.INVALID_TOKEN);
        }
        return new BasicResponse(true, null);
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

    private RefreshToken createRefreshToken(Integer userId, String tokenValue) {
        long now = System.currentTimeMillis();

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUserId(userId);
        refreshToken.setToken(tokenValue);
        refreshToken.setCreatedAt(now);
        refreshToken.setExpiresAt(jwtService.extractExpirationTime(tokenValue));
        return refreshToken;
    }
}