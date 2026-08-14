package com.syndica.backend.domain.models;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @UuidGenerator
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String username;

    
    @Column(nullable = false)
    private String fullname;

    @Column(name="password_hash", nullable = false)
    private String passwordHash;

    @Column(length = 11, nullable = false, unique=true)
    private String cpf;

    @Column(nullable = false)
    @Builder.Default
    private boolean isActive = true;

    @Column
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column
    private Instant lastLogin;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<UserGroup> userGroups = new HashSet<>();
}