package com.syndica.backend.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeUserThemeDTO(
    @NotBlank 
    String theme
) {}
