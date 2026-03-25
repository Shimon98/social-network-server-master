package com.socialNetwork.server.auth.services;

import com.socialNetwork.server.auth.database.DBManager;
import com.socialNetwork.server.auth.entity.RefreshToken;
import com.socialNetwork.server.auth.entity.User;
import com.socialNetwork.server.auth.responses.BasicResponse;
import com.socialNetwork.server.auth.responses.LoginResponse;
import com.socialNetwork.server.auth.security.JwtService;
import com.socialNetwork.server.auth.utils.ConstantLogger;
import com.socialNetwork.server.auth.utils.Constants;
import com.socialNetwork.server.auth.utils.ErrorCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private static final Logger logger = LoggerFactory.getLogger(TokenService.class);

    private final DBManager dbManager;
    private final JwtService jwtService;

    public TokenService(DBManager dbManager, JwtService jwtService) {
        this.dbManager = dbManager;
        this.jwtService = jwtService;
    }

    public LoginResponse createLoginTokens(User user) {
        try {
            String accessToken = jwtService.generateAccessToken((long) user.getId(), user.getUsername());
            String refreshTokenValue = jwtService.generateRefreshToken((long) user.getId(), user.getUsername());

            RefreshToken refreshToken = createRefreshToken((int) user.getId(), refreshTokenValue);

            dbManager.deleteRefreshTokensByUserId((int) user.getId());

            boolean saved = dbManager.saveRefreshToken(refreshToken);
            if (!saved) {
                return new LoginResponse(false, ErrorCodes.INTERNAL_SERVER_ERROR, null, null);
            }

            return new LoginResponse(true, null, accessToken, refreshTokenValue);

        } catch (Exception e) {
            logger.error(ConstantLogger.LOG_REFRESH_TOKEN_ERROR, e);
            return new LoginResponse(false, ErrorCodes.INTERNAL_SERVER_ERROR, null, null);
        }
    }

    public String refreshAccessToken(String refreshTokenValue) {
        try {
            if (refreshTokenValue == null || refreshTokenValue.isBlank()) {
                return null;
            }

            if (!jwtService.isTokenValid(refreshTokenValue)) {
                return null;
            }

            RefreshToken savedToken = dbManager.findRefreshToken(refreshTokenValue);
            if (savedToken == null) {
                return null;
            }

            if (!Constants.REFRESH.equals(jwtService.extractTokenType(refreshTokenValue))) {
                return null;
            }

            Long userId = jwtService.extractUserId(refreshTokenValue);
            String username = jwtService.extractUsername(refreshTokenValue);

            return jwtService.generateAccessToken(userId, username);

        } catch (Exception e) {
            logger.error(ConstantLogger.LOG_REFRESH_TOKEN_ERROR, e);
            return null;
        }
    }

    public BasicResponse logout(String refreshTokenValue) {
        if (refreshTokenValue == null || refreshTokenValue.isBlank()) {
            return new BasicResponse(true, null);
        }

        boolean deleted = dbManager.deleteRefreshToken(refreshTokenValue);
        if (!deleted) {
            return new BasicResponse(true, null);
        }

        return new BasicResponse(true, null);
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