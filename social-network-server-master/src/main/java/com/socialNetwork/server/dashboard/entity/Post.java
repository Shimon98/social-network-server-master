package com.socialNetwork.server.dashboard.entity;

import java.sql.Timestamp;

public class Post {
    private Long id;
    private Long userId;
    private String content;
    private Timestamp createdAt;

    public Post() {
    }

    public Post(Long id, Long userId, String content, Timestamp createdAt) {
        this.id = id;
        this.userId = userId;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
}