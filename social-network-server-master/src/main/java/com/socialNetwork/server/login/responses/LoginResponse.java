package com.socialNetwork.server.login.responses;

public class LoginResponse extends BasicResponse {
    private String accessToken;
    private String refreshToken;

    public LoginResponse(boolean success, String message, String accessToken, String refreshToken) {
        super(success, message);
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
