package com.socialNetwork.server.auth.requests;

public class LoginRequest extends BasicRequest{

    public LoginRequest() {
    }

    public LoginRequest(String username, String password ) {
        super(username, password);
    }


}
