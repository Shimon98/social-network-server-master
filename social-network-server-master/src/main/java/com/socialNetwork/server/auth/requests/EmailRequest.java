package com.socialNetwork.server.auth.requests;

public class EmailRequest {
    ///  זה חדש
    private String email;

    public EmailRequest() {
    }

    public EmailRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}