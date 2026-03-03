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
import com.alvesdev.medsched_api.dto.request.UpdateUserRequest;
import com.alvesdev.medsched_api.dto.request.register.RegisterUserReqDto;
import com.alvesdev.medsched_api.dto.response.ErrorResponse;
import com.alvesdev.medsched_api.dto.response.profile.ProfileDetailResponse;
import com.alvesdev.medsched_api.dto.response.user.UserDetailResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Operation(summary = "Register a new user", description = "Registers a new user in the system")
    @ApiResponses(
        {
        @ApiResponse(responseCode = "201", description = "User registered successfully"),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Email already exists",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
        }
    )
    @PostMapping("/register")
    public ResponseEntity<UserDetailResponse> registerUser(@RequestBody @Valid RegisterUserReqDto dto) {
        UserDetailResponse registeredUser = userService.registerUser(dto);
        
        return ResponseEntity.status(Response.SC_CREATED).body(registeredUser);
    
    }

    @Operation(summary = "Get user profile", description = "Retrieves the profile details of a user by their ID")
    @ApiResponses(
        {
        @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
        })
    @GetMapping("/{uuid}")
    public ResponseEntity<ProfileDetailResponse> getUserProfile(@PathVariable UUID uuid) {
        ProfileDetailResponse profile = userService.getUserProfile(uuid);
        return ResponseEntity.ok(profile);
    }

    @Operation(summary = "Update user information", description = "Updates the username and email of an existing user")
    @ApiResponses(
        {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
        }
    )
    @PutMapping("/{uuid}")
    public ResponseEntity<UserDetailResponse> updateUser(@PathVariable UUID uuid, @RequestBody @Valid UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(uuid, request));
    }


    @Operation(summary = "Delete a user", description = "Deletes a user and all associated data from the system")
    @ApiResponses(
        {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(
            responseCode = "404",
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class)
            )
        )
        }
    )
    @DeleteMapping("/{uuid}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID uuid) {
        userService.deleteUser(uuid);
        return ResponseEntity.noContent().build();
    }
    
}
