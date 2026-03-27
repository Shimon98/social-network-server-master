package com.socialNetwork.server.auth.database;

public final class SqlQueries {

    private SqlQueries() {
    }

    public static final int FEED_LIMIT = 20;
    public static final int SEARCH_USERS_LIMIT = 20;

    public static final String INSERT_USER =
            "INSERT INTO users (username, email, password_hash) VALUES (?, ?, ?)";

    public static final String USER_EXISTS_BY_USERNAME_OR_EMAIL =
            "SELECT COUNT(*) FROM users WHERE username = ? OR email = ?";

    public static final String FIND_USER_BY_USERNAME =
            "SELECT id, username, email, password_hash, profile_image_url FROM users WHERE username = ?";

    public static final String FIND_USER_BY_ID =
            "SELECT id, username, email, password_hash, profile_image_url FROM users WHERE id = ?";

    public static final String SEARCH_USERS_BY_USERNAME =
            "SELECT id, username, profile_image_url " +
                    "FROM users " +
                    "WHERE username LIKE ? AND id <> ? " +
                    "LIMIT " + SEARCH_USERS_LIMIT;

    public static final String UPDATE_PROFILE_IMAGE =
            "UPDATE users SET profile_image_url = ? WHERE id = ?";

    public static final String USER_EXISTS_BY_ID =
            "SELECT 1 FROM users WHERE id = ?";

    public static final String USER_EXISTS_BY_EMAIL =
            "SELECT 1 FROM users WHERE email = ?";

    public static final String INSERT_REFRESH_TOKEN =
            "INSERT INTO refresh_tokens (user_id, token, expires_at, created_at) VALUES (?, ?, ?, ?)";

    public static final String DELETE_REFRESH_TOKENS_BY_USER_ID =
            "DELETE FROM refresh_tokens WHERE user_id = ?";

    public static final String FIND_REFRESH_TOKEN =
            "SELECT id, user_id, token, expires_at, created_at FROM refresh_tokens WHERE token = ?";

    public static final String DELETE_REFRESH_TOKEN =
            "DELETE FROM refresh_tokens WHERE token = ?";

    public static final String IS_FOLLOWING =
            "SELECT 1 FROM follows WHERE follower_id = ? AND followed_id = ?";

    public static final String CREATE_FOLLOW =
            "INSERT INTO follows (follower_id, followed_id) VALUES (?, ?)";

    public static final String REMOVE_FOLLOW =
            "DELETE FROM follows WHERE follower_id = ? AND followed_id = ?";

    public static final String GET_FOLLOWING_USERS =
            "SELECT u.id, u.username, u.profile_image_url " +
                    "FROM follows f " +
                    "JOIN users u ON f.followed_id = u.id " +
                    "WHERE f.follower_id = ?";

    public static final String CREATE_POST =
            "INSERT INTO posts (user_id, content) VALUES (?, ?)";

    public static final String DELETE_POST =
            "DELETE FROM posts WHERE id = ? AND user_id = ?";

    public static final String GET_FEED_POSTS =
            "SELECT p.id, p.user_id, p.content, p.created_at, u.username, u.profile_image_url " +
                    "FROM posts p " +
                    "JOIN users u ON p.user_id = u.id " +
                    "WHERE p.user_id = ? " +
                    "   OR p.user_id IN ( " +
                    "       SELECT followed_id " +
                    "       FROM follows " +
                    "       WHERE follower_id = ? " +
                    "   ) " +
                    "ORDER BY p.created_at DESC " +
                    "LIMIT " + FEED_LIMIT;

    public static final String GET_POSTS_BY_USER_ID =
            "SELECT p.id, p.user_id, p.content, p.created_at, u.username, u.profile_image_url " +
                    "FROM posts p " +
                    "JOIN users u ON p.user_id = u.id " +
                    "WHERE p.user_id = ? " +
                    "ORDER BY p.created_at DESC";







    public static final String IS_USER_BLOCKED =
            "SELECT 1 FROM blocked_users WHERE user_id = ?";

    public static final String INSERT_BLOCKED_USER =
            "INSERT INTO blocked_users (user_id) VALUES (?)";

    public static final String DELETE_BLOCKED_USER =
            "DELETE FROM blocked_users WHERE user_id = ?";

}

