package com.vinesh.banking.security;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    public String generateToken(String email) {
        return "sample-jwt-token-for-" + email;
    }
}
