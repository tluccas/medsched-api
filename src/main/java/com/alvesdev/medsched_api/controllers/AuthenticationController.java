package com.alvesdev.medsched_api.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alvesdev.medsched_api.domain.model.User;
import com.alvesdev.medsched_api.dto.request.AuthenticationDTO;
import com.alvesdev.medsched_api.dto.response.AuthenticationResponseDTO;
import com.alvesdev.medsched_api.infrastructure.security.TokenService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("auth")
public class AuthenticationController {
    @Autowired
    private TokenService tokenService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> postMethodName(@RequestBody @Valid AuthenticationDTO dto) {
        var emailPassword = new UsernamePasswordAuthenticationToken(dto.email(), dto.password());

        var auth = this.authenticationManager.authenticate(emailPassword);

        var token = tokenService.generateToken((User) auth.getPrincipal());

        return ResponseEntity.ok(new AuthenticationResponseDTO(token));
    }
    
}
