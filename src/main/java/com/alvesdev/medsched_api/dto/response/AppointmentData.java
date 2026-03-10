package com.alvesdev.medsched_api.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.alvesdev.medsched_api.domain.model.enums.AppointmentStatus;

public record AppointmentData(
    UUID scheduleId,
    LocalDateTime appointmentDateTime,
    AppointmentStatus status

) {
    
}
