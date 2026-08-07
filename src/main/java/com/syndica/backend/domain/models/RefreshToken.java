package com.syndica.backend.domain.models;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
    name = "refresh_tokens",
    indexes = {
        @Index(name = "idx_refresh_tokens_token_hash", columnList = "token_hash"),
        @Index(name = "idx_refresh_tokens_user_id", columnList = "user_id"),
        @Index(name = "idx_refresh_tokens_user_revoked", columnList = "user_id, revoked_at")
    }
)
@Getter
@Setter
@Builder
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jti", nullable = false, unique = true, length = 36)
    private String jti;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "replaced_by", unique = true)
    private RefreshToken replacedBy;

    @Column(name = "reason", length = 255)
    private String reason;

    @PrePersist
    protected void onCreate() {
        if (issuedAt == null) {
            issuedAt = Instant.now();
        }
    }

}