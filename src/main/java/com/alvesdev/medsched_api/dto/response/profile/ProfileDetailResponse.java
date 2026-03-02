package com.alvesdev.medsched_api.dto.response.profile;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProfileDetailResponse(
    String username,
    String profileType,

    // Doctor-specific fields
    String specialization,
    String licenseNumber,

    // Patient-specific fields
    String medicalHistory,
    LocalDate birthDate,

    int totalAppointments
) {}
