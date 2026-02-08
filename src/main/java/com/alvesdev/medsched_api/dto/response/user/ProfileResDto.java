package com.alvesdev.medsched_api.dto.response.user;

import java.time.LocalDate;

import com.alvesdev.medsched_api.dto.request.register.ProfileType;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProfileResDto(
    ProfileType profileType,

    LocalDate birthDate,
    String medicalHistory,

    String specialization,
    String licenseNumber
) {
    
}
