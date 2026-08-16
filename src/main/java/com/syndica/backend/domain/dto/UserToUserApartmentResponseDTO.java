package com.syndica.backend.domain.dto;

import java.net.URI;

import lombok.Builder;

@Builder
public record UserToUserApartmentResponseDTO (
    String fullname,
    URI URI
){}
