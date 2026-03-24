package com.socialNetwork.server.auth.responses;

public class PendingLoginResponse extends MailResponse {
    public PendingLoginResponse(boolean success, Integer errorCode, String pendingLoginToken) {
        super(success, errorCode , pendingLoginToken);

    }


}