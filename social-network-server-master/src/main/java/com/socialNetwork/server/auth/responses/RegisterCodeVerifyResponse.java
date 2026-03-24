package com.socialNetwork.server.auth.responses;

public class RegisterCodeVerifyResponse extends MailResponse {
    public RegisterCodeVerifyResponse(boolean success, Integer errorCode, String registrationToken) {
        super(success, errorCode , registrationToken);

    }

}