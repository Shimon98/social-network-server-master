package com.socialNetwork.server.dashboard.responses;

import com.socialNetwork.server.auth.responses.BasicResponse;

public class UserPreviewResponse extends BasicResponse {
    private Long id;
    private String username;
    private String profilePicture;
    private boolean isFollowing;

    public UserPreviewResponse() {
    }

    public UserPreviewResponse(boolean success, Integer errorCode,Long id, String username, String profilePicture, boolean isFollowing) {
        super(success,errorCode);
        this.id = id;
        this.username = username;
        this.profilePicture = profilePicture;
        this.isFollowing = isFollowing;
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

    public boolean isFollowing() {
        return isFollowing;
    }

    public void setFollowing(boolean following) {
        isFollowing = following;
    }
}