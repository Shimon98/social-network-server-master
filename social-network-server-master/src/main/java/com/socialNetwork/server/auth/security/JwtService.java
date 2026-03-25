package com.socialNetwork.server.auth.security;

import com.socialNetwork.server.auth.utils.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    private final JwtConfig jwtConfig;

    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecret());
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateAccessToken(Long userId, String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtConfig.getAccessExpiration());

        return Jwts.builder()
                .subject(username)
                .claim(Constants.USER_ID, userId)
                .claim(Constants.TYPE, Constants.ACCESS)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(Long userId, String username) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtConfig.getRefreshExpiration());

        return Jwts.builder()
                .subject(username)
                .claim(Constants.USER_ID, userId)
                .claim(Constants.TYPE, Constants.REFRESH)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }


    public String generatePendingLoginToken(Long userId, String username, String email) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtConfig.getPendingLoginExpiration());

        return Jwts.builder()
                .subject(username)
                .claim(Constants.USER_ID, userId)
                .claim(Constants.EMAIL, email)
                .claim(Constants.TYPE, Constants.PENDING_LOGIN)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    public String generatePendingRegisterToken(String email) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + jwtConfig.getPendingRegisterExpiration());

        return Jwts.builder()
                .subject(email)
                .claim(Constants.EMAIL, email)
                .claim(Constants.TYPE, Constants.PENDING_REGISTER)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    public String extractEmail(String token) {
        return extractAllClaims(token).get(Constants.EMAIL, String.class);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Long extractUserId(String token) {
        return extractAllClaims(token).get(Constants.USER_ID, Long.class);
    }

    public String extractTokenType(String token) {
        return extractAllClaims(token).get(Constants.TYPE, String.class);
    }

    public Long extractExpirationTime(String token) {
        return extractAllClaims(token).getExpiration().getTime();
    }

    public boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public boolean isTokenValid(String token) {
        try {
            return !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}