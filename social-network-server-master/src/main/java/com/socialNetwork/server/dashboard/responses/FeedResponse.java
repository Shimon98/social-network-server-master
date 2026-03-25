package com.socialNetwork.server.dashboard.responses;

import com.socialNetwork.server.auth.responses.BasicResponse;

import java.util.List;

public class FeedResponse extends BasicResponse {
    private List<PostResponse> posts;

    public FeedResponse() {
    }

    public FeedResponse(boolean success, Integer errorCode, List<PostResponse> posts) {
        super(success, errorCode);
        this.posts = posts;
    }

    public List<PostResponse> getPosts() {
        return posts;
    }

    public void setPosts(List<PostResponse> posts) {
        this.posts = posts;
    }
}