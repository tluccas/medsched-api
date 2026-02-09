package com.alvesdev.medsched_api.domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alvesdev.medsched_api.domain.model.DoctorProfile;

public interface DoctorRepository extends JpaRepository<DoctorProfile, UUID>{
    
}
