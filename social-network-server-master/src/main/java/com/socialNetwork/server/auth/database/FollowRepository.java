package com.socialNetwork.server.auth.database;

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
public class FollowRepository {

    private static final Logger logger = LoggerFactory.getLogger(FollowRepository.class);

    private final DatabaseConnectionProvider connectionProvider;

    public FollowRepository(DatabaseConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public boolean isFollowing(Long followerId, Long followedId) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlQueries.IS_FOLLOWING)) {

            statement.setLong(1, followerId);
            statement.setLong(2, followedId);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return false;
        }
    }

    public boolean createFollow(Long followerId, Long followedId) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlQueries.CREATE_FOLLOW)) {

            statement.setLong(1, followerId);
            statement.setLong(2, followedId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return false;
        }
    }

    public boolean removeFollow(Long followerId, Long followedId) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlQueries.REMOVE_FOLLOW)) {

            statement.setLong(1, followerId);
            statement.setLong(2, followedId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return false;
        }
    }

    public List<User> getFollowingUsers(Long currentUserId) {
        List<User> users = new ArrayList<>();

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlQueries.GET_FOLLOWING_USERS)) {

            statement.setLong(1, currentUserId);

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
}