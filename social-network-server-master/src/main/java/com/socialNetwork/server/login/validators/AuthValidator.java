package com.socialNetwork.server.login.validators;

import com.socialNetwork.server.login.responses.RegisterRequest;

public class AuthValidator {

    private AuthValidator() {
    }

    public static String validateRegisterRequest(RegisterRequest request) {
        if (request == null) {
            return "Request is empty";
        }

        if (!isValidUsername(request.getUsername())) {
            return "Invalid username";
        }

        if (!isValidEmail(request.getEmail())) {
            return "Invalid email";
        }

        if (!isValidPassword(request.getPassword())) {
            return "Invalid password";
        }

        return null;
    }

    private static boolean isValidUsername(String username) {
        if (username == null) {
            return false;
        }

        username = username.trim();

        if (username.isEmpty()) {
            return false;
        }

        return username.length() >= 3 && username.length() <= 20;
    }

    private static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }

        email = email.trim();

        if (email.isEmpty()) {
            return false;
        }

        return email.contains("@") && email.contains(".");
    }

    private static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }

        password = password.trim();

        if (password.isEmpty()) {
            return false;
        }

        return password.length() >= 6;
    }
}