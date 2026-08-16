package com.syndica.backend.domain.dto;

import lombok.Builder;


@Builder
public record ApartmentResponseDTO (
    Long id,
    Integer number,
    Integer block,
    Integer floor
){}
