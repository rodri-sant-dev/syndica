package com.syndica.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginDTO (
    @NotBlank(message="Username is required")
    String email,

    @NotBlank(message="Password is required")
    String password,

    @NotNull(message="Remember is required")
    Boolean remember
){}
