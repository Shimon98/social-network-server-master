package com.socialNetwork.server.auth.database;

import com.socialNetwork.server.auth.config.DatabaseConfig;
import com.socialNetwork.server.auth.entity.RefreshToken;
import com.socialNetwork.server.auth.entity.User;
import com.socialNetwork.server.auth.utils.ConstantLogger;
import com.socialNetwork.server.dashboard.responses.PostResponse;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
        String sql = "SELECT id, username, email, password_hash, profile_image_url FROM users WHERE username = ?";
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
                user.setProfileImageUrl(resultSet.getString("profile_image_url"));
                return user;
            }
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
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

    public User findUserById(Long userId) {
        String sql = "SELECT id, username, email, password_hash, profile_image_url FROM users WHERE id = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setLong(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setEmail(resultSet.getString("email"));
                user.setPasswordHash(resultSet.getString("password_hash"));
                user.setProfileImageUrl(resultSet.getString("profile_image_url"));
                return user;
            }
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
        }
        return null;
    }

    public List<User> searchUsersByUsername(Long currentUserId, String text) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT id, username, profile_image_url " +
                "FROM users " +
                "WHERE username LIKE ? AND id <> ? " +
                "LIMIT 20";
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setString(1, "%" + text + "%");
            statement.setLong(2, currentUserId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setProfileImageUrl(resultSet.getString("profile_image_url"));
                users.add(user);
            }
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
        }

        return users;
    }

    public boolean updateProfileImage(Long userId, String profileImageUrl) {
        String sql = "UPDATE users SET profile_image_url = ? WHERE id = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setString(1, profileImageUrl);
            statement.setLong(2, userId);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
        }
        return false;
    }

    public boolean isFollowing(Long followerId, Long followedId) {
        String sql = "SELECT 1 FROM follows WHERE follower_id = ? AND followed_id = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setLong(1, followerId);
            statement.setLong(2, followedId);

            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
        }
        return false;
    }

    public boolean createFollow(Long followerId, Long followedId) {
        String sql = "INSERT INTO follows (follower_id, followed_id) VALUES (?, ?)";
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setLong(1, followerId);
            statement.setLong(2, followedId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
        }
        return false;
    }

    public List<User> getFollowingUsers(Long currentUserId) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT u.id, u.username, u.profile_image_url " +
                "FROM follows f " +
                "JOIN users u ON f.followed_id = u.id " +
                "WHERE f.follower_id = ?";

        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setLong(1, currentUserId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setUsername(resultSet.getString("username"));
                user.setProfileImageUrl(resultSet.getString("profile_image_url"));
                users.add(user);
            }
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
        }

        return users;
    }

    public boolean createPost(Long userId, String content) {
        String sql = "INSERT INTO posts (user_id, content) VALUES (?, ?)";
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setLong(1, userId);
            statement.setString(2, content);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
        }
        return false;
    }

    public List<PostResponse> getFeedPosts(Long currentUserId) {
        List<PostResponse> posts = new ArrayList<>();
        String sql = "SELECT p.id, p.content, p.created_at, u.username, u.profile_image_url " +
                "FROM posts p " +
                "JOIN users u ON p.user_id = u.id " +
                "WHERE p.user_id = ? " +
                "   OR p.user_id IN ( " +
                "       SELECT followed_id " +
                "       FROM follows " +
                "       WHERE follower_id = ? " +
                "   ) " +
                "ORDER BY p.created_at DESC " +
                "LIMIT 20";
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setLong(1, currentUserId);
            statement.setLong(2, currentUserId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PostResponse post = new PostResponse();
                post.setId(resultSet.getLong("id"));
                post.setContent(resultSet.getString("content"));
                post.setCreatedAt(resultSet.getTimestamp("created_at"));
                post.setUsername(resultSet.getString("username"));
                post.setProfilePicture(resultSet.getString("profile_image_url"));
                post.setSuccess(true);
                post.setErrorCode(null);
                posts.add(post);
            }
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
        }
        return posts;
    }

    public boolean removeFollow(Long followerId, Long followedId) {
        String sql = "DELETE FROM follows WHERE follower_id = ? AND followed_id = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setLong(1, followerId);
            statement.setLong(2, followedId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
        }
        return false;
    }
    public boolean deletePost(Long postId, Long currentUserId) {
        String sql = "DELETE FROM posts WHERE id = ? AND user_id = ?";
        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setLong(1, postId);
            statement.setLong(2, currentUserId);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
        }
        return false;
    }
    public List<PostResponse> getPostsByUserId(Long userId) {
        List<PostResponse> posts = new ArrayList<>();

        String sql = "SELECT p.id, p.content, p.created_at, u.username, u.profile_image_url " +
                "FROM posts p " +
                "JOIN users u ON p.user_id = u.id " +
                "WHERE p.user_id = ? " +
                "ORDER BY p.created_at DESC";

        try {
            PreparedStatement statement = this.connection.prepareStatement(sql);
            statement.setLong(1, userId);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                PostResponse post = new PostResponse();
                post.setId(resultSet.getLong("id"));
                post.setContent(resultSet.getString("content"));
                post.setCreatedAt(resultSet.getTimestamp("created_at"));
                post.setUsername(resultSet.getString("username"));
                post.setProfilePicture(resultSet.getString("profile_image_url"));
                post.setSuccess(true);
                post.setErrorCode(null);
                posts.add(post);
            }
        } catch (SQLException e) {
            logger.error(ConstantLogger.LOG_DB_UNEXPECTED_ERROR, e.getMessage(), e);
        }
        return posts;
    }
}