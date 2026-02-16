package jp.github.minamoto.m.reservationsystem.security.auth.jwt;

import java.util.Date;

import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private static final String SECRET = "my-secret-key-for-jwt-must-be-32bytes-long!!";

    public String generateToken(String username, String role) {
        return Jwts.builder()
        .subject(username)
        .claim("role", role)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
        .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
        .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }
}
