package com.syndica.backend.domain.dto;

import lombok.Builder;

@Builder
public record UserForCreateDTO (
    String fullname,
    String cpf,
    String email,
    String username,
    String password
    
){}
