package com.syndica.backend.domain.mappers;

import com.syndica.backend.domain.dto.UserForCreateDTO;
import com.syndica.backend.domain.dto.UserResponseDTO;
import com.syndica.backend.domain.models.User;

public class UserMapper {
    public static UserResponseDTO toUserResponseDTO(User user){
        return UserResponseDTO.builder()
            .id(user.getId())
            .fullname(user.getFullname())
            .cpf(user.getCpf())
            .email(user.getEmail())
            .isActive(user.isActive())
            .createdAt(user.getCreatedAt())
            .lastLogin(user.getLastLogin())
            .build();
    }

    public static User fromUserForCreateDTOToUser(
        UserForCreateDTO userForCreateDTO,
        String passwordHash
    ){
        return User.builder()
        .fullname(userForCreateDTO.fullname())
        .email(userForCreateDTO.fullname())
        .cpf(userForCreateDTO.cpf())
        .passwordHash(passwordHash)
        .build();
    }

}
