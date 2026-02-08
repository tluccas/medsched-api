package com.alvesdev.medsched_api.dto.response;

public record AuthenticationResponseDTO(
    String token,
    String tokenType,
    Long expiresIn
) {
    
    public AuthenticationResponseDTO(String token, Long expiresIn) {
        this(token, "Bearer", expiresIn);
    }
}
