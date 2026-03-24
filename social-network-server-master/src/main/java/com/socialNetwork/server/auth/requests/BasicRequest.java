package com.socialNetwork.server.auth.requests;

public class BasicRequest extends EmailRequest {
    private String username;
    private String email;
    private String password;

    public BasicRequest() {
    }

    public BasicRequest(String username, String email, String password) {
        super(email);
        this.username = username;
        this.password = password;
    }

    public BasicRequest(String username, String password ) {
        this.username = username;
        this.password = password;
    }

    public BasicRequest(String username) {
        this.username = username;
    }





    public String getUsername() {
        return username;
    }


    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }



    public void setPassword(String password) {
        this.password = password;
    }
}
