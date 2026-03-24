package com.socialNetwork.server.dashboard.responses;

import java.sql.Timestamp;

public class PostResponse {
    private Long id;
    private String content;
    private Timestamp createdAt;
    private String username;
    private String profilePicture;

    public PostResponse() {
    }

    public PostResponse(Long id, String content, Timestamp createdAt, String username, String profilePicture) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.username = username;
        this.profilePicture = profilePicture;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
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