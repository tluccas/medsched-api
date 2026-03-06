package com.alvesdev.medsched_api.domain.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alvesdev.medsched_api.domain.model.DoctorProfile;
import com.alvesdev.medsched_api.domain.model.User;
import com.alvesdev.medsched_api.domain.repositories.DoctorRepository;
import com.alvesdev.medsched_api.dto.request.profiles.UpdateDoctorRequest;
import com.alvesdev.medsched_api.dto.response.PaginatedResponse;
import com.alvesdev.medsched_api.dto.response.profile.DoctorDetailResponse;
import com.alvesdev.medsched_api.exceptions.UserNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class DoctorService {
    
    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    UserService userService;

    @Cacheable(
        value = "DOCTORS_CACHE", 
        key = "'allDoctors' + #pageable.pageNumber + ':' + #pageable.pageSize",
        condition = "#specialization == null")
    public PaginatedResponse<DoctorDetailResponse> getAllDoctors(String specialization, Pageable pageable) {
        
        Page<DoctorProfile> page;

        if(specialization != null) {
           page = doctorRepository.findBySpecialization(specialization, pageable);
        } else {
            page = doctorRepository.findAll(pageable);
        }

        Page<DoctorDetailResponse> dtoPage = page.map(doctor -> new DoctorDetailResponse(
            doctor.getId(),
            doctor.getUser().getUsername(),
            doctor.getSpecialization()
        ));

        return new PaginatedResponse<>(
            dtoPage.getContent(), 
            dtoPage.getNumber(), 
            dtoPage.getSize(), 
            dtoPage.getTotalElements(), 
            dtoPage.getTotalPages()
        );
    }

    @Cacheable(value = "DOCTORS_CACHE", key = "#uuid")
    public DoctorDetailResponse getDoctorByUserId(UUID uuid) {
         User user = userService.findById(uuid);

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
    @CacheEvict(value = "DOCTORS_CACHE", key = "#uuid")
    public DoctorDetailResponse update(UUID uuid, UpdateDoctorRequest updateDoctorRequest) {

        User user = userService.findById(uuid);

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
