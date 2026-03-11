package com.socialNetwork.server.login.responses;

public class RegisterResponse extends BasicResponse {

    public RegisterResponse(boolean success, Integer errorCode) {
        super(success, errorCode);
    }
}