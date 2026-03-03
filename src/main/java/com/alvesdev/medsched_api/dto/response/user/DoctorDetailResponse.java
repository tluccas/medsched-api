package com.alvesdev.medsched_api.dto.response.user;

import java.util.UUID;

public record DoctorDetailResponse(
    UUID doctorId,
    String name,
    String specialization
) {
    
}
