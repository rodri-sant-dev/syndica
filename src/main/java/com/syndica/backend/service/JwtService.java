package com.syndica.backend.service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.syndica.backend.domain.models.Group;
import com.syndica.backend.domain.models.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final Duration accessTokenExpiration;
    private final Duration refreshTokenExpiration;
    private final UserService userService;

    public JwtService(
        UserService userService,
        @Value("${jwt.secret}") String secret,
        @Value("${jwt.access-token-expiration}") Duration accessTokenExpiration,
        @Value("${jwt.refresh-token-expiration}") Duration refreshTokenExpiration
    ) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.userService = userService;
    }

    public String generateAccessToken(User user) {
        List<String> roles = userService.listGroups(user).stream()
            .map(Group::getName)
            .toList();

        return Jwts.builder()
            .subject(user.getId().toString())
            .claim("roles", roles)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plus(accessTokenExpiration)))
            .signWith(signingKey)
            .compact();
    }

    public String generateRefreshToken(User user, String jti) {
        return Jwts.builder()
            .subject(user.getId().toString())
            .id(jti)
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plus(refreshTokenExpiration)))
            .signWith(signingKey)
            .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(Date.from(Instant.now()));
    }

    public Duration getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
}