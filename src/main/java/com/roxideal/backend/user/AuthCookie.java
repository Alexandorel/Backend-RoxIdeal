package com.roxideal.backend.user;

import org.springframework.http.ResponseCookie;

import java.time.Duration;

public final class AuthCookie {

    public static final String NAME = "access_token";

    private AuthCookie() {
    }

    public static ResponseCookie create(String token, Duration maxAge) {
        return ResponseCookie.from(NAME, token)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api")
                .maxAge(maxAge)
                .build();
    }

    public static ResponseCookie clear() {
        return create("", Duration.ZERO);
    }
}
