package com.university.management.system.controllers.auth;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.auth.LoginRequest;
import com.university.management.system.dtos.auth.RegisterRequest;
import com.university.management.system.services.auth.IAuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final IAuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return authenticationService.register(registerRequest, false);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refreshToken(@RequestHeader("Authorization") String token) {
        return authenticationService.refreshToken(token);
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse> me() {
        return authenticationService.me();
    }
}
