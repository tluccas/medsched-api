package com.alvesdev.medsched_api.domain.services;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.alvesdev.medsched_api.domain.model.DoctorProfile;
import com.alvesdev.medsched_api.domain.model.PatientProfile;
import com.alvesdev.medsched_api.domain.model.Role;
import com.alvesdev.medsched_api.domain.model.User;
import com.alvesdev.medsched_api.domain.model.enums.RoleType;
import com.alvesdev.medsched_api.domain.repositories.AppointmentRepository;
import com.alvesdev.medsched_api.domain.repositories.DoctorRepository;
import com.alvesdev.medsched_api.domain.repositories.PatientRepository;
import com.alvesdev.medsched_api.domain.repositories.RoleRepository;
import com.alvesdev.medsched_api.domain.repositories.UserRepository;
import com.alvesdev.medsched_api.dto.request.UpdateUserRequest;
import com.alvesdev.medsched_api.dto.request.register.ProfileType;
import com.alvesdev.medsched_api.dto.request.register.RegisterUserReqDto;
import com.alvesdev.medsched_api.dto.response.profile.ProfileDetailResponse;
import com.alvesdev.medsched_api.dto.response.user.ProfileResDto;
import com.alvesdev.medsched_api.dto.response.user.UserDetailResponse;
import com.alvesdev.medsched_api.exceptions.EmailAlreadyExistsException;
import com.alvesdev.medsched_api.exceptions.UserNotFoundException;

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

    @Autowired
    AppointmentRepository appointmentRepository;

    public ProfileDetailResponse getUserProfile(UUID userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        int upcomingAppointmentsCount = 0;
        ProfileDetailResponse profileResponse = null;
    
        if (user.hasRole(RoleType.DOCTOR)) {
            upcomingAppointmentsCount = appointmentRepository.countByDoctorId(userId);
            DoctorProfile doctor = doctorRepository.findByUserId(userId)
                    .orElseThrow(() -> new UserNotFoundException("Doctor profile not found for user ID: " + userId));
            profileResponse = new ProfileDetailResponse(
                user.getUsername(),
                "DOCTOR",
                doctor.getSpecialization(),
                doctor.getLicenseNumber(),
                null,
                null,
                upcomingAppointmentsCount
            );
        } else if (user.hasRole(RoleType.PATIENT)) {
            upcomingAppointmentsCount = appointmentRepository.countByPatientId(userId);
            PatientProfile patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("Patient profile not found for user ID: " + userId));

            profileResponse = new ProfileDetailResponse(
                user.getUsername(),
                "PATIENT",
                null,
                null,
                patient.getMedicalHistory(),
                patient.getBirthDate(),
                upcomingAppointmentsCount
            );
        }
        
        return profileResponse;
    }
    @Transactional
    public UserDetailResponse registerUser(RegisterUserReqDto data) {
        
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

        return new UserDetailResponse(
            newUser.getId(),
            newUser.getUsername(),
            newUser.getEmail(),
            profile
            
        );
    }
    
    // Cacheable here
    public User findById(UUID uuid) {
        return userRepository.findById(uuid)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + uuid));
    }

    @Transactional
    public UserDetailResponse updateUser(UUID uuid, UpdateUserRequest request) {
            User user = findById(uuid);

            if (user.getEmail().equals(request.email()) || userRepository.existsByEmail(request.email())){
                throw new EmailAlreadyExistsException("A user with this email already exists.");
            }

            user.setUsername(request.username());
            user.setEmail(request.email());

            userRepository.save(user);

            return new UserDetailResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                null // Perhaps this is redundant in this case 
            );
    }

    @Transactional
    @PreAuthorize("#uuid == authentication.principal.id")
    public void deleteUser(UUID uuid) {
        User user = findById(uuid);
        
        if (user.hasRole(RoleType.DOCTOR)) {
            Optional.ofNullable(user.getDoctorProfile()).ifPresent(doctor -> {
                appointmentRepository.deleteByDoctorId(doctor.getId());
                doctorRepository.delete(doctor);
            });
        } else if (user.hasRole(RoleType.PATIENT)) {
            Optional.ofNullable(user.getPatientProfile()).ifPresent(patient -> {
                appointmentRepository.deleteByPatientId(patient.getId());
                patientRepository.delete(patient);
            });
        } else {
            throw new UserNotFoundException("User profile not found for user ID: " + uuid);
        }

        userRepository.delete(user);

    }
}
