package com.alvesdev.medsched_api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(
    
    @NotBlank(message = "Username is required")
    String username,

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email
) {
    
}
