package com.socialNetwork.server.login.responses;

public class PendingLoginResponse extends BasicResponse {

    private String pendingLoginToken;

    public PendingLoginResponse(boolean success, Integer errorCode, String pendingLoginToken) {
        super(success, errorCode);
        this.pendingLoginToken = pendingLoginToken;
    }

    public String getPendingLoginToken() {
        return pendingLoginToken;
    }

    public void setPendingLoginToken(String pendingLoginToken) {
        this.pendingLoginToken = pendingLoginToken;
    }
}