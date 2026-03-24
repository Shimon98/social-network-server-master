package com.socialNetwork.server.auth.responses;

public class RefreshResponse extends BasicResponse {

    private String accessToken;

    public RefreshResponse(boolean success, Integer errorCode, String accessToken) {
        super(success, errorCode);
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}