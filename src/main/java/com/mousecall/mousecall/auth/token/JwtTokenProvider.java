package com.mousecall.mousecall.auth.token;


import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    private final String SECRET_KEY = "mousecall-secret-key";
    private final long EXPIRATION_TIME = 1000 * 60 * 60 * 2; // 2시간

    public String
}
