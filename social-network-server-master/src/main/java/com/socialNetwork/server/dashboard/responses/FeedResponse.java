package com.socialNetwork.server.dashboard.responses;

import java.util.List;

public class FeedResponse {
    private List<PostResponse> posts;

    public FeedResponse() {
    }

    public FeedResponse(List<PostResponse> posts) {
        this.posts = posts;
    }

    public List<PostResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<PostResponse> posts) {
        this.posts = posts;
    }
}