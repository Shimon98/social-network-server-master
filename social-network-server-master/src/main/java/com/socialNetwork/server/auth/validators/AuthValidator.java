package com.socialNetwork.server.auth.validators;

import com.socialNetwork.server.auth.requests.LoginRequest;
import com.socialNetwork.server.auth.requests.RegisterRequest;
import com.socialNetwork.server.auth.utils.Constants;
import com.socialNetwork.server.auth.utils.Errors;

public class AuthValidator {

    private AuthValidator() {
    }

    public static Integer validateLoginRequest(LoginRequest request) {
        if (request == null) {
            return Errors.INVALID_REQUEST;
        }
        Integer errorCode = validateUsername(request.getUsername());
        if (errorCode != null) {
            return errorCode;
        }
        return validatePassword(request.getPassword());
    }

    public static Integer validateRegisterRequest(RegisterRequest request) {
        if (request == null) {
            return Errors.INVALID_REQUEST;
        }
        Integer errorCode = validateUsername(request.getUsername());
        if (errorCode != null) {
            return errorCode;
        }
        errorCode = validateEmail(request.getEmail());
        if (errorCode != null) {
            return errorCode;
        }
        return validatePassword(request.getPassword());
    }

    private static Integer validateUsername(String username) {
        Integer missingFieldError = validateRequiredField(username, Errors.MISSING_USERNAME);
        if (missingFieldError != null) {
            return missingFieldError;
        }
        String normalizedUsername = username.trim();
        if (!isLengthInRange(normalizedUsername, Constants.MIN_USERNAME_LENGTH,
                Constants.MAX_USERNAME_LENGTH)) {
            return Errors.INVALID_USERNAME;
        }
        return null;
    }

    private static Integer validateEmail(String email) {
        Integer missingFieldError = validateRequiredField(email, Errors.MISSING_EMAIL);
        if (missingFieldError != null) {
            return missingFieldError;
        }
        String normalizedEmail = email.trim();
        if (!normalizedEmail.contains(Constants.EMAIL_AT_SIGN) ||
                !normalizedEmail.contains(Constants.EMAIL_DOT)) {
            return Errors.INVALID_EMAIL;
        }
        return null;
    }

    private static Integer validatePassword(String password) {
        Integer missingFieldError = validateRequiredField(password, Errors.MISSING_PASSWORD);
        if (missingFieldError != null) {
            return missingFieldError;
        }
        String normalizedPassword = password.trim();
        if (normalizedPassword.length() < Constants.MIN_PASSWORD_LENGTH) {
            return Errors.INVALID_PASSWORD;
        }
        return null;
    }

    private static Integer validateRequiredField(String value, int missingErrorCode) {
        if (value == null) {
            return missingErrorCode;
        }
        if (value.trim().isEmpty()) {
            return missingErrorCode;
        }
        return null;
    }

    private static boolean isLengthInRange(String value, int minLength, int maxLength) {
        return value.length() >= minLength && value.length() <= maxLength;
    }
}