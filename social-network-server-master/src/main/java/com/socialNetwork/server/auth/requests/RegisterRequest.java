package com.socialNetwork.server.auth.requests;

public class RegisterRequest extends BasicRequest {

    public RegisterRequest() {
    }

    public RegisterRequest(String username, String email, String password) {
        super(username, email, password);
    }


}


