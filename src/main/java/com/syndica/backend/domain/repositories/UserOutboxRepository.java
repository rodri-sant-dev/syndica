package com.syndica.backend.domain.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syndica.backend.domain.models.UserOutbox;

public interface UserOutboxRepository extends JpaRepository<UserOutbox, Long>{
    List<UserOutbox> findAllByStatus(String status);
}
