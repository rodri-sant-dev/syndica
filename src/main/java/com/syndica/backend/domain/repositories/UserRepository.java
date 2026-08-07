package com.syndica.backend.domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syndica.backend.domain.models.User;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, UUID>{
    public Optional<User> getByUsername(String username);
}
