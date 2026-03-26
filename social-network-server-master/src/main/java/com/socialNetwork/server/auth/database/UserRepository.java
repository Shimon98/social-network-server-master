package com.socialNetwork.server.auth.database;

import com.socialNetwork.server.auth.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository extends BaseRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    public UserRepository(DatabaseConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    public boolean createUser(User user) {
        return executeUpdate(
                logger,
                SqlQueries.INSERT_USER,
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash()
        );
    }

    public boolean userExists(String username, String email) {
        Integer count = queryInt(
                logger,
                SqlQueries.USER_EXISTS_BY_USERNAME_OR_EMAIL,
                username,
                email
        );
        return count != null && count > 0;
    }

    public User findUserByUsername(String username) {
        return queryOne(
                logger,
                SqlQueries.FIND_USER_BY_USERNAME,
                RowMappers::mapFullUser,
                username
        );
    }

    public User findUserById(Long userId) {
        return queryOne(
                logger,
                SqlQueries.FIND_USER_BY_ID,
                RowMappers::mapFullUser,
                userId
        );
    }

    public List<User> searchUsersByUsername(Long currentUserId, String text) {
        return queryList(
                logger,
                SqlQueries.SEARCH_USERS_BY_USERNAME,
                RowMappers::mapUserPreview,
                "%" + text + "%",
                currentUserId
        );
    }

    public boolean updateProfileImage(Long userId, String profileImageUrl) {
        return executeUpdate(
                logger,
                SqlQueries.UPDATE_PROFILE_IMAGE,
                profileImageUrl,
                userId
        );
    }

    public boolean userExistsById(Long userId) {
        return exists(logger, SqlQueries.USER_EXISTS_BY_ID, userId);
    }

    public boolean userExistsByEmail(String email) {
        return exists(logger, SqlQueries.USER_EXISTS_BY_EMAIL, email);
    }
}