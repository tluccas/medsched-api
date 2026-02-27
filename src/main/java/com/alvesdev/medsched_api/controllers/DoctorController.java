package com.alvesdev.medsched_api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alvesdev.medsched_api.domain.services.DoctorService;
import com.alvesdev.medsched_api.dto.response.user.DoctorDetailResDto;

import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @GetMapping("")
    public ResponseEntity<List<DoctorDetailResDto>> getAllDoctors() {
        List<DoctorDetailResDto> doctors = doctorService.getAllDoctors();
        return ResponseEntity.ok(doctors);
    }
    
}
