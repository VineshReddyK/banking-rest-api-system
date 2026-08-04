package com.vinesh.banking.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

// handles token generation, parsing, and validation for the whole app.
// email is the subject claim — every downstream filter and service
// extracts the user identity from here.
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    // TTL in milliseconds, pulled from application.properties
    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public String extractEmail(String token) {
        return getClaims(token).getSubject();
    }

    // catches malformed/expired/tampered tokens in one shot —
    // any jjwt exception means the token is unusable
    public boolean isTokenValid(String token) {
        try {
            Date expiry = getClaims(token).getExpiration();
            return !expiry.before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // base64-decode the configured secret into an HMAC-SHA key.
    // the secret must be at least 256 bits after decoding or jjwt throws.
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
