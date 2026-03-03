package com.alvesdev.medsched_api.dto.request.profiles;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;

public record UpdatePatientRequest(
    @NotBlank(message = "Birth date is required")
    LocalDate birthDate,

    @NotBlank(message = "License number is required")
    String medicalHistory
) {
    
}