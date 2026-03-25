package com.socialNetwork.server.auth.requests;

public class LoginCodeRequest {
    private String tempToken;

    public LoginCodeRequest() {
    }

    public LoginCodeRequest(String tempToken) {
        this.tempToken = tempToken;
    }

    public String getTempToken() {
        return tempToken;
    }

    public void setTempToken(String tempToken) {
        this.tempToken = tempToken;
    }

}