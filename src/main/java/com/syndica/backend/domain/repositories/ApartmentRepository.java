package com.syndica.backend.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syndica.backend.domain.models.Apartment;

public interface ApartmentRepository extends JpaRepository<Apartment, Long>{
    
}
