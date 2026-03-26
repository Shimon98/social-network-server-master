package com.socialNetwork.server.auth.database;

import com.socialNetwork.server.auth.entity.RefreshToken;
import com.socialNetwork.server.auth.entity.User;
import com.socialNetwork.server.auth.utils.ConstantLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private final DatabaseConnectionProvider connectionProvider;

    public UserRepository(DatabaseConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public boolean createUser(User user) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlQueries.INSERT_USER)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return false;
        }
    }

    public boolean userExists(String username, String email) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlQueries.USER_EXISTS_BY_USERNAME_OR_EMAIL)) {
            statement.setString(1, username);
            statement.setString(2, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }
            return false;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return false;
        }
    }

    public User findUserByUsername(String username) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlQueries.FIND_USER_BY_USERNAME)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return RowMappers.mapFullUser(resultSet);
            }
            return null;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return null;
        }
    }

    public User findUserById(Long userId) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlQueries.FIND_USER_BY_ID)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return RowMappers.mapFullUser(resultSet);
            }
            return null;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return null;
        }
    }

    public List<User> searchUsersByUsername(Long currentUserId, String text) {
        List<User> users = new ArrayList<>();
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlQueries.SEARCH_USERS_BY_USERNAME)) {
            statement.setString(1, "%" + text + "%");
            statement.setLong(2, currentUserId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.add(RowMappers.mapUserPreview(resultSet));
            }
            return users;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return users;
        }
    }

    public boolean updateProfileImage(Long userId, String profileImageUrl) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlQueries.UPDATE_PROFILE_IMAGE)) {
            statement.setString(1, profileImageUrl);
            statement.setLong(2, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return false;
        }
    }

    public boolean userExistsById(Long userId) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlQueries.USER_EXISTS_BY_ID)) {
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return false;
        }
    }

    public boolean userExistsByEmail(String email) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlQueries.USER_EXISTS_BY_EMAIL)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return false;
        }
    }
}
