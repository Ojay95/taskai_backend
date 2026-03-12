package com.taskai_backend.identity.controller;


import com.taskai_backend.identity.dto.request.SignupRequest;
import com.taskai_backend.identity.dto.response.AuthResponse;
import com.taskai_backend.identity.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequest request) {
        authService.register(request.getEmail(), request.getPassword(), request.getFullName());
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody SignupRequest request) {
        String token = authService.login(request.getEmail(), request.getPassword());
        return ResponseEntity.ok(AuthResponse.builder()
                .token(token)
                .email(request.getEmail())
                .build());
    }
}