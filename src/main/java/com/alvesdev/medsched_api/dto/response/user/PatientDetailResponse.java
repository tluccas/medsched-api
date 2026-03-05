package com.alvesdev.medsched_api.dto.response.user;

import java.util.UUID;

public record PatientDetailResponse(
    UUID userId,
    UUID patientId,
    String username,

    String birthDate,
    String medicalHistory
) {
    
}
