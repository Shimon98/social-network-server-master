package com.socialNetwork.server.auth.database;

import com.socialNetwork.server.auth.entity.RefreshToken;
import com.socialNetwork.server.auth.entity.User;
import com.socialNetwork.server.dashboard.responses.PostResponse;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class RowMappers {

    private RowMappers() {
    }

    public static User mapFullUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUsername(resultSet.getString("username"));
        user.setEmail(resultSet.getString("email"));
        user.setPasswordHash(resultSet.getString("password_hash"));
        user.setProfileImageUrl(resultSet.getString("profile_image_url"));
        return user;
    }

    public static User mapUserPreview(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUsername(resultSet.getString("username"));
        user.setProfileImageUrl(resultSet.getString("profile_image_url"));
        return user;
    }

    public static RefreshToken mapRefreshToken(ResultSet resultSet) throws SQLException {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setId(resultSet.getInt("id"));
        refreshToken.setUserId(resultSet.getInt("user_id"));
        refreshToken.setToken(resultSet.getString("token"));
        refreshToken.setExpiresAt(resultSet.getLong("expires_at"));
        refreshToken.setCreatedAt(resultSet.getLong("created_at"));
        return refreshToken;
    }

    public static PostResponse mapPost(ResultSet resultSet) throws SQLException {
        PostResponse post = new PostResponse();
        post.setId(resultSet.getLong("id"));
        post.setUserId(resultSet.getLong("user_id"));
        post.setContent(resultSet.getString("content"));
        post.setCreatedAt(resultSet.getTimestamp("created_at"));
        post.setUsername(resultSet.getString("username"));
        post.setProfilePicture(resultSet.getString("profile_image_url"));
        post.setSuccess(true);
        post.setErrorCode(null);
        return post;
    }
}