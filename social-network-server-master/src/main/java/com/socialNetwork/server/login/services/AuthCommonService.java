package com.socialNetwork.server.login.services;

import com.socialNetwork.server.login.dataBase.DBManager;
import com.socialNetwork.server.login.entity.User;
import com.socialNetwork.server.login.hashing.PasswordHashUtil;
import com.socialNetwork.server.login.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthCommonService {
    @Autowired
    private JwtService jwtService;
    @Autowired
     private DBManager dbManager;

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

    public User extractUserFromToken(String token) {
        String usernameFromToken = jwtService.extractUsername(token);
        String normalizedUsername = normalizeUsername(usernameFromToken);
        User user = dbManager.findUserByUsername(normalizedUsername);
        return user;
    }
    public String extractEmailFromToken(String token) {
        String emailFromToken = jwtService.extractEmail(token);
        return emailFromToken;
    }

    public boolean validateToken(String token) {
        if ( token == null) {
            return false;}
        if (!jwtService.isTokenValid(token)) {
            return false;     }
        if (!"pending_login".equals(jwtService.extractTokenType(token))) {
            return false;}
        return true;
    }

    public User getAuthUserFormToken(String token) {
        if (!validateToken(token)) {
            return null;
        }
        User user = extractUserFromToken(token);
        if (!authTokenForLogin(token, user)) {
            return null;
        }
        return user;
    }

    private boolean authTokenForLogin(String token, User user) {
        if (user == null) {
            return false;
        }
        String emailFromToken =extractEmailFromToken(token);
        if (!emailFromToken.equalsIgnoreCase(user.getEmail())) {
            return  false;
        }
        return true;
    }

}