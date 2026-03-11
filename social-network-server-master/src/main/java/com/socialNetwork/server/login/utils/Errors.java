package com.socialNetwork.server.login.utils;

public class Errors {
    public static final int INVALID_REQUEST = 1000;

    public static final int MISSING_USERNAME = 1001;
    public static final int INVALID_USERNAME = 1002;

    public static final int MISSING_PASSWORD = 1003;
    public static final int INVALID_PASSWORD = 1004;

    public static final int MISSING_EMAIL = 1005;
    public static final int INVALID_EMAIL = 1006;

    public static final int USERNAME_ALREADY_EXISTS = 2000;
    public static final int EMAIL_ALREADY_EXISTS = 2001;
    public static final int USER_ALREADY_EXISTS = 2002;

    public static final int INVALID_CREDENTIALS = 3000;

    public static final int REGISTRATION_FAILED = 4000;
    public static final int INTERNAL_SERVER_ERROR = 5000;
}
