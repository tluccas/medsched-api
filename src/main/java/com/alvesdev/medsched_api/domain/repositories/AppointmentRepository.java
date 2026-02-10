package com.alvesdev.medsched_api.domain.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.alvesdev.medsched_api.domain.model.Appointment;
import com.alvesdev.medsched_api.domain.model.enums.AppointmentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
    
    Optional<Appointment> findByScheduleId(UUID scheduleId);

    List<Appointment> findByDoctorId(UUID doctorId);

    List<Appointment> findByPatientId(UUID patientId);

    List<Appointment> findByStatus(AppointmentStatus status);

    List<Appointment> findByPatientIdAndAppointmentDateTimeAfterOrderByAppointmentDateTimeAsc(UUID patientId, LocalDateTime now);

        List<Appointment> findByDoctorIdAndAppointmentDateTimeAfterOrderByAppointmentDateTimeAsc(UUID doctorId, LocalDateTime now);

}
