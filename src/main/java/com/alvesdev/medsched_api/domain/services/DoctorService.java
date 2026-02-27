package com.alvesdev.medsched_api.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alvesdev.medsched_api.domain.repositories.DoctorRepository;
import com.alvesdev.medsched_api.dto.response.user.DoctorDetailResDto;

@Service
public class DoctorService {
    
    @Autowired
    DoctorRepository doctorRepository;

    public List<DoctorDetailResDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
            .map(doctor -> new DoctorDetailResDto(
                doctor.getUser().getId(),
                doctor.getId(),
                doctor.getUser().getUsername(),
                doctor.getSpecialization()
            ))
            .toList();
    }
}
