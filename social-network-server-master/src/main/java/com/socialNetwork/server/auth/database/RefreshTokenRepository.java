package com.socialNetwork.server.auth.database;

import com.socialNetwork.server.auth.entity.RefreshToken;
import com.socialNetwork.server.auth.utils.ConstantLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Repository
public class RefreshTokenRepository {

    private static final Logger logger = LoggerFactory.getLogger(RefreshTokenRepository.class);

    private final DatabaseConnectionProvider connectionProvider;

    public RefreshTokenRepository(DatabaseConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public boolean saveRefreshToken(RefreshToken refreshToken) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlQueries.INSERT_REFRESH_TOKEN)) {

            statement.setInt(1, refreshToken.getUserId());
            statement.setString(2, refreshToken.getToken());
            statement.setLong(3, refreshToken.getExpiresAt());
            statement.setLong(4, refreshToken.getCreatedAt());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return false;
        }
    }

    public boolean deleteRefreshTokensByUserId(Integer userId) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlQueries.DELETE_REFRESH_TOKENS_BY_USER_ID)) {

            statement.setInt(1, userId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return false;
        }
    }

    public RefreshToken findRefreshToken(String token) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlQueries.FIND_REFRESH_TOKEN)) {

            statement.setString(1, token);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return RowMappers.mapRefreshToken(resultSet);
            }

            return null;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return null;
        }
    }

    public boolean deleteRefreshToken(String token) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlQueries.DELETE_REFRESH_TOKEN)) {

            statement.setString(1, token);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return false;
        }
    }
}