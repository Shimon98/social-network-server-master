package com.socialNetwork.server.dashboard.services;

import com.socialNetwork.server.auth.database.FollowRepository;
import com.socialNetwork.server.auth.database.PostRepository;
import com.socialNetwork.server.auth.database.UserRepository;
import com.socialNetwork.server.auth.responses.BasicResponse;
import com.socialNetwork.server.auth.utils.ErrorCodes;
import com.socialNetwork.server.dashboard.responses.FeedResponse;
import com.socialNetwork.server.dashboard.responses.PostResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, FollowRepository followRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    public BasicResponse createPost(Long currentUserId, String content) {
        if (content == null) {
            return new BasicResponse(false, ErrorCodes.POST_FAILURE);
        }
        content = content.trim();
        if (content.isEmpty()) {
            return new BasicResponse(false, ErrorCodes.POST_FAILURE);
        }
        boolean created = postRepository.createPost(currentUserId, content);
        if (!created) {
            return new BasicResponse(false, ErrorCodes.POST_FAILURE);
        }
        return new BasicResponse(true, ErrorCodes.POST_SUCCESS);
    }

    public FeedResponse getFeed(Long currentUserId) {
        List<PostResponse> posts = postRepository.getFeedPosts(currentUserId);
        return new FeedResponse(true, ErrorCodes.GET_FEED_SUCCESS, posts);
    }

    public BasicResponse deletePost(Long currentUserId, Long postId) {
        if (currentUserId == null || postId == null) {
            return new BasicResponse(false, ErrorCodes.POST_DELETE_FAILURE);
        }
        boolean deleted = postRepository.deletePost(postId, currentUserId);
        if (!deleted) {
            return new BasicResponse(false, ErrorCodes.POST_DELETE_FAILURE);
        }
        return new BasicResponse(true, ErrorCodes.POST_DELETE_SUCCESS);
    }

    public FeedResponse getMyPosts(Long currentUserId) {
        List<PostResponse> posts = postRepository.getPostsByUserId(currentUserId);
        return new FeedResponse(true, null, posts);
    }

    public FeedResponse getUserPosts(Long currentUserId, Long userId) {
        if (currentUserId == null || userId == null) {
            return new FeedResponse(false, ErrorCodes.GET_FEED_FAILURE, null);
        }
        boolean allowed = currentUserId.equals(userId) || followRepository.isFollowing(currentUserId, userId);
        if (!allowed) {
            return new FeedResponse(false, ErrorCodes.GET_FEED_FAILURE, null);
        }
        boolean userExists = userRepository.userExistsById(userId);
        if (!userExists) {
            return new FeedResponse(false, ErrorCodes.GET_FEED_FAILURE, null);
        }
        List<PostResponse> posts = postRepository.getPostsByUserId(userId);
        return new FeedResponse(true, null, posts);
    }
}