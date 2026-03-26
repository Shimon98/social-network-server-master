package com.socialNetwork.server.auth.database;

import com.socialNetwork.server.auth.utils.ConstantLogger;
import com.socialNetwork.server.dashboard.responses.PostResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostRepository {

    private static final Logger logger = LoggerFactory.getLogger(PostRepository.class);

    private final DatabaseConnectionProvider connectionProvider;

    public PostRepository(DatabaseConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    @CacheEvict(value = "userPosts", key = "#userId")
    public boolean createPost(Long userId, String content) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlQueries.CREATE_POST)) {

            statement.setLong(1, userId);
            statement.setString(2, content);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return false;
        }
    }

    public List<PostResponse> getFeedPosts(Long currentUserId) {
        List<PostResponse> posts = new ArrayList<>();

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlQueries.GET_FEED_POSTS)) {

            statement.setLong(1, currentUserId);
            statement.setLong(2, currentUserId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                posts.add(RowMappers.mapPost(resultSet));
            }

            return posts;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return posts;
        }
    }

    @CacheEvict(value = "userPosts", key = "#currentUserId")
    public boolean deletePost(Long postId, Long currentUserId) {
        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlQueries.DELETE_POST)) {

            statement.setLong(1, postId);
            statement.setLong(2, currentUserId);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return false;
        }
    }

    @Cacheable(value = "userPosts", key = "#userId", unless = "#result == null")
    public List<PostResponse> getPostsByUserId(Long userId) {
        List<PostResponse> posts = new ArrayList<>();

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement statement = connection.prepareStatement(SqlQueries.GET_POSTS_BY_USER_ID)) {

            statement.setLong(1, userId);

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                posts.add(RowMappers.mapPost(resultSet));
            }

            return posts;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
            return null;
        }
    }
}