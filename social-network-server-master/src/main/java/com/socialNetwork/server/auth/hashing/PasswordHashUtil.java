package com.socialNetwork.server.auth.hashing;

import com.socialNetwork.server.auth.utils.Constants;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHashUtil {

    private PasswordHashUtil() {
    }

    public static String hashPassword(String username, String password) {
        String source = username + password;

        try {
            MessageDigest digest = MessageDigest.getInstance(Constants.ALGORITHM);
            byte[] hashBytes = digest.digest(source.getBytes(StandardCharsets.UTF_8));

            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                hashString.append(String.format(Constants.FORMAT, b));
            }

            return hashString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(Constants.HASH_ERROR, e);
        }
    }
}