package com.alvesdev.medsched_api.dto.request.register;

import java.time.LocalDate;

import com.alvesdev.medsched_api.infrastructure.validators.ValidRegister;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@ValidRegister
public record RegisterUserReqDto(

    @NotBlank(message = "Username is required")
    String username,

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters long")
    String password,

    @NotNull(message = "Profile type is required")
    ProfileType profileType,

    // Patient specific fields
    @JsonFormat(pattern = "yyyy-MM-dd")
    LocalDate birthDate,

    String medicalHistory,

    // Doctor specific fields
    String specialization,

    String licenseNumber
) {
    
}
