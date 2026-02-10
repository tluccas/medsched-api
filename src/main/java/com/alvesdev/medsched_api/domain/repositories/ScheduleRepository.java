package com.alvesdev.medsched_api.domain.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alvesdev.medsched_api.domain.model.Schedule;

public interface ScheduleRepository extends JpaRepository<Schedule, UUID>{

    List<Schedule> findByDoctorId(UUID doctorId);

    List<Schedule> findByAvailable(Boolean available);

    List<Schedule> findByDayOfWeek(int dayOfWeek);

    List<Schedule> findByDoctorIdAndAvailable(UUID doctorId, Boolean available);
}
