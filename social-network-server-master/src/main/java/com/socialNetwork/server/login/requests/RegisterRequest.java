package com.socialNetwork.server.login.requests;

public class RegisterRequest extends BasicRequest { //מחלקה שמקבלת מידע מהלקוח בשלב זה נתונים יבשים הפרטים האלו מועברים לאוטנטיקציה וולידציה לפני שנוצר אובייקט של יוזר
    private String username;
    private String email;
    private String password;

    public RegisterRequest() {
    }

    public RegisterRequest(String username, String email, String password) {
        super(username, email, password);
    }


}


