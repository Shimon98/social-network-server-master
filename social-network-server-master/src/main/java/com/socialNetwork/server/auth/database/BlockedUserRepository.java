package com.socialNetwork.server.auth.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class BlockedUserRepository extends BaseRepository {
    private static final Logger logger = LoggerFactory.getLogger(BlockedUserRepository.class);

    public BlockedUserRepository(DatabaseConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    public boolean isUserBlocked(Long userId) {
        return exists(
                logger,
                SqlQueries.IS_USER_BLOCKED,
                userId
        );
    }

    public boolean blockUser(Long userId) {
        return executeUpdate(
                logger,
                SqlQueries.INSERT_BLOCKED_USER,
                userId
        );
    }

    public boolean unblockUser(Long userId) {
        return executeUpdate(
                logger,
                SqlQueries.DELETE_BLOCKED_USER,
                userId
        );
    }
}