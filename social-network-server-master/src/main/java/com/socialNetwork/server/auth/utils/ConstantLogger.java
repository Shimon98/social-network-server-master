package com.socialNetwork.server.auth.utils;

public class ConstantLogger {
    public static final String LOG_LOGIN_FAILED_USERNAME = "Login failed for username {}";
    public static final String LOG_REGISTER_INSERT_FAILED = "Register failed to insert user {}";
    public static final String LOG_REGISTER_UNEXPECTED_ERROR = "Unexpected error during register for username {}";
    public static final String LOG_REGISTER_VERIFY_CODE_ERROR = "Failed to verify register code for email {}";
    public static final String LOG_REGISTER_SEND_CODE_ERROR = "Failed to send register code for email {}";


    public static final String LOG_DB_CONNECTED = "DB connected successfully";
    public static final String LOG_DB_FAILED_CONNECTED = "Failed to create DB connection";
    public static final String LOG_DB_UNEXPECTED_ERROR = "Database error";

    public static final String LOG_REFRESH_TOKEN_ERROR = "Refresh token error";

    public static final String LOG_VERIFY_LOGIN_CODE_FAIL = "Failed to verify login code";

}
