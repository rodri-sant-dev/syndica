package com.syndica.backend.domain.repositories;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.syndica.backend.domain.models.Group;
import com.syndica.backend.domain.models.User;


public interface UserRepository extends JpaRepository<User, UUID>{
    public Optional<User> getByEmail(String email);
    
    @Query("SELECT ug.group FROM UserGroup ug WHERE ug.user.id = :userId")
    List<Group> findGroupsByUserId(@Param("userId") UUID userId);
    @Modifying
    @Query("""
        UPDATE RefreshToken rt
        SET rt.revokedAt = :revokedAt, rt.reason = 'OUTER_LOGIN'
        WHERE rt.user.id = :userId AND rt.revokedAt IS NULL
        """)
    void revokeOutersRefreshTokens(@Param("userId") UUID userId, @Param("revokedAt") Instant revokedAt);
}
