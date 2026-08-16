package com.syndica.backend.domain.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syndica.backend.domain.models.UserApartment;


public interface  UserApartmentRepository extends JpaRepository<UserApartment, Long> {
    boolean existsByApartmentIdAndExitMomentIsNull(Long apartmentId);
    Optional<UserApartment> findByUserIdAndApartmentId(UUID userId, Long apartmentId);
    Optional<UserApartment> findByUserIdAndApartmentIdAndExitMomentIsNull(UUID userId, Long apartmentId);
    
}
