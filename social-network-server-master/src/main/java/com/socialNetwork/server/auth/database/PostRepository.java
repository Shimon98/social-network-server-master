package com.socialNetwork.server.auth.database;

import com.socialNetwork.server.dashboard.responses.PostResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PostRepository extends BaseRepository {

    private static final Logger logger = LoggerFactory.getLogger(PostRepository.class);

    public PostRepository(DatabaseConnectionProvider connectionProvider) {
        super(connectionProvider);
    }

    @CacheEvict(value = "userPosts", key = "#userId")
    public boolean createPost(Long userId, String content) {
        return executeUpdate(logger, SqlQueries.CREATE_POST, userId, content);
    }

    public List<PostResponse> getFeedPosts(Long currentUserId) {
        return queryList(
                logger,
                SqlQueries.GET_FEED_POSTS,
                RowMappers::mapPost,
                currentUserId,
                currentUserId
        );
    }

    @CacheEvict(value = "userPosts", key = "#currentUserId")
    public boolean deletePost(Long postId, Long currentUserId) {
        return executeUpdate(logger, SqlQueries.DELETE_POST, postId, currentUserId);
    }

    @Cacheable(value = "userPosts", key = "#userId", unless = "#result == null")
    public List<PostResponse> getPostsByUserId(Long userId) {
        return queryList(
                logger,
                SqlQueries.GET_POSTS_BY_USER_ID,
                RowMappers::mapPost,
                userId
        );
    }
}