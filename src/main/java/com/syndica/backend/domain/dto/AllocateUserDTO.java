package com.syndica.backend.domain.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AllocateUserDTO (
    @NotNull(message="UserId is required")
    UUID userId,
    @NotNull(message="apartmentId is required")
    Long apartmentId
){}
