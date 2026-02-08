package com.alvesdev.medsched_api.domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alvesdev.medsched_api.domain.model.PatientProfile;

public interface PatientRepository extends JpaRepository<PatientProfile, UUID> {
}
