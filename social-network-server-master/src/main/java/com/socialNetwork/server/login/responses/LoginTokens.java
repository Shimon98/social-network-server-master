package com.socialNetwork.server.login.responses;

public class LoginTokens extends BasicResponse {
    private final String accessToken;
    private final String refreshToken;

    public LoginTokens(boolean success, Integer errorCode, String accessToken, String refreshToken) {
        super(success, errorCode);

        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}