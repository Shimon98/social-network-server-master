package com.socialNetwork.server.login.services;

import com.socialNetwork.server.login.entity.User;
import com.socialNetwork.server.login.hashing.PasswordHashUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthCommonService {

    public String normalizeUsername(String username) {
        return username.trim();
    }

    public String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    public String normalizePassword(String password) {
        return password.trim();
    }

    public String hashPassword(String username, String password) {
        return PasswordHashUtil.hashPassword(username, password);
    }

    public boolean isPasswordMatch(String username, String password, String savedPasswordHash) {
        String requestPasswordHash = hashPassword(username, password);
        return requestPasswordHash.equals(savedPasswordHash);
    }

    public User createUser(String username, String email, String passwordHash) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        return user;
    }
}