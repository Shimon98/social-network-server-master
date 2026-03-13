package com.socialNetwork.server.login.dataBase;

import com.socialNetwork.server.login.config.DatabaseConfig;
import com.socialNetwork.server.login.entity.RefreshToken;
import com.socialNetwork.server.login.entity.User;
import com.socialNetwork.server.login.utils.ConstantLogger;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DBManager {
    private static final Logger logger = LoggerFactory.getLogger(DBManager.class);

    private Connection connection;

    @PostConstruct
    public void connect() {
        try {
            this.connection = DatabaseConfig.getConnection();
            logger.info(ConstantLogger.LOG_DB_CONNECTED);
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_FAILED_CONNECTED, e.getMessage(), e);
        }
    }

    public boolean createUserOnDb(User user) {
        String sql = "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)";
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage());
        }
        return false;
    }

    public boolean userExists(String username, String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? OR email = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage());
        }
        return false;
    }

    public User findUserByUsername(String username) {
        String sql = "SELECT id, username, email, password_hash FROM users WHERE username = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPasswordHash(resultSet.getString("password_hash"));
                return user;
            }
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage());
        }
        return null;
    }

    public boolean saveRefreshToken(RefreshToken refreshToken) {
        String sql = "INSERT INTO refresh_tokens (user_id, token, expires_at, created_at) VALUES (?, ?, ?, ?)";
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setInt(1, refreshToken.getUserId());
            statement.setString(2, refreshToken.getToken());
            statement.setLong(3, refreshToken.getExpiresAt());
            statement.setLong(4, refreshToken.getCreatedAt());

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage());
        }
        return false;
    }

    public boolean deleteRefreshTokensByUserId(Integer userId) {
        String sql = "DELETE FROM refresh_tokens WHERE user_id = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage());
        }
        return false;
    }

    public RefreshToken findRefreshToken(String token) {
        String sql = "SELECT id, user_id, token, expires_at, created_at FROM refresh_tokens WHERE token = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, token);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                RefreshToken refreshToken = new RefreshToken();
                refreshToken.setId(resultSet.getInt("id"));
                refreshToken.setUserId(resultSet.getInt("user_id"));
                refreshToken.setToken(resultSet.getString("token"));
                refreshToken.setExpiresAt(resultSet.getLong("expires_at"));
                refreshToken.setCreatedAt(resultSet.getLong("created_at"));
                return refreshToken;
            }
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage());
        }
        return null;
    }

    public boolean deleteRefreshToken(String token) {
        String sql = "DELETE FROM refresh_tokens WHERE token = ?";
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, token);
            int rows = statement.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage());
        }
        return false;
    }
}