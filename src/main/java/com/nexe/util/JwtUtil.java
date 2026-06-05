package com.nexe.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * JWT UTILITY CLASS
 *
 * PURPOSE:
 * - Handles creation, parsing, and validation of JWT tokens
 * - Used in authentication flow (login → generate token → validate on every request)
 */
@Component
public class JwtUtil {

    /**
     * SECRET KEY USED TO SIGN JWT TOKENS
     *
     * PURPOSE:
     * - Ensures token integrity (prevents tampering)
     * - Same key is required to validate token signature
     *
     * NOTE:
     * - In production, this should NOT be hardcoded
     * - Should be stored in environment variables or secrets manager
     */
    private static final String SECRET = "my-very-secure-secret-key-123456";

    /**
     * TOKEN EXPIRATION TIME (1 minute here)
     *
     * PURPOSE:
     * - Defines how long a token remains valid
     * - After this time, token becomes invalid automatically
     *
     * NOTE:
     * - 1 minute is very short (usually 15 min–24 hrs in real apps)
     */
    private static final long EXPIRATION_TIME = 1000 * 60;

    /**
     * SIGNING KEY OBJECT
     *
     * PURPOSE:
     * - Converts SECRET string into cryptographic key
     * - Used for signing and verifying JWT tokens
     */
    private final SecretKey key =
            Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    /**
     * GENERATE JWT TOKEN
     *
     * PURPOSE:
     * - Creates a signed JWT token for authenticated user
     * - Includes username + issued time + expiration time
     *
     * FLOW:
     * - subject() → stores username inside token
     * - issuedAt() → token creation time
     * - expiration() → token validity limit
     * - signWith() → digitally signs token using secret key
     */
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }

    /**
     * EXTRACT USERNAME FROM TOKEN
     *
     * PURPOSE:
     * - Reads JWT payload and extracts the "subject" field
     * - In this case, subject = username
     */
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    /**
     * EXTRACT ALL CLAIMS FROM TOKEN
     *
     * PURPOSE:
     * - Parses JWT token using secret key
     * - Validates signature automatically
     * - Returns payload (claims like username, expiration, etc.)
     *
     * SECURITY:
     * - If token is tampered → parsing fails (exception thrown)
     */
    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * VALIDATE JWT TOKEN
     *
     * PURPOSE:
     * - Ensures token is valid for the given user
     *
     * CHECKS PERFORMED:
     * 1. Username in token matches logged-in user
     * 2. Token is not expired
     * 3. Token is structurally valid (handled by parsing)
     *
     * RETURNS:
     * - true → valid token
     * - false → invalid/expired/tampered token
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            String username = extractUsername(token);

            return username.equals(userDetails.getUsername())
                    && !isTokenExpired(token);

        } catch (Exception e) {
            // Any error (malformed, expired, invalid signature) → token is invalid
            return false;
        }
    }

    /**
     * CHECK IF TOKEN IS EXPIRED
     *
     * PURPOSE:
     * - Compares token expiration time with current time
     * - If current time > expiration → token is invalid
     */
    private boolean isTokenExpired(String token) {
        return extractClaims(token)
                .getExpiration()
                .before(new Date());
    }
}