package com.socialNetwork.server.auth.responses;

public class RegisterResponse extends BasicResponse {

    public RegisterResponse(boolean success, Integer errorCode) {
        super(success, errorCode);
    }
}