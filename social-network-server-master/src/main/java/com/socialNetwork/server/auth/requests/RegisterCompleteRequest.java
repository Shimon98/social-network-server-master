package com.socialNetwork.server.auth.requests;

public class RegisterCompleteRequest {
    ///  זה חדש
    private String registrationToken;
    private String username;
    private String password;

    public RegisterCompleteRequest() {
    }

    public RegisterCompleteRequest(String registrationToken, String username, String password) {
        this.registrationToken = registrationToken;
        this.username = username;
        this.password = password;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}