package com.alvesdev.medsched_api.domain.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.alvesdev.medsched_api.domain.repositories.DoctorRepository;
import com.alvesdev.medsched_api.dto.response.user.DoctorDetailResDto;

@Service
@EnableCaching
public class DoctorService {
    
    @Autowired
    DoctorRepository doctorRepository;

    
    public List<DoctorDetailResDto> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable).stream()
            .map(doctor -> new DoctorDetailResDto(
                doctor.getUser().getId(),
                doctor.getId(),
                doctor.getUser().getUsername(),
                doctor.getSpecialization()
            ))
            .toList();
    }
}
