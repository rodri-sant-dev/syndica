package com.syndica.backend.domain.mappers;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.syndica.backend.domain.dto.UserResponseDTO;
import com.syndica.backend.domain.dto.UserToUserApartmentResponseDTO;
import com.syndica.backend.domain.models.User;

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

    public static UserToUserApartmentResponseDTO toUserApartmentResponseDTO(User user){
        return UserToUserApartmentResponseDTO.builder()
            .fullname(user.getFullname())
            .URI(
                ServletUriComponentsBuilder.fromCurrentRequestUri()
                    .path("/{userId}")
                    .buildAndExpand(user.getId())
                    .toUri()
            )    
            .build();
    }
}
