package com.syndica.backend.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syndica.backend.domain.models.RefreshToken;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}
