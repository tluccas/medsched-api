package com.alvesdev.medsched_api.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.alvesdev.medsched_api.domain.model.DoctorProfile;
import com.alvesdev.medsched_api.domain.model.Role;
import com.alvesdev.medsched_api.domain.model.User;
import com.alvesdev.medsched_api.domain.model.enums.RoleType;
import com.alvesdev.medsched_api.domain.repositories.DoctorRepository;
import com.alvesdev.medsched_api.dto.response.PaginatedResponse;
import com.alvesdev.medsched_api.dto.response.profile.DoctorDetailResponse;
import com.alvesdev.medsched_api.exceptions.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {
    
    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private DoctorService doctorService;

    private User user;
    private Role doctorRole;
    private DoctorProfile doctorProfile;

    @BeforeEach
    void setUp() {
        user = new User(
            "Dr. Test",
            "test@example.com",
            "passTest"
        );
        Set<Role> roles = new HashSet<>();
        doctorRole = new Role();
        doctorRole.setName(RoleType.DOCTOR);
        roles.add(doctorRole);
        user.setRoles(roles);

        doctorProfile = new DoctorProfile();
        doctorProfile.setId(UUID.randomUUID());
        doctorProfile.setUser(user);
        doctorProfile.setSpecialization("Cardiology");
    }

    @Test
    @DisplayName("Should return doctors filtered by specialization when specialization is provided")
    void shouldReturnDoctorsFilteredBySpecialization() {

        Pageable pageable = PageRequest.of(0, 10);

        List<DoctorProfile> doctors = List.of(doctorProfile);

        Page<DoctorProfile> doctorPage = new PageImpl<>(doctors, pageable, doctors.size());

        when(doctorRepository.findBySpecialization("Cardiology", pageable))
            .thenReturn(doctorPage);

        PaginatedResponse<DoctorDetailResponse> response = 
            doctorService.getAllDoctors("Cardiology", pageable);

        assertEquals(1, response.content().size());
        assertEquals("Dr. Test", response.content().get(0).name());

        verify(doctorRepository)
            .findBySpecialization("Cardiology", pageable);
    }

    @Test
    @DisplayName("Should return all doctors when specialization is null")
    void shouldReturnAllDoctorsWhenSpecializationIsNull() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<DoctorProfile> doctorPage = new PageImpl<>(List.of(doctorProfile), pageable, 1);

        when(doctorRepository.findAll(pageable))
            .thenReturn(doctorPage);

        PaginatedResponse<DoctorDetailResponse> response = 
            doctorService.getAllDoctors(null, pageable);

        assertEquals(1, response.content().size());
        assertEquals("Dr. Test", response.content().get(0).name());

        verify(doctorRepository)
            .findAll(pageable);
    }

    @Test
    @DisplayName("Should return doctor details when doctor profile exists for the user")
    void shouldReturnDoctorDetailsWhenDoctorProfileExists() {
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        // Set the doctor profile for the user
        user.setDoctorProfile(doctorProfile);

        when(userService.findById(userId)).thenReturn(user);

        DoctorDetailResponse response = doctorService.getDoctorByUserId(userId);

        assertEquals("Dr. Test", response.name());
        assertEquals("Cardiology", response.specialization());

        verify(userService).findById(userId);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user does not have a doctor profile")
    void shouldThrowUserNotFoundExceptionWhenNoDoctorProfile() {
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        // Ensure the user does not have a doctor profile
        user.setDoctorProfile(null);

        when(userService.findById(userId)).thenReturn(user);

        assertThrows(UserNotFoundException.class, () -> doctorService.getDoctorByUserId(userId));

        verify(userService).findById(userId);
    }
}