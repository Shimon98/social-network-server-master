package com.socialNetwork.server.auth.requests;

public class LoginCodeAnswer extends LoginCodeRequest {
    private String code;
    public LoginCodeAnswer() {
    }

    public LoginCodeAnswer(String pendingLoginToken, String code) {
        super(pendingLoginToken);
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}