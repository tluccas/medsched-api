package com.alvesdev.medsched_api.dto.response.user;

import java.util.UUID;

public record DoctorDetailResDto(
    UUID userId,
    UUID doctorId,
    String name,
    String specialization
) {
    
}
