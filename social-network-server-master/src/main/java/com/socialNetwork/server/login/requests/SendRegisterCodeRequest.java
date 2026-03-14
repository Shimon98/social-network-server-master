package com.socialNetwork.server.login.requests;

public class SendRegisterCodeRequest {

    private String email;

    public SendRegisterCodeRequest() {
    }

    public SendRegisterCodeRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}