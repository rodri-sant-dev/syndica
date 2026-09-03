package com.syndica.backend.domain.models;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import com.syndica.backend.beans.CpfEncryptor;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
    
    @OneToOne
    @JoinColumn(name="user_avatar_image")
    private Document avatarImage;

    @Column(length=6)
    private String themePreference;

    @Column(nullable = false)
    private String fullname;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name="password_hash", length = 255)
    private String passwordHash;

    @Convert(converter = CpfEncryptor.class)
    @Column(length = 255, nullable = false, unique=true)
    private String cpf;
    
    /* System Controls  */
    @Column(nullable = false)
    @Builder.Default
    private boolean isActive = true;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();

    @Column
    private Instant lastLogin;

    /* System keys */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<UserGroup> userGroups = new HashSet<>();
}