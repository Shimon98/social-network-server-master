package com.socialNetwork.server.dashboard.requests;

public class SearchRequest {
    private String text;


    public SearchRequest(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
