package com.socialNetwork.server.login.dataBase;

import com.socialNetwork.server.login.config.DatabaseConfig;
import com.socialNetwork.server.login.entity.User;
import com.socialNetwork.server.login.services.AuthService;
import com.socialNetwork.server.login.utils.ConstantLogger;
import com.socialNetwork.server.login.utils.Constants;
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
            logger.error(ConstantLogger.LOG_DB_FAILED_CONNECTED,e.getMessage(),e);
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
}
