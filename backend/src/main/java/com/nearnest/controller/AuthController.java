package com.nearnest.controller;

import com.nearnest.dto.LoginRequest;
import com.nearnest.dto.LoginResponse;
import com.nearnest.dto.RegisterRequest;
import com.nearnest.service.AuthService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
            @RequestBody RegisterRequest request) {

        return ResponseEntity.ok(
                authService.register(request)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest request) {

        LoginResponse response = authService.login(request);

        if (response == null) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid email or password");
        }

        return ResponseEntity.ok(response);
    }
}