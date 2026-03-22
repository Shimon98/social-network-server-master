package com.socialNetwork.server.login.requests;

public class LoginCodeRequest {
    private String pendingLoginToken;


    public LoginCodeRequest() {
    }

    public LoginCodeRequest(String pendingLoginToken) {
        this.pendingLoginToken = pendingLoginToken;
    }

    public String getPendingLoginToken() {
        return pendingLoginToken;
    }

    public void setPendingLoginToken(String pendingLoginToken) {
        this.pendingLoginToken = pendingLoginToken;
    }

}