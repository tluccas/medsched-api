package com.alvesdev.medsched_api.dto.response.profile;

import java.util.UUID;

public record DoctorDetailResponse(
    UUID doctorId,
    String name,
    String specialization
) {
    
}
