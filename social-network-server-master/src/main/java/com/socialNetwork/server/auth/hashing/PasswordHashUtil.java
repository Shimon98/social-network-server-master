package com.socialNetwork.server.auth.hashing;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHashUtil {

    private PasswordHashUtil() {
    }

    public static String hashPassword(String username, String password) {
        String source = username + password;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(source.getBytes(StandardCharsets.UTF_8));

            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                hashString.append(String.format("%02x", b));
            }

            return hashString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash error", e);
        }
    }
}