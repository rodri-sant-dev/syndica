package com.syndica.backend.domain.mappers;

import com.syndica.backend.domain.models.User;
import com.syndica.backend.domain.dto.UserResponseDTO;

public class UserMapper {
    public static UserResponseDTO toUserResponseDTO(User user){
        return UserResponseDTO.builder()
            .id(user.getId())
            .username(user.getUsername())
            .fullname(user.getFullname())
            .cpf(user.getCpf())
            .isActive(user.isActive())
            .createdAt(user.getCreatedAt())
            .lastLogin(user.getLastLogin())
            .build();
    }
}
