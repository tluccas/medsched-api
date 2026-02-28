package com.alvesdev.medsched_api.controllers;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alvesdev.medsched_api.domain.services.UserService;
import com.alvesdev.medsched_api.dto.request.register.RegisterUserReqDto;
import com.alvesdev.medsched_api.dto.response.user.UserDetailResDto;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Operation(summary = "Register a new user", description = "Registers a new user in the system")
    @PostMapping("/register")
    public ResponseEntity<UserDetailResDto> postMethodName(@RequestBody @Valid RegisterUserReqDto dto) {
        UserDetailResDto registeredUser = userService.registerUser(dto);
        
        return ResponseEntity.status(Response.SC_CREATED).body(registeredUser);
    
    }
}
