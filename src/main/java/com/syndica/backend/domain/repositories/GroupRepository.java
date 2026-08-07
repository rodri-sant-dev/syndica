package com.syndica.backend.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syndica.backend.domain.models.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
    
}
