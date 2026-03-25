package com.socialNetwork.server.auth.responses;

public class BasicResponse {
    private boolean success;
    private Integer errorCode;

    public BasicResponse(){}

    public BasicResponse(boolean success, Integer errorCode) {
        this.success = success;
        this.errorCode = errorCode;
    }

    public boolean isSuccess() {
        return success;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}