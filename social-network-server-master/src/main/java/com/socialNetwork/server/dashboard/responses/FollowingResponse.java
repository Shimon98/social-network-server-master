package com.socialNetwork.server.dashboard.responses;

import com.socialNetwork.server.auth.requests.BasicRequest;
import com.socialNetwork.server.auth.responses.BasicResponse;

import java.util.List;

public class FollowingResponse extends BasicResponse {
    private List<UserPreviewResponse> followingUsers;


    public FollowingResponse() {
    }
    public FollowingResponse(boolean success, Integer errorCode) {
    }

    public FollowingResponse(boolean success, Integer errorCode, List<UserPreviewResponse> followingUsers) {
        super(success,errorCode);
        this.followingUsers = followingUsers;
    }

    public List<UserPreviewResponse> getFollowingUsers() {
        return followingUsers;
    }

    public void setFollowingUsers(List<UserPreviewResponse> followingUsers) {
        this.followingUsers = followingUsers;
    }
}