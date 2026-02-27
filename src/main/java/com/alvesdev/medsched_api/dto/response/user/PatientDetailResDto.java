package com.alvesdev.medsched_api.dto.response.user;

import java.time.LocalDate;
import java.util.UUID;

public record PatientDetailResDto(
    UUID userId,
    UUID patientId,
    String username,
    LocalDate birthDate,
    String medicalHistory
) {
    
}
