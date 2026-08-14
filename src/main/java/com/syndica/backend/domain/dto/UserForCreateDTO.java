package com.syndica.backend.domain.dto;

import lombok.Builder;

@Builder
public record UserForCreateDTO (
    String username,
    String password,
    String fullname,
    String cpf
){}
