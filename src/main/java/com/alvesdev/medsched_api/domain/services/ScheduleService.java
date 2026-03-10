package com.alvesdev.medsched_api.domain.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alvesdev.medsched_api.domain.repositories.ScheduleRepository;
import com.alvesdev.medsched_api.dto.response.ScheduleResponse;

@Service
public class ScheduleService {
    
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ScheduleRepository scheduleRepository;

    /* Method to get a doctor's schedule. This will be used by doctors to manage their 
        available time slots */
    public List<ScheduleResponse> getDoctorSchedule(UUID doctorId) {
        doctorService.getDoctorByProfileId(doctorId); // Check if doctor exists.

        return scheduleRepository.findByDoctorId(doctorId).stream()
            .map(schedule -> new ScheduleResponse(
                schedule.getDoctor().getId(),
                schedule.getDayOfWeek(),
                schedule.getStartTime().toString(),
                schedule.getEndTime().toString()
            ))
            .toList();
    }
}
