package com.alvesdev.medsched_api.domain.services;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import com.alvesdev.medsched_api.exceptions.UserNotFoundException;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.alvesdev.medsched_api.domain.model.PatientProfile;
import com.alvesdev.medsched_api.domain.model.User;
import com.alvesdev.medsched_api.domain.repositories.PatientRepository;
import com.alvesdev.medsched_api.dto.response.user.PatientDetailResponse;
import com.alvesdev.medsched_api.dto.request.profiles.UpdatePatientRequest;

@Service
public class PatientService {
    @Autowired
    PatientRepository patientRepository;

    @Autowired
    UserService userService;

    @Cacheable(value = "PATIENTS_CACHE", key = "'allPatients'")
    public List<PatientDetailResponse> getAllPatients() {
        return patientRepository.findAll().stream()
            .map(patient -> new PatientDetailResponse(
                patient.getUser().getId(),
                patient.getId(),
                patient.getUser().getUsername(),
                patient.getBirthDate().toString(),
                patient.getMedicalHistory()
            ))
            .collect(Collectors.toList());
    }

    @Cacheable(value = "PATIENTS_CACHE", key = "#uuid")
    public PatientDetailResponse getByProfileId(UUID uuid) {
        User user = userService.findById(uuid);
        
        PatientProfile patient = user.getPatientProfile();

        if (patient == null) {
            throw new UserNotFoundException("This user does not have a patient profile.");
        }

        return new PatientDetailResponse(
            patient.getUser().getId(),
            patient.getId(),
            patient.getUser().getUsername(),
            patient.getBirthDate().toString(),
            patient.getMedicalHistory()
        );
    }

    @CacheEvict(value = "PATIENTS_CACHE", key = "#uuid")
    @Transactional
    public PatientDetailResponse update(UUID uuid, UpdatePatientRequest dto){
        User user = userService.findById(uuid);
        
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
            updatedPatient.getBirthDate().toString(),
            updatedPatient.getMedicalHistory()
        );
    }

}
