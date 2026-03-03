package com.alvesdev.medsched_api.domain.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.alvesdev.medsched_api.exceptions.UserNotFoundException;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.alvesdev.medsched_api.domain.model.PatientProfile;
import com.alvesdev.medsched_api.domain.model.User;
import com.alvesdev.medsched_api.domain.repositories.PatientRepository;
import com.alvesdev.medsched_api.dto.response.user.PatientDetailResponse;
import com.alvesdev.medsched_api.domain.repositories.UserRepository;
import com.alvesdev.medsched_api.dto.request.profiles.UpdatePatientRequest;

@Service
public class PatientService {
    @Autowired
    PatientRepository patientRepository;

    @Autowired
    UserRepository userRepository;

    public List<PatientDetailResponse> getAllPatients() {
        return patientRepository.findAll().stream()
            .map(patient -> new PatientDetailResponse(
                patient.getUser().getId(),
                patient.getId(),
                patient.getUser().getUsername(),
                patient.getBirthDate(),
                patient.getMedicalHistory()
            ))
            .toList();
    }

    public PatientDetailResponse getByProfileId(UUID uuid) {
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new UserNotFoundException("This user does not exist."));
        
        PatientProfile patient = user.getPatientProfile();

        if (patient == null) {
            throw new UserNotFoundException("This user does not have a patient profile.");
        }

        return new PatientDetailResponse(
            patient.getUser().getId(),
            patient.getId(),
            patient.getUser().getUsername(),
            patient.getBirthDate(),
            patient.getMedicalHistory()
        );
    }

    @Transactional
    public PatientDetailResponse update(UUID uuid, UpdatePatientRequest dto){
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new UserNotFoundException("This user does not exist."));
        
        PatientProfile patient = user.getPatientProfile();

        if (patient == null) {
            throw new UserNotFoundException("This user does not have a patient profile.");
        }

        patient.setBirthDate(dto.birthDate());
        patient.setMedicalHistory(dto.medicalHistory());

        PatientProfile updatedPatient = patientRepository.save(patient);

        return new PatientDetailResponse(
            updatedPatient.getUser().getId(),
            updatedPatient.getId(),
            updatedPatient.getUser().getUsername(),
            updatedPatient.getBirthDate(),
            updatedPatient.getMedicalHistory()
        );
    }

}
