package com.socialNetwork.server.login.responses;

public class RegisterResponse {
    private boolean success;
    private String message;

    public RegisterResponse() {
    }

    public RegisterResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
