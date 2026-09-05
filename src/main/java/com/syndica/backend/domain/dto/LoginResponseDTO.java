package com.syndica.backend.domain.dto;

import java.net.URI;

import lombok.Builder;

@Builder
public record LoginResponseDTO(
    URI imageURI,
    UserResponseDTO user,
    TokenPairDTO tokens
) {}
