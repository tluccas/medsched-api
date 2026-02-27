package com.alvesdev.medsched_api.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alvesdev.medsched_api.domain.repositories.PatientRepository;
import com.alvesdev.medsched_api.dto.response.user.PatientDetailResDto;

@Service
public class PatientService {
    @Autowired
    PatientRepository patientRepository;

    public List<PatientDetailResDto> getAllPatients() {
        return patientRepository.findAll().stream()
            .map(patient -> new PatientDetailResDto(
                patient.getUser().getId(),
                patient.getId(),
                patient.getUser().getUsername(),
                patient.getBirthDate(),
                patient.getMedicalHistory()
            ))
            .toList();
    }
}
