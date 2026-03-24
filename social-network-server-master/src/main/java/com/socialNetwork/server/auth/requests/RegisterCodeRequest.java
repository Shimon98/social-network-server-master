package com.socialNetwork.server.auth.requests;

public class RegisterCodeRequest extends EmailRequest {
///  זה חדש
    private String code;

    public RegisterCodeRequest() {
    }

    public RegisterCodeRequest(String email, String code) {
        super(email);
        this.code = code;
    }



    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }
}