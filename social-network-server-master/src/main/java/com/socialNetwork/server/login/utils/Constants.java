package com.socialNetwork.server.login.utils;

public class Constants {
    public static final int MIN_USERNAME_LENGTH = 3;
    public static final int MAX_USERNAME_LENGTH = 20;
    public static final int MIN_PASSWORD_LENGTH = 6;


    public static final String EMAIL_AT_SIGN = "@";
    public static final String EMAIL_DOT = ".";

    public static final String ACCESS_COOKIE_NAME = "accessToken";
    public static final String REFRESH_COOKIE_NAME = "refreshToken";

    public static final String COOKIE_PATH = "/";
    public static final String COOKIE_SAME_SITE = "Lax";
    public static final String SET_COOKIE_HEADER = "Set-Cookie";

    public static final boolean COOKIE_HTTP_ONLY = true;
    public static final boolean COOKIE_SECURE = false;

    public static final long COOKIE_DELETE_MAX_AGE = 0;




}
