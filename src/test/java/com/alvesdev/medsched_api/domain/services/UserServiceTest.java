package com.alvesdev.medsched_api.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.alvesdev.medsched_api.domain.model.DoctorProfile;
import com.alvesdev.medsched_api.domain.model.Role;
import com.alvesdev.medsched_api.domain.model.User;
import com.alvesdev.medsched_api.domain.model.enums.RoleType;
import com.alvesdev.medsched_api.domain.repositories.DoctorRepository;
import com.alvesdev.medsched_api.domain.repositories.PatientRepository;
import com.alvesdev.medsched_api.domain.repositories.RoleRepository;
import com.alvesdev.medsched_api.domain.repositories.UserRepository;
import com.alvesdev.medsched_api.dto.request.register.ProfileType;
import com.alvesdev.medsched_api.dto.request.register.RegisterUserReqDto;
import com.alvesdev.medsched_api.dto.response.user.UserDetailResDto;
import com.alvesdev.medsched_api.exceptions.EmailAlreadyExistsException;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private PatientRepository patientRepository;
    
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterUserReqDto doctorDto;
    private RegisterUserReqDto patientDto;
    private User user;
    private Role doctorRole;
    private Role patientRole;

    @BeforeEach
    void setUp() {

        // Doctor user data
        doctorDto = new RegisterUserReqDto(
            "DoctorUser", 
            "doctor@example.com", 
            "password123", 
            ProfileType.DOCTOR, 
            null, 
            null, 
            "Cardiology", 
            "1234567890");
        
        // Patient user data
        patientDto = new RegisterUserReqDto(
            "PatientUser", 
            "patient@example.com", 
            "password123", 
            ProfileType.PATIENT, 
            LocalDate.of(2000, 1, 1), 
            "No significant medical history", 
            null, 
            null);

        user = new User("testUser", "test@example.com", "testPassword");
        Set<Role> roles = new HashSet<>();
        doctorRole = new Role();
        doctorRole.setName(RoleType.DOCTOR);
        patientRole = new Role();
        patientRole.setName(RoleType.PATIENT);
        roles.add(doctorRole);
        user.setRoles(roles);
    }


    @Test
    @DisplayName("Test case 1: New user registration successfully when everything is OK")
    void testRegisterUser_AsDoctor_Sucess() {
        when(userRepository.existsByEmail(doctorDto.email())).thenReturn(false);
        when(passwordEncoder.encode(doctorDto.password())).thenReturn("testPassword");
        when(roleRepository.findByName(RoleType.DOCTOR)).thenReturn(Optional.of(doctorRole));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(doctorRepository.save(any(DoctorProfile.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserDetailResDto result = userService.registerUser(doctorDto);

        assertNotNull(result);
        assertEquals(doctorDto.username(), result.username());
        assertEquals(doctorDto.email(), result.emailString());
        assertEquals(ProfileType.DOCTOR, result.profile().profileType());
        assertEquals(doctorDto.specialization(), result.profile().specialization());
        verify(userRepository, times(1)).save(any(User.class));
        verify(doctorRepository, times(1)).save(any(DoctorProfile.class));
    } 

    @Test
    @DisplayName("Test case 2: Should register a new patient successfully")
    void testRegisterUser_AsPatient_Sucess() {
        when(userRepository.existsByEmail(patientDto.email())).thenReturn(false);
        when(passwordEncoder.encode(patientDto.password())).thenReturn("testPassword");
        when(roleRepository.findByName(RoleType.PATIENT)).thenReturn(Optional.of(patientRole));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(patientRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        UserDetailResDto result = userService.registerUser(patientDto);

        assertNotNull(result);
        assertEquals(patientDto.username(), result.username());
        assertEquals(patientDto.email(), result.emailString());
        assertEquals(ProfileType.PATIENT, result.profile().profileType());
        assertEquals(patientDto.medicalHistory(), result.profile().medicalHistory());
        verify(userRepository, times(1)).save(any(User.class));
        verify(patientRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Test case 3: Should throw EmailAlreadyExistsException when email is already in use")
    void testRegisterUser_EmailAlreadyExists() {
        when(userRepository.existsByEmail(doctorDto.email())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.registerUser(doctorDto);
        });

        verify(userRepository, never()).save(any(User.class));
    }


    @Test
    @DisplayName("Test case 4: Should throw RuntimeException when role type is invalid")
    void testRegisterUser_RoleNotFound() {
        when(userRepository.existsByEmail(doctorDto.email())).thenReturn(false);
        when(passwordEncoder.encode(doctorDto.password())).thenReturn("testPassword");
        when(roleRepository.findByName(RoleType.DOCTOR)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            userService.registerUser(doctorDto);
        });

        verify(userRepository, never()).save(any(User.class));
    }

}