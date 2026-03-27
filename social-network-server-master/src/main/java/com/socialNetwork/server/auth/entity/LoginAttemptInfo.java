package com.socialNetwork.server.auth.entity;

public class LoginAttemptInfo {
    private int attempts;

    public LoginAttemptInfo() {
        this.attempts = 0;
    }

    public void addAttempt() {
        this.attempts++;
    }

    public int getAttempts() {
        return attempts;
    }
}