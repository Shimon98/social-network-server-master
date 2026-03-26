package com.socialNetwork.server.auth.database;

import com.socialNetwork.server.auth.entity.RefreshToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class RefreshTokenRepository extends BaseRepository {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenRepository.class);

    public RefreshTokenRepository(DatabaseConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    public boolean saveRefreshToken(RefreshToken refreshToken) {
        return executeUpdate(
                logger,
                SqlQueries.INSERT_REFRESH_TOKEN,
                refreshToken.getUserId(),
                refreshToken.getToken(),
                refreshToken.getExpiresAt(),
                refreshToken.getCreatedAt()
        );
    }

    public boolean deleteRefreshTokensByUserId(Integer userId) {
        return executeUpdate(logger, SqlQueries.DELETE_REFRESH_TOKENS_BY_USER_ID, userId);
    }

    public RefreshToken findRefreshToken(String token) {
        return queryOne(
                logger,
                SqlQueries.FIND_REFRESH_TOKEN,
                RowMappers::mapRefreshToken,
                token
        );
    }

    public boolean deleteRefreshToken(String token) {
        return executeUpdate(logger, SqlQueries.DELETE_REFRESH_TOKEN, token);
    }
}