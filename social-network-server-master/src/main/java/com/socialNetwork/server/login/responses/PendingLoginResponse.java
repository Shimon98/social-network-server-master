package com.socialNetwork.server.login.responses;

public class PendingLoginResponse extends MailResponse {
    public PendingLoginResponse(boolean success, Integer errorCode, String pendingLoginToken) {
        super(success, errorCode , pendingLoginToken);

    }


}