package com.syndica.backend.domain.mappers;

import com.syndica.backend.domain.dto.ApartmentResponseDTO;
import com.syndica.backend.domain.dto.UserApartmentResponseDTO;
import com.syndica.backend.domain.models.Apartment;
import com.syndica.backend.domain.models.UserApartment;

public class UserApartmentDTO {
    public static ApartmentResponseDTO toApartmentResponseDTO(Apartment apartment){
        return ApartmentResponseDTO.builder()
            .id(apartment.getId())
            .number(apartment.getNumber())
            .block(apartment.getBlock())
            .floor(apartment.getFloor())
            .build();
    }
    public static UserApartmentResponseDTO toResponseDTO(UserApartment userApartment){
        return UserApartmentResponseDTO.builder()
        .enterMoment(userApartment.getEnterMoment())
        .exitMoment(userApartment.getExitMoment())
        .apartmentResponse(
            UserApartmentDTO.toApartmentResponseDTO(userApartment.getApartment())
        )
        .userApartmentResponse(
            UserMapper.toUserApartmentResponseDTO(userApartment.getUser())
        )
        .build();
    }
}
