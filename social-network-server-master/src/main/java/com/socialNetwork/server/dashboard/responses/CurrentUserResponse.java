package com.socialNetwork.server.dashboard.responses;

import com.socialNetwork.server.auth.responses.BasicResponse;

public class CurrentUserResponse extends BasicResponse {
    private Long id;
    private String username;
    private String profilePicture;

    public CurrentUserResponse() {
    }

    public CurrentUserResponse(boolean success, Integer errorCode) {
    }

    public CurrentUserResponse(boolean success, Integer errorCode, Long id, String username, String profilePicture) {
        super(success, errorCode);
        this.id = id;
        this.username = username;
        this.profilePicture = profilePicture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}