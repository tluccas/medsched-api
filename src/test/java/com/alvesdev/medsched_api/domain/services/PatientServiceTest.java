package com.alvesdev.medsched_api.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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

import com.alvesdev.medsched_api.domain.model.PatientProfile;
import com.alvesdev.medsched_api.domain.model.Role;
import com.alvesdev.medsched_api.domain.model.User;
import com.alvesdev.medsched_api.domain.model.enums.RoleType;
import com.alvesdev.medsched_api.domain.repositories.PatientRepository;
import com.alvesdev.medsched_api.dto.request.profiles.UpdatePatientRequest;
import com.alvesdev.medsched_api.dto.response.user.PatientDetailResponse;
import com.alvesdev.medsched_api.exceptions.UserNotFoundException;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private PatientService patientService;

    private User user;
    private PatientProfile patientProfile;

    @BeforeEach
    void setUp() {
        user = new User(
            "Patient Test",
            "patient@example.com",
            "passTest"
        );
        Set<Role> roles = new HashSet<>();
        Role patientRole = new Role();
        patientRole.setName(RoleType.PATIENT);
        roles.add(patientRole);
        user.setRoles(roles);

        patientProfile = new PatientProfile();
        patientProfile.setId(UUID.randomUUID());
        patientProfile.setUser(user);
        patientProfile.setBirthDate(LocalDate.of(1990, 1, 1));
        patientProfile.setMedicalHistory("None");
    }

    @Test
    @DisplayName("Should return all patients")
    void shouldReturnAllPatients() {
        when(patientRepository.findAll()).thenReturn(List.of(patientProfile));

        List<PatientDetailResponse> response = patientService.getAllPatients();

        assertEquals(1, response.size());
        assertEquals("Patient Test", response.get(0).username());
        verify(patientRepository).findAll();
    }

    @Test
    @DisplayName("Should return patient details when patient profile exists for the user")
    void shouldReturnPatientDetailsWhenPatientProfileExists() {
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        user.setPatientProfile(patientProfile);

        when(userService.findById(userId)).thenReturn(user);

        PatientDetailResponse response = patientService.getByProfileId(userId);

        assertEquals("Patient Test", response.username());
        assertEquals("1990-01-01", response.birthDate());
        verify(userService).findById(userId);
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user does not have a patient profile for getByProfileId")
    void shouldThrowUserNotFoundExceptionWhenNoPatientProfileForGet() {
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        user.setPatientProfile(null);

        when(userService.findById(userId)).thenReturn(user);

        assertThrows(UserNotFoundException.class, () -> patientService.getByProfileId(userId));
        verify(userService).findById(userId);
    }

    @Test
    @DisplayName("Should update patient profile")
    void shouldUpdatePatientProfile() {
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        user.setPatientProfile(patientProfile);
        UpdatePatientRequest request = new UpdatePatientRequest(LocalDate.of(1991, 2, 2), "Updated History");

        when(userService.findById(userId)).thenReturn(user);
        when(patientRepository.save(any(PatientProfile.class))).thenReturn(patientProfile);

        PatientDetailResponse response = patientService.update(userId, request);

        assertNotNull(response);
        assertEquals("1991-02-02", response.birthDate());
        assertEquals("Updated History", response.medicalHistory());
        verify(userService).findById(userId);
        verify(patientRepository).save(any(PatientProfile.class));
    }

    @Test
    @DisplayName("Should throw UserNotFoundException when user does not have a patient profile for update")
    void shouldThrowUserNotFoundExceptionWhenNoPatientProfileForUpdate() {
        UUID userId = UUID.randomUUID();
        user.setId(userId);
        user.setPatientProfile(null);
        UpdatePatientRequest request = new UpdatePatientRequest(LocalDate.of(1991, 2, 2), "Updated History");

        when(userService.findById(userId)).thenReturn(user);

        assertThrows(UserNotFoundException.class, () -> patientService.update(userId, request));
        verify(userService).findById(userId);
    }
}
