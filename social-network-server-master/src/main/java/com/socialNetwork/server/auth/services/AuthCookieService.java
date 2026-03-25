package com.socialNetwork.server.auth.services;

import com.socialNetwork.server.auth.security.JwtConfig;
import com.socialNetwork.server.auth.utils.Constants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import static com.socialNetwork.server.auth.utils.Constants.COOKIE_VALUE;
import static com.socialNetwork.server.auth.utils.Constants.SECOND;

@Service
public class AuthCookieService {

    private final JwtConfig jwtConfig;

    public AuthCookieService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public void addAccessTokenCookie(HttpServletResponse response, String token) {
        addCookie(
                response,
                Constants.ACCESS_COOKIE_NAME,
                token,
                jwtConfig.getAccessExpiration() / SECOND
        );
    }

    public void addRefreshTokenCookie(HttpServletResponse response, String token) {
        addCookie(
                response,
                Constants.REFRESH_COOKIE_NAME,
                token,
                jwtConfig.getRefreshExpiration() / SECOND
        );
    }

    public void clearAuthCookies(HttpServletResponse response) {
        expireCookie(response, Constants.ACCESS_COOKIE_NAME);
        expireCookie(response, Constants.REFRESH_COOKIE_NAME);
    }

    public String getAccessTokenFromCookies(HttpServletRequest request) {
        return getCookieValue(request, Constants.ACCESS_COOKIE_NAME);
    }

    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        return getCookieValue(request, Constants.REFRESH_COOKIE_NAME);
    }

    public String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookieName.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }

    private void addCookie(HttpServletResponse response, String cookieName, String cookieValue, long maxAgeSeconds) {
        ResponseCookie cookie = buildCookie(cookieName, cookieValue, maxAgeSeconds);
        response.addHeader(Constants.SET_COOKIE_HEADER, cookie.toString());
    }

    private void expireCookie(HttpServletResponse response, String cookieName) {
        addCookie(response, cookieName, COOKIE_VALUE, Constants.COOKIE_DELETE_MAX_AGE);
    }

    private ResponseCookie buildCookie(String cookieName, String cookieValue, long maxAgeSeconds) {
        return ResponseCookie.from(cookieName, cookieValue)
                .httpOnly(Constants.COOKIE_HTTP_ONLY)
                .secure(Constants.COOKIE_SECURE)
                .path(Constants.COOKIE_PATH)
                .maxAge(maxAgeSeconds)
                .sameSite(Constants.COOKIE_SAME_SITE)
                .build();
    }
}