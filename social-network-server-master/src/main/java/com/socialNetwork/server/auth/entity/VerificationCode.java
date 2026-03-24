package com.socialNetwork.server.auth.entity;

import com.socialNetwork.server.auth.utils.TempCodePurpose;

public class VerificationCode {
    private String code;
    private String email;
    private long createAt;
    private int attempts;
    private TempCodePurpose purpose;

    public VerificationCode(TempCodePurpose purpose, String email, String code) {
        this.purpose = purpose;
        this.email = email;
        this.code = code;
        this.attempts = 0;
        this.createAt = System.currentTimeMillis();

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getCreateAt() {
        return createAt;
    }

    public void setCreateAt(long createAt) {
        this.createAt = createAt;
    }

    public TempCodePurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(TempCodePurpose purpose) {
        this.purpose = purpose;
    }

    public int getAttempts() {
        return attempts;
    }

    public void addAttempt() {
        this.attempts++;
    }
}
