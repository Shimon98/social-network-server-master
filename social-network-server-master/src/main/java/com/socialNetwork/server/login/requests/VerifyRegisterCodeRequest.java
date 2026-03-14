package com.socialNetwork.server.login.requests;

public class VerifyRegisterCodeRequest extends SendRegisterCodeRequest{
    private String code;

    public VerifyRegisterCodeRequest() {
    }

    public VerifyRegisterCodeRequest(String email, String code) {
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
