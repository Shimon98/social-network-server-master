package com.socialNetwork.server.auth.responses;

public class LoginResponse extends BasicResponse {
    private final String accessToken;
    private final String refreshToken;

    public LoginResponse(boolean success, Integer errorCode, String accessToken, String refreshToken) {
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