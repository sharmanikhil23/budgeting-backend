package com.budgeting.backend.global;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class JWT {

    private final SecretKey key;
    private final long accessExpiration;
    private final long refreshExpiration;

    // Constructor name must match the Class name (JWT)
    public JWT(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessExpiration,
            @Value("${jwt.refresh-token-expiration}") long refreshExpiration) {

        // Use this if your secret in YAML is a plain string:
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        // Use this instead if your secret in YAML is Base64 encoded:
        // this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        this.accessExpiration = accessExpiration;
        this.refreshExpiration = refreshExpiration;
    }

    /**
     * Generates both tokens and returns them in a Map.
     * In 2025 (JJWT 0.12+), we use 'builder()' and 'signWith(key)' without deprecated constants.
     */
    public Map<String, String> generateTokens(String email) {
        String accessToken = Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessExpiration))
                .signWith(key) // JJWT automatically detects HS256 from the key type
                .compact();

        String refreshToken = Jwts.builder()
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshExpiration))
                .signWith(key)
                .compact();

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return tokens;
    }

    public String extractEmail(String token) {
        return Jwts.parser()
                .verifyWith(key) // New method in 0.12.x instead of setSigningKey
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}