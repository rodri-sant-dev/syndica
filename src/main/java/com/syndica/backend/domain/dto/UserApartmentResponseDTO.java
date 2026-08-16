package com.syndica.backend.domain.dto;

import java.time.Instant;

import lombok.Builder;

@Builder
public record UserApartmentResponseDTO (
    Long Id,
    UserToUserApartmentResponseDTO userApartmentResponse,
    ApartmentResponseDTO apartmentResponse,
    Instant enterMoment,
    Instant exitMoment
){}
