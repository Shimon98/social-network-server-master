package com.socialNetwork.server.login.responses;

public class MailResponse extends BasicResponse {
    private String tempToken;

    public MailResponse(boolean success, Integer errorCode, String tempToken) {
        super(success, errorCode);
        this.tempToken = tempToken;
    }


    public String getTempToken() {
        return tempToken;
    }

    public void setTempToken(String tempToken) {
        this.tempToken = tempToken;
    }

}
