package com.syndica.backend.domain.dto;

public record LoginResponseDTO(
    UserResponseDTO user,
    TokenPairDTO tokens
) {}
