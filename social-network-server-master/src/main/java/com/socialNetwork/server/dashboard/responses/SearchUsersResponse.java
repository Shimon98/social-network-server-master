package com.socialNetwork.server.dashboard.responses;

import com.socialNetwork.server.auth.responses.BasicResponse;

import java.util.List;

public class SearchUsersResponse extends BasicResponse {
    private List<UserPreviewResponse> users;

    public SearchUsersResponse() {
    }

    public SearchUsersResponse(boolean success, Integer errorCode) {
        super(success, errorCode);
    }

    public SearchUsersResponse(boolean success, Integer errorCode, List<UserPreviewResponse> users) {
        super(success, errorCode);
        this.users = users;
    }

    public List<UserPreviewResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UserPreviewResponse> users) {
        this.users = users;
    }
}