package com.university.management.system.services.auth;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.auth.LoginRequest;
import com.university.management.system.dtos.auth.RegisterRequest;
import org.springframework.http.ResponseEntity;

public interface IAuthenticationService {

    ResponseEntity<ApiResponse> register(RegisterRequest registerRequest, Boolean isAdmin);
    ResponseEntity<ApiResponse> login(LoginRequest loginRequest);

    ResponseEntity<ApiResponse> refreshToken(String token);

    ResponseEntity<ApiResponse> me();
}
