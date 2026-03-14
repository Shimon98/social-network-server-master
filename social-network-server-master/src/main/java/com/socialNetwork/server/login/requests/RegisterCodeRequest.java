package com.socialNetwork.server.login.requests;

public class RegisterCodeRequest extends EmailRequest {

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