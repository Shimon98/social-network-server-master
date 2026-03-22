package com.socialNetwork.server.login.responses;

public class MailResponse extends BasicResponse {
    private String token;

    public MailResponse(boolean success, Integer errorCode, String token) {
        super(success, errorCode);
        this.token = token;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}
