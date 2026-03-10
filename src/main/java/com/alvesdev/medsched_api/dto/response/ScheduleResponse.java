package com.alvesdev.medsched_api.dto.response;

import java.util.UUID;

public record ScheduleResponse(
    UUID doctorId,

    int dayOfWeek,
    String startTime,
    String endTime
) {}
