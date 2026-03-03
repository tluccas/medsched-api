package com.alvesdev.medsched_api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alvesdev.medsched_api.domain.services.DoctorService;
import com.alvesdev.medsched_api.dto.request.profiles.UpdateDoctorRequest;
import com.alvesdev.medsched_api.dto.response.profile.DoctorDetailResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
@RequestMapping("/users/doctors")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @Operation(summary = "Get all doctors", description = "Returns a list of all doctors in the system.")
    @GetMapping("")
    public ResponseEntity<List<DoctorDetailResponse>> getAllDoctors(
        @PageableDefault(size = 15, sort = "username") Pageable pageable
    ) {
        List<DoctorDetailResponse> doctors = doctorService.getAllDoctors(pageable);
        return ResponseEntity.ok(doctors);
    }

    @Operation(summary = "Get doctor by ID", description = "Returns the details of a doctor by their unique identifier.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Doctor found and returned successfully"),
        @ApiResponse(responseCode = "404", description = "Doctor not found with the provided ID")
    })
    @GetMapping("/{uuid}")
    public ResponseEntity<DoctorDetailResponse> getDoctorById(@PathVariable UUID uuid) {
        DoctorDetailResponse doctor = doctorService.getDoctorById(uuid);
        return ResponseEntity.ok(doctor);
    }

    @Operation(summary = "Update doctor profile", description = "Updates the profile information of a doctor.")
    @ApiResponses({
        @ApiResponse(responseCode = "200",
        description = "Doctor profile updated successfully"),
        @ApiResponse(responseCode = "404",
        description = "Doctor user not found"),
        @ApiResponse(responseCode = "400",
        description = "Invalid input data")
    })
    @PutMapping("/{uuid}")
    public ResponseEntity<DoctorDetailResponse> updateDoctor(
        @PathVariable UUID uuid,
        @RequestBody UpdateDoctorRequest updateDoctorRequest
    ) {
        DoctorDetailResponse updatedDoctor = doctorService.update(uuid, updateDoctorRequest);
        return ResponseEntity.ok(updatedDoctor);
    }
    
}
