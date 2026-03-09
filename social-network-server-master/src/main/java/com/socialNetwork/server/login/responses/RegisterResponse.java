package com.socialNetwork.server.login.responses;

public class RegisterResponse {
    private boolean successes;

    public RegisterResponse(boolean successes) {
        this.successes = successes;
    }

    public boolean isSuccesses() {
        return successes;
    }

    public void setSuccesses(boolean successes) {
        this.successes = successes;
    }
}
