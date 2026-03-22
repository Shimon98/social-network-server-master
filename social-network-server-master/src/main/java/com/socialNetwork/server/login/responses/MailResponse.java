package com.socialNetwork.server.login.responses;

public class MailResponse extends BasicResponse {
    private String Token;

    public MailResponse(boolean success, Integer errorCode, String token) {
        super(success, errorCode);
        Token = token;
    }


    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

}
