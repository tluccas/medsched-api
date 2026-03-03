package com.alvesdev.medsched_api.domain.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alvesdev.medsched_api.domain.model.DoctorProfile;
import com.alvesdev.medsched_api.domain.model.User;
import com.alvesdev.medsched_api.domain.repositories.DoctorRepository;
import com.alvesdev.medsched_api.domain.repositories.UserRepository;
import com.alvesdev.medsched_api.dto.request.profiles.UpdateDoctorRequest;
import com.alvesdev.medsched_api.dto.response.profile.DoctorDetailResponse;
import com.alvesdev.medsched_api.exceptions.UserNotFoundException;

import jakarta.transaction.Transactional;

@Service
@EnableCaching
public class DoctorService {
    
    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    UserRepository userRepository;

    
    public List<DoctorDetailResponse> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable).stream()
            .map(doctor -> new DoctorDetailResponse(
                doctor.getId(),
                doctor.getUser().getUsername(),
                doctor.getSpecialization()
            ))
            .toList();
    }

    public DoctorDetailResponse getDoctorByUserId(UUID uuid) {
         User user = userRepository.findById(uuid)
            .orElseThrow(() -> new UserNotFoundException("This user does not exist."));

        DoctorProfile doctor = user.getDoctorProfile();

        if (doctor == null) {
            throw new UserNotFoundException("This user does not have a doctor profile.");
        }

        return new DoctorDetailResponse(
            doctor.getId(),
            doctor.getUser().getUsername(),
            doctor.getSpecialization()
        );
    }

    @Transactional
    public DoctorDetailResponse update(UUID uuid, UpdateDoctorRequest updateDoctorRequest) {

        User user = userRepository.findById(uuid)
            .orElseThrow(() -> new UserNotFoundException("This user does not exist."));

        DoctorProfile doctorProfile = user.getDoctorProfile();

        if (doctorProfile == null) {
            throw new UserNotFoundException("This user does not have a doctor profile.");
        }

        doctorProfile.setSpecialization(updateDoctorRequest.specialization());
        doctorProfile.setLicenseNumber(updateDoctorRequest.licenseNumber());

        doctorRepository.save(doctorProfile);

        return new DoctorDetailResponse(
            doctorProfile.getId(),
            doctorProfile.getUser().getUsername(),
            doctorProfile.getSpecialization()
        );
    }
}
