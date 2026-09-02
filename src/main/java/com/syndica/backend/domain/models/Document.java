package com.syndica.backend.domain.models;
import java.time.Instant;
import java.util.UUID;

import org.springframework.data.domain.Persistable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "Document")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Document implements Persistable<UUID> {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Transient
    @Builder.Default
    private boolean newEntity = true;

    @Column(nullable=false)
    private String originalFilename;

    @Column(nullable=false)
    private Long size;

    @Column(name="content_type")
    private String contentType;

    @Column(name="bucket_key", nullable=false)
    private String bucketKey;

    @Column(nullable=false)
    private Instant createdAt;

    @Column
    private Instant deletedAt;

    /* System keys */
    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return newEntity;
    }

    @PostPersist
    @PostLoad
    @SuppressWarnings("unused")
    void markNotNew() {
        newEntity = false;
    }
}
