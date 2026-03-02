package com.alvesdev.medsched_api.controllers;

import java.util.UUID;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alvesdev.medsched_api.domain.services.UserService;
import com.alvesdev.medsched_api.dto.request.register.RegisterUserReqDto;
import com.alvesdev.medsched_api.dto.response.profile.ProfileDetailResponse;
import com.alvesdev.medsched_api.dto.response.user.UserDetailResDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Operation(summary = "Register a new user", description = "Registers a new user in the system")
    @ApiResponses(
        {
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "409", description = "Email already exists")
        }
    )
    @PostMapping("/register")
    public ResponseEntity<UserDetailResDto> postMethodName(@RequestBody @Valid RegisterUserReqDto dto) {
        UserDetailResDto registeredUser = userService.registerUser(dto);
        
        return ResponseEntity.status(Response.SC_CREATED).body(registeredUser);
    
    }

    @Operation(summary = "Get user profile", description = "Retrieves the profile details of a user by their ID")
    @ApiResponses(
        {
        @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "User not found")}
    )
    @GetMapping("/{uuid}")
    public ResponseEntity<ProfileDetailResponse> getMethodName(@PathVariable UUID uuid) {
        ProfileDetailResponse profile = userService.getUserProfile(uuid);
        return ResponseEntity.ok(profile);
    }
    
}
