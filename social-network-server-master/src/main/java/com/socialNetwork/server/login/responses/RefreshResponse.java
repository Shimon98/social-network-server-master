package com.socialNetwork.server.login.responses;

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