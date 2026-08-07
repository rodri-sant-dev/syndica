package com.syndica.backend.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.syndica.backend.domain.dto.LoginDTO;
import com.syndica.backend.domain.dto.TokenPair;
import com.syndica.backend.domain.models.RefreshToken;
import com.syndica.backend.domain.models.User;
import com.syndica.backend.domain.repositories.RefreshTokenRepository;
import com.syndica.backend.domain.repositories.UserRepository;

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

    public TokenPair login(User user) {
        var accessToken = jwtService.generateAccessToken(user);

        var jti = UUID.randomUUID().toString();
        var refreshToken = jwtService.generateRefreshToken(user, jti);

        var entity = RefreshToken.builder()
            .jti(jti)
            .user(user)
            .expiresAt(Instant.now().plus(jwtService.getRefreshTokenExpiration()))
            .build();

        refreshTokenRepository.save(entity);

        return new TokenPair(accessToken, refreshToken);
    }

    public Optional<User> userIsValid(LoginDTO loginDTO) {
    return userRepository.getByUsername(loginDTO.username())
            .filter(user ->
                passwordEncoder.matches(
                    loginDTO.password(),
                    user.getPasswordHash()
                )
            );
}
}