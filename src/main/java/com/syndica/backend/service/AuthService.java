package com.syndica.backend.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.syndica.backend.domain.dto.LoginDTO;
import com.syndica.backend.domain.dto.RefreshTokenRequestDTO;
import com.syndica.backend.domain.dto.TokenPairDTO;
import com.syndica.backend.domain.models.RefreshToken;
import com.syndica.backend.domain.models.User;
import com.syndica.backend.domain.repositories.RefreshTokenRepository;
import com.syndica.backend.domain.repositories.UserRepository;
import com.syndica.backend.execptions.InvalidTokenException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public AuthService(
        JwtService jwtService,
        RefreshTokenRepository refreshTokenRepository,
        PasswordEncoder passwordEncoder,
        UserRepository userRepository
    ) {
        this.jwtService = jwtService;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional
    public TokenPairDTO login(User user, Boolean remenber) {
        userRepository.revokeOutersRefreshTokens(user.getId(), Instant.now());

        String accessToken = jwtService.generateAccessToken(user);

        String jti = UUID.randomUUID().toString();
        String refreshToken = jwtService.generateRefreshToken(user, jti, remenber);

        RefreshToken entity = RefreshToken.builder()
            .jti(jti)
            .user(user)
            .expiresAt(
                remenber ? null : Instant.now().plus(jwtService.getRefreshTokenExpiration())
            )
            .build();

        refreshTokenRepository.save(entity);
        user.setLastLogin(Instant.now());

        return new TokenPairDTO(accessToken, refreshToken);
    }

    @Transactional
    public TokenPairDTO refreshToken(RefreshTokenRequestDTO refreshTokenRequestDTO){
        Claims claims;
        try {
            claims = jwtService.extractClaims(refreshTokenRequestDTO.refreshToken());

        } catch (JwtException e) {
            throw new InvalidTokenException();
        }

        RefreshToken oldRefreshToken = refreshTokenRepository.
        findByJtiAndRevokedAtIsNull(claims.getId())
        .orElseThrow(
            () -> new InvalidTokenException("Token is invalid")
        );

        User user = oldRefreshToken.getUser();
        String jti = UUID.randomUUID().toString();
        Boolean remenber = oldRefreshToken.getExpiresAt() == null;

        String refreshTokenString = jwtService.generateRefreshToken(
            user, jti, remenber
        );
        
        RefreshToken refreshTokeEntity = RefreshToken.builder()
        .jti(jti)
        .user(user)
        .expiresAt(
            remenber ? null : Instant.now().plus(jwtService.getRefreshTokenExpiration())
        )
        .build();

        refreshTokeEntity = refreshTokenRepository.save(refreshTokeEntity);

        oldRefreshToken.setRevokedAt(Instant.now());
        oldRefreshToken.setReason("REFRESH_TOKEN");
        oldRefreshToken.setReplacedBy(refreshTokeEntity);
        
        String accessToken = jwtService.generateAccessToken(user);

        return new TokenPairDTO(accessToken, refreshTokenString);
    }

    public Optional<User> userIsValid(LoginDTO loginDTO) {
    return userRepository.getByEmail(loginDTO.email())
            .filter(user ->
                passwordEncoder.matches(
                    loginDTO.password(),
                    user.getPasswordHash()
                )
            )
            .filter(user -> user.isActive());
    }

    @Transactional
    public void blacklistToken(RefreshTokenRequestDTO refreshTokenRequestDTO) {
        Claims claims;
        try {
            claims = jwtService.extractClaims(refreshTokenRequestDTO.refreshToken());

        } catch (JwtException e) {
            throw new InvalidTokenException();
        }

        RefreshToken refreshToken = refreshTokenRepository.findByJtiAndRevokedAtIsNull(claims.getId())
        .orElseThrow(
            () -> new InvalidTokenException("Token is invalid")
        );
        
        refreshToken.setRevokedAt(Instant.now());
        refreshToken.setReason("LOGOUT");

    }
}