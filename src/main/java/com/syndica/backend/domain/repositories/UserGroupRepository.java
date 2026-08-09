package com.syndica.backend.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.syndica.backend.domain.models.UserGroup;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
}
