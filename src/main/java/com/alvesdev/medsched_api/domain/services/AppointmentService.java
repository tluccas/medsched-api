package com.alvesdev.medsched_api.domain.services;

import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alvesdev.medsched_api.domain.repositories.AppointmentRepository;
import com.alvesdev.medsched_api.dto.response.AppointmentData;

@Service
public class AppointmentService {
    
    @Autowired
    private AppointmentRepository appointmentRepository;

    public List<AppointmentData> getDoctorAppointmentsInMonth(UUID doctorId, int month){
        LocalDateTime now = LocalDateTime.now();
        
        LocalDateTime startOfMonth = LocalDateTime.of(now.getYear(), month, 1, 0, 0);
        LocalDateTime endOfMonth = startOfMonth.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59);

        return appointmentRepository.findByMonthAppointmentsAndAppointmentsDoctorAppointments(doctorId, startOfMonth, endOfMonth)
            .stream()
            .map(appointment -> new AppointmentData(
                appointment.getSchedule().getId(),
                appointment.getAppointmentDateTime(),
                appointment.getStatus()
            ))
            .toList();
    }
}
