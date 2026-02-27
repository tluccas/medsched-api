package com.alvesdev.medsched_api.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alvesdev.medsched_api.domain.model.Role;
import com.alvesdev.medsched_api.domain.model.enums.RoleType;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleType name);
}
