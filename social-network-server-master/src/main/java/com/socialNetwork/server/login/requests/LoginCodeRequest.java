package com.socialNetwork.server.login.requests;

public class LoginCodeRequest {
    ///  זה חדש
    private String pendingLoginToken;
    private String code;

    public LoginCodeRequest() {
    }

    public LoginCodeRequest(String pendingLoginToken, String code) {
        this.pendingLoginToken = pendingLoginToken;
        this.code = code;
    }

    public String getPendingLoginToken() {
        return pendingLoginToken;
    }

    public String getCode() {
        return code;
    }

    public void setPendingLoginToken(String pendingLoginToken) {
        this.pendingLoginToken = pendingLoginToken;
    }

    public void setCode(String code) {
        this.code = code;
    }
}