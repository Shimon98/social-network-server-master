package com.socialNetwork.server.auth.utils;

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
    public static final String COOKIE_VALUE = "";

    public static final boolean COOKIE_HTTP_ONLY = true;
    public static final boolean COOKIE_SECURE = false;

    public static final long COOKIE_DELETE_MAX_AGE = 0;

    public static final int CODE_MAX_ATTEMPT = 5;

    public static final int SECOND = 1000;

    public static final String MISSING_TOKEN = "Missing token";
    public static final String INVALID_TOKEN = "Invalid token";
    public static final String WRONG_TOKEN_TYPE = "Wrong token type";
    public static final String ACCESS = "access";
    public static final String REFRESH = "refresh";

    public static final String PENDING_LOGIN = "pending_login";
    public static final String PENDING_REGISTER = "pending_register";

    public static final String BLOCK_SERVICE_NAME = "";

    public static final String USER_ID = "userId";
    public static final String EMAIL = "email";
    public static final String TYPE = "type";

    public static final String ALGORITHM = "SHA-256";
    public static final String FORMAT = "%02x";
    public static final String HASH_ERROR = "Hash error";





}
