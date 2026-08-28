package com.syndica.backend.domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syndica.backend.domain.models.Document;

public interface DocumentRepository extends JpaRepository<Document, UUID> {
    
}
