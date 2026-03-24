package com.socialNetwork.server.dashboard.services;

import com.socialNetwork.server.auth.database.DBManager;
import com.socialNetwork.server.auth.responses.BasicResponse;
import com.socialNetwork.server.auth.utils.Errors;
import com.socialNetwork.server.dashboard.responses.FeedResponse;
import com.socialNetwork.server.dashboard.responses.PostResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    private final DBManager dbManager;

    public PostService(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public BasicResponse createPost(Long currentUserId, String content) {
        if (content == null) {
            return new BasicResponse(false, Errors.POST_FAILURE);
        }
        content = content.trim();
        if (content.isEmpty()) {
            return new BasicResponse(false, Errors.POST_FAILURE);
        }
        boolean created = dbManager.createPost(currentUserId, content);
        if (!created) {
            return new BasicResponse(false, Errors.POST_FAILURE);
        }
        return new BasicResponse(true, Errors.POST_SUCCESS);
    }

    public FeedResponse getFeed(Long currentUserId) {
        List<PostResponse> posts = dbManager.getFeedPosts(currentUserId);
        return new FeedResponse(posts);
    }
    public BasicResponse deletePost(Long currentUserId, Long postId) {
        boolean deleted = dbManager.deletePost(postId, currentUserId);

        if (!deleted) {
            return new BasicResponse(false, Errors.POST_DELETE_FAILURE);
        }

        return new BasicResponse(true, Errors.POST_DELETE_SUCCESS);
    }
    public FeedResponse getMyPosts(Long currentUserId) {
        List<PostResponse> posts = dbManager.getPostsByUserId(currentUserId);
        return new FeedResponse(posts);
    }
}