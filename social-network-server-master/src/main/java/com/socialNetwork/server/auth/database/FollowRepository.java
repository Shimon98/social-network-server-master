package com.socialNetwork.server.auth.database;

import com.socialNetwork.server.auth.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FollowRepository extends BaseRepository {

    private static final Logger logger = LoggerFactory.getLogger(FollowRepository.class);

    public FollowRepository(DatabaseConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    public boolean isFollowing(Long followerId, Long followedId) {
        return exists(logger, SqlQueries.IS_FOLLOWING, followerId, followedId);
    }

    public boolean createFollow(Long followerId, Long followedId) {
        return executeUpdate(logger, SqlQueries.CREATE_FOLLOW, followerId, followedId);
    }

    public boolean removeFollow(Long followerId, Long followedId) {
        return executeUpdate(logger, SqlQueries.REMOVE_FOLLOW, followerId, followedId);
    }

    public List<User> getFollowingUsers(Long currentUserId) {
        return queryList(
                logger,
                SqlQueries.GET_FOLLOWING_USERS,
                RowMappers::mapUserPreview,
                currentUserId
        );
    }
}