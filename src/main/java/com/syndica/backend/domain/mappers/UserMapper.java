package com.syndica.backend.domain.mappers;

import com.syndica.backend.domain.dto.UserForCreateDTO;
import com.syndica.backend.domain.dto.UserResponseDTO;
import com.syndica.backend.domain.models.User;

public class UserMapper {
    public static UserResponseDTO toUserResponseDTO(User user){
        return UserResponseDTO.builder()
            .id(user.getId())
            .fullname(user.getFullname())
            .email(user.getEmail())
            .cpf(user.getCpf())
            .themePreference(user.getThemePreference())
            .build();
    }

    public static User fromUserForCreateDTOToUser(
        UserForCreateDTO userForCreateDTO,
        String passwordHash
    ){
        return User.builder()
        .fullname(userForCreateDTO.fullname())
        .email(userForCreateDTO.email())
        .cpf(userForCreateDTO.cpf())
        .themePreference(userForCreateDTO.theme())
        .passwordHash(passwordHash)
        .build();
    }

}
