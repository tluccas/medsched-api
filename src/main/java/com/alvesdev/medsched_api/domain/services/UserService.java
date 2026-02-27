package com.alvesdev.medsched_api.domain.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.alvesdev.medsched_api.domain.model.DoctorProfile;
import com.alvesdev.medsched_api.domain.model.PatientProfile;
import com.alvesdev.medsched_api.domain.model.Role;
import com.alvesdev.medsched_api.domain.model.User;
import com.alvesdev.medsched_api.domain.model.enums.RoleType;
import com.alvesdev.medsched_api.domain.repositories.DoctorRepository;
import com.alvesdev.medsched_api.domain.repositories.PatientRepository;
import com.alvesdev.medsched_api.domain.repositories.RoleRepository;
import com.alvesdev.medsched_api.domain.repositories.UserRepository;
import com.alvesdev.medsched_api.dto.request.register.ProfileType;
import com.alvesdev.medsched_api.dto.request.register.RegisterUserReqDto;
import com.alvesdev.medsched_api.dto.response.user.ProfileResDto;
import com.alvesdev.medsched_api.dto.response.user.UserDetailResDto;
import com.alvesdev.medsched_api.exceptions.EmailAlreadyExistsException;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    DoctorRepository doctorRepository;

    @Autowired
    PatientRepository patientRepository;
    
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;



    @Transactional
    public UserDetailResDto registerUser(RegisterUserReqDto data) {
        
        if(userRepository.existsByEmail(data.email())) {
            throw new EmailAlreadyExistsException("A user with this email already exists.");
        }

        String encryptedPassword = passwordEncoder.encode(data.password());

        User newUser = new User(
            data.username(),
            data.email(),
            encryptedPassword
        );

        RoleType roleType = (data.profileType() == ProfileType.DOCTOR) ? RoleType.DOCTOR : RoleType.PATIENT;

        Role userRole = roleRepository.findByName(roleType)
            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        
        newUser.getRoles().add(userRole);
        
        userRepository.save(newUser);
        ProfileResDto profile = null;
        if(data.profileType() == ProfileType.DOCTOR) {
            DoctorProfile doctorProfile = new DoctorProfile(
                newUser,
                data.specialization(),
                data.licenseNumber()
            );
            doctorRepository.save(doctorProfile);
            profile = new ProfileResDto(
                ProfileType.DOCTOR,
                null,
                null,
                doctorProfile.getSpecialization(),
                doctorProfile.getLicenseNumber()
            );
        } else if(data.profileType() == ProfileType.PATIENT) {
           PatientProfile patientProfile = new PatientProfile(
                newUser,
                data.birthDate(),
                data.medicalHistory()
            );
            patientRepository.save(patientProfile);
            profile = new ProfileResDto(
                ProfileType.PATIENT,
                patientProfile.getBirthDate(),
                patientProfile.getMedicalHistory(),
                null,
                null
            );
        }

        return new UserDetailResDto(
            newUser.getId(),
            newUser.getUsername(),
            newUser.getEmail(),
            profile
            
        );
    }
}
