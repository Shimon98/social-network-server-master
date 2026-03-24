package com.socialNetwork.server.dashboard.responses;

import java.util.List;

public class FollowingResponse {
    private List<UserPreviewResponse> followingUsers;

    public FollowingResponse() {
    }

    public FollowingResponse(List<UserPreviewResponse> followingUsers) {
        this.followingUsers = followingUsers;
    }

    public List<UserPreviewResponse> getFollowingUsers() {
        return followingUsers;
    }

    public void setFollowingUsers(List<UserPreviewResponse> followingUsers) {
        this.followingUsers = followingUsers;
    }
}