package com.socialNetwork.server.login.entity;

public class User { //מחלקת יוזר של מה שנשמר בDB בפועל (על פניו נראה דומה מאוד לREGISTER REQUEST אך הם ממלאים תפקידים שונים
    private int id;
    private String username;
    private String email;
    private String passwordHash;

    public User() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
