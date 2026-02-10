package com.alvesdev.medsched_api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alvesdev.medsched_api.domain.services.PatientService;
import com.alvesdev.medsched_api.dto.response.user.PatientDetailResDto;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/patients")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @GetMapping("")
    public ResponseEntity<List<PatientDetailResDto>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }
    
}
