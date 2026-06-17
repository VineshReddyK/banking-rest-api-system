package com.vinesh.banking.security;

import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

    // NOTE: In production, replace with a proper JJWT/Nimbus implementation
    // using a secret key and real JWT signing/verification.

    public String generateToken(String email) {
        return "sample-jwt-token-for-" + email;
    }

    public String extractEmail(String token) {
        if (token != null && token.startsWith("sample-jwt-token-for-")) {
            return token.substring("sample-jwt-token-for-".length());
        }
        return null;
    }
}
