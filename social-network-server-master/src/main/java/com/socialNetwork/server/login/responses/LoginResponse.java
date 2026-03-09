package com.socialNetwork.server.login.responses;

public class LoginResponse extends BasicResponse{
    private String token;

    public LoginResponse(boolean successes, Integer errorCode, String token) {
        super(successes, errorCode);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
