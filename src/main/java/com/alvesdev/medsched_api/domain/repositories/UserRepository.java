package com.alvesdev.medsched_api.domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.alvesdev.medsched_api.domain.model.User;

public interface UserRepository extends JpaRepository<User, UUID> {
    
    UserDetails findByEmail(String email);

    boolean existsByEmail(String email);
}
