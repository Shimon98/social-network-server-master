package com.socialNetwork.server.login.requests;

public class LoginCodeRequest extends BasicRequest {

    private String code;

    public LoginCodeRequest() {
    }

    public LoginCodeRequest(String username, String code) {
         super(username);
        this.code = code;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}