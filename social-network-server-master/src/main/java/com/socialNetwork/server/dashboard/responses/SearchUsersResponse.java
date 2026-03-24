package com.socialNetwork.server.dashboard.responses;

import java.util.List;

public class SearchUsersResponse {
    private List<UserPreviewResponse> users;

    public SearchUsersResponse() {
    }

    public SearchUsersResponse(List<UserPreviewResponse> users) {
        this.users = users;
    }

    public List<UserPreviewResponse> getUsers() {
        return users;
    }

    public void setUsers(List<UserPreviewResponse> users) {
        this.users = users;
    }
}