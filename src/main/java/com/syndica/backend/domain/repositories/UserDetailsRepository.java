package com.syndica.backend.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syndica.backend.domain.models.UserDetails;


public interface UserDetailsRepository extends JpaRepository<UserDetails, Long>{}
