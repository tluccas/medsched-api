package com.alvesdev.medsched_api.domain.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.time.DayOfWeek;
import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alvesdev.medsched_api.dto.response.AppointmentData;
import com.alvesdev.medsched_api.dto.response.ScheduleResponse;

@Service
public class AvailabilityService {
    
    @Autowired
    AppointmentService appointmentService;

    @Autowired
    ScheduleService scheduleService;

    public Map<LocalDate, Integer> getDoctorAvailabilityInMonth(UUID doctorId, YearMonth month) {

        LocalDate start = month.atDay(1);
        LocalDate end = month.atEndOfMonth();

        Map<LocalDate, Integer> availability = new HashMap<>();


        // Get the doctor's schedules
        List<ScheduleResponse> schedules = scheduleService.getDoctorSchedule(doctorId);

        // Get the doctor's appointments in the month
        List<AppointmentData> appointments = appointmentService.getDoctorAppointmentsInMonth(doctorId, month.getMonthValue());

        LocalDate current = start;
        while (!current.isAfter(end)) {

            int slots = calculateAvailableSlotsForDay(current, schedules, appointments);
            availability.put(current, slots);
            current = current.plusDays(1);
        }
        return availability;
    }

    private int calculateAvailableSlotsForDay(LocalDate currentDate, List<ScheduleResponse> schedules, List<AppointmentData> appointments) {

        DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();

        int slots = 0;

        for (ScheduleResponse schedule : schedules) {
            if (DayOfWeek.of(schedule.dayOfWeek()).equals(currentDayOfWeek)) {

                // Calculate the total available slots for this schedule
                Duration duration = Duration.between(LocalTime.parse(schedule.startTime()), LocalTime.parse(schedule.endTime()));
                slots += duration.toMinutes() / 30; // Assuming 30-minute slots
            }
        }

        for (AppointmentData appointment : appointments) {
            if(appointment.appointmentDateTime().toLocalDate().equals(currentDate)) {
                slots--;
            }
        }

        return slots;
    }
}