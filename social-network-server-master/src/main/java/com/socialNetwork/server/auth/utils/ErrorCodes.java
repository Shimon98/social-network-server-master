package com.socialNetwork.server.auth.utils;

public class ErrorCodes {
    public static final int INVALID_REQUEST = 1000;

    public static final int MISSING_USERNAME = 1001;
    public static final int INVALID_USERNAME = 1002;

    public static final int MISSING_PASSWORD = 1003;
    public static final int INVALID_PASSWORD = 1004;

    public static final int MISSING_EMAIL = 1005;
    public static final int INVALID_EMAIL = 1006;
    public static final int INVALID_SEND_CODE = 1007;

    public static final int USERNAME_ALREADY_EXISTS = 2000;
    public static final int EMAIL_ALREADY_EXISTS = 2001;
    public static final int USER_ALREADY_EXISTS = 2002;

    public static final int INVALID_CREDENTIALS = 3000;

    public static final int REGISTRATION_FAILED = 4000;
    public static final int INTERNAL_SERVER_ERROR = 5000;

    public static final int INVALID_TOKEN = 6000;

    public static final int INVALID_PROFILE_IMAGE = 7000;//"Failed to update profile image"
    public static final int IMAGE_CHANGE_SUCCESS = 7001;//"Profile image updated successfully"
    public static final int FOLLOW_FAILURE = 7002;//"You cannot follow yourself" ,"You are already following this user","Failed to follow user"
    public static final int FOLLOW_SUCCESS = 7003; //"Follow created successfully"
    public static final int UNFOLLOW_FAILURE = 7004; //"You cannot unfollow yourself" , "You are not following this user" , "Failed to unfollow user"
    public static final int UNFOLLOW_SUCCESS = 7005; //"Unfollow completed successfully"
    public static final int POST_FAILURE = 7006; //"Post content cannot be empty","Failed to create post"
    public static final int POST_SUCCESS = 7007; //"Post created successfully"
    public static final int POST_DELETE_FAILURE = 7008; //"Failed to delete post"
    public static final int POST_DELETE_SUCCESS = 7009; //"Post deleted successfully"
    public static final int GET_FEED_SUCCESS = 7010;
    public static final int GET_FEED_FAILURE = 7011;

    public static final int UNAUTHORIZED = 401;
    public static final int USER_NOT_FOUND = 4041;
    public static final int INVALID_POST = 4001;
    public static final int POST_NOT_FOUND = 4042;
    public static final int FORBIDDEN = 4031;



}
