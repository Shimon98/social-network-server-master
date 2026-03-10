package com.socialNetwork.server.login.responses;

public class RegisterResponse extends BasicResponse { //מחזיר תשובה אם ניסיו ההרשמה עבר בהצלחה או נכשל

    public RegisterResponse(String message, boolean success) {
        super(success, message);
    }

}
