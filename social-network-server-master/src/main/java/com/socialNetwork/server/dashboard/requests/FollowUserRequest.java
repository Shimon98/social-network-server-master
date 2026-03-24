package com.socialNetwork.server.dashboard.requests;

public class FollowUserRequest {
    private Long followedUserId;

    public FollowUserRequest() {
    }

    public Long getFollowedUserId() {
        return followedUserId;
    }

    public void setFollowedUserId(Long followedUserId) {
        this.followedUserId = followedUserId;
    }
}