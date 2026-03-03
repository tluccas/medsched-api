package com.alvesdev.medsched_api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alvesdev.medsched_api.domain.services.PatientService;
import com.alvesdev.medsched_api.dto.request.profiles.UpdatePatientRequest;
import com.alvesdev.medsched_api.dto.response.user.PatientDetailResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;



@Controller
@RequestMapping("/users/patients")
public class PatientController {
    @Autowired
    private PatientService patientService;

    @Operation(summary = "Get all patients", description = "Returns a list of all patients in the system.")
    @GetMapping("")
    public ResponseEntity<List<PatientDetailResponse>> getAllPatients() {
        return ResponseEntity.ok(patientService.getAllPatients());
    }

    @Operation(summary = "Get patient by User ID", description = "Returns the details of a patient by their unique identifier.")
    @ApiResponses(
        {
            @ApiResponse(responseCode = "200", description = "Patient found and returned successfully."),
            @ApiResponse(responseCode = "404", description = "Patient not found with the provided ID."),
            @ApiResponse(responseCode = "400", description = "Invalid request parameters.")
        }
    )
    @GetMapping("/{uuid}")
    public ResponseEntity<PatientDetailResponse> getPatientById(@PathVariable UUID uuid) {
        return ResponseEntity.ok(patientService.getByProfileId(uuid));
    }

    @Operation(summary = "Update patient details", description = "Updates the details of a patient by their unique identifier.")
    @ApiResponses(
        {
            @ApiResponse(responseCode = "200", description = "Patient details updated successfully."),
            @ApiResponse(responseCode = "404", description = "Patient not found with the provided ID."),
            @ApiResponse(responseCode = "400", description = "Invalid request data.")
        }
    )
    @PutMapping("/{uuid}")
    public ResponseEntity<PatientDetailResponse> updatePatient(@PathVariable UUID uuid, @RequestBody @Valid UpdatePatientRequest dto) {
        PatientDetailResponse updatedPatient = patientService.update(uuid, dto);
        return ResponseEntity.ok(updatedPatient);
    }
    
}
