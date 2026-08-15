package com.example.usermanagement.controller.auth;

import com.example.usermanagement.dto.auth.*;
import com.example.usermanagement.service.AuthService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register (
            @Valid @RequestBody RegisterRequest request
    ){
        authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new RegisterResponse("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh (
            @Valid @RequestBody RefreshTokenRequest request
            ){
        return ResponseEntity.ok(
                authService.refreshToken(request)
        );
    }
}
