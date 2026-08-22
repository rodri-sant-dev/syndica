package com.syndica.backend.domain.dto;

import java.time.Instant;
import java.util.UUID;

import lombok.Builder;

@Builder
public record UserResponseDTO(
    UUID id,
    String username,
    String fullname,
    String email,
    String cpf,
    boolean isActive,
    Instant createdAt,
    Instant lastLogin
)
{}
