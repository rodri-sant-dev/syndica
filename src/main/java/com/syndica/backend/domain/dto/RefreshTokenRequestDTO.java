package com.syndica.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record  RefreshTokenRequestDTO (
    @NotBlank
    String refreshToken
){}
