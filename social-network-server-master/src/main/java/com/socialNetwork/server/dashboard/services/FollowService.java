package com.socialNetwork.server.dashboard.services;

import com.socialNetwork.server.auth.database.FollowRepository;
import com.socialNetwork.server.auth.database.UserRepository;
import com.socialNetwork.server.auth.entity.User;
import com.socialNetwork.server.auth.responses.BasicResponse;
import com.socialNetwork.server.auth.utils.ErrorCodes;
import com.socialNetwork.server.dashboard.responses.FollowingResponse;
import com.socialNetwork.server.dashboard.responses.SearchUsersResponse;
import com.socialNetwork.server.dashboard.responses.UserPreviewResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FollowService {
    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public FollowService(UserRepository userRepository, FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    public SearchUsersResponse searchUsers(Long currentUserId, String text) {
        List<User> users = userRepository.searchUsersByUsername(currentUserId, text);
        List<UserPreviewResponse> result = new ArrayList<>();
        for (User user : users) {
            boolean isFollowing = followRepository.isFollowing(currentUserId, user.getId());
            result.add(new UserPreviewResponse(
                    true,
                    null,
                    user.getId(),
                    user.getUsername(),
                    user.getProfileImageUrl(),
                    isFollowing
            ));
        }
        return new SearchUsersResponse(true, null, result);
    }

    public BasicResponse followUser(Long currentUserId, Long followedUserId) {
        if (currentUserId.equals(followedUserId)) {
            return new BasicResponse(false, ErrorCodes.FOLLOW_FAILURE);
        }
        boolean alreadyFollowing = followRepository.isFollowing(currentUserId, followedUserId);
        if (alreadyFollowing) {
            return new BasicResponse(false, ErrorCodes.FOLLOW_FAILURE);
        }
        boolean created = followRepository.createFollow(currentUserId, followedUserId);
        if (!created) {
            return new BasicResponse(false, ErrorCodes.FOLLOW_FAILURE);
        }
        return new BasicResponse(true, ErrorCodes.FOLLOW_SUCCESS);
    }

    public FollowingResponse getFollowingUsers(Long currentUserId) {
        List<User> users = followRepository.getFollowingUsers(currentUserId);
        List<UserPreviewResponse> result = new ArrayList<>();
        for (User user : users) {
            result.add(new UserPreviewResponse(
                    true,
                    null,
                    user.getId(),
                    user.getUsername(),
                    user.getProfileImageUrl(),
                    true
            ));
        }
        return new FollowingResponse(true, null, result);
    }

    public BasicResponse unfollowUser(Long currentUserId, Long followedUserId) {
        if (currentUserId.equals(followedUserId)) {
            return new BasicResponse(false, ErrorCodes.UNFOLLOW_FAILURE);
        }
        boolean isFollowing = followRepository.isFollowing(currentUserId, followedUserId);
        if (!isFollowing) {
            return new BasicResponse(false, ErrorCodes.UNFOLLOW_FAILURE);
        }
        boolean removed = followRepository.removeFollow(currentUserId, followedUserId);
        if (!removed) {
            return new BasicResponse(false, ErrorCodes.UNFOLLOW_FAILURE);
        }
        return new BasicResponse(true, ErrorCodes.UNFOLLOW_SUCCESS);
    }
}