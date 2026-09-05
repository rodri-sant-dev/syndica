package com.syndica.backend.domain.dto;

import java.util.UUID;

import lombok.Builder;

@Builder
public record UserResponseDTO(
    UUID id,
    String fullname,
    String email,
    String cpf,
    String themePreference
)
{}
