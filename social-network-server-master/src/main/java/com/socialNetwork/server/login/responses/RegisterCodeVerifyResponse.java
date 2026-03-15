package com.socialNetwork.server.login.responses;

public class RegisterCodeVerifyResponse extends BasicResponse {

    private String registrationToken;

    public RegisterCodeVerifyResponse(boolean success, Integer errorCode, String registrationToken) {
        super(success, errorCode);
        this.registrationToken = registrationToken;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }
}