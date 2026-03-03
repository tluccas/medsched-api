package com.alvesdev.medsched_api.dto.request.profiles;

import jakarta.validation.constraints.NotBlank;

public record UpdateDoctorRequest(
    @NotBlank(message = "Specialization is required")
    String specialization,

    @NotBlank(message = "License number is required")
    String licenseNumber
) {
    
}
