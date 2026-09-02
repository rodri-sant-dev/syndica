package com.syndica.backend.domain.dto;

import lombok.Builder;

@Builder
public record UserForCreateDTO (
    String fullname,
    String email,
    String password,
    String cpf,
    String theme
){}
