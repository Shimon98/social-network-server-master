package com.socialNetwork.server.dashboard.requests;

public class UpdateProfileImageRequest {
    private String profileImageUrl;

    public UpdateProfileImageRequest() {
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}