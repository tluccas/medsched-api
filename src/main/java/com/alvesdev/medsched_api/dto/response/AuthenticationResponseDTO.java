package com.alvesdev.medsched_api.dto.response;

public record AuthenticationResponseDTO(
    String token,
    String tokenType
) {
    
    public AuthenticationResponseDTO(String token) {
        this(token, "Bearer");
    }
}
