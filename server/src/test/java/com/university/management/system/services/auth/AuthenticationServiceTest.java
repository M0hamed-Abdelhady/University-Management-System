package com.university.management.system.services.auth;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.auth.AuthResponse;
import com.university.management.system.dtos.auth.LoginRequest;
import com.university.management.system.dtos.users.ProfileResponse;
import com.university.management.system.models.users.Person;
import com.university.management.system.repositories.users.PersonRepository;
import com.university.management.system.security.JwtTokenProvider;
import com.university.management.system.utils.mappers.users.PersonResponseMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonResponseMapper personResponseMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void login_ShouldReturnOk_WhenCredentialsAreValid() {
        LoginRequest request = new LoginRequest("john@example.com", "password");
        Person person = Person.builder()
                .email("john@example.com")
                .password("encodedPassword")
                .build();

        ProfileResponse profileResponse = ProfileResponse.builder()
                .email("john@example.com")
                .build();

        AuthResponse authResponse = new AuthResponse("token", profileResponse);

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenProvider.generateToken(authentication)).thenReturn("token");
        when(personRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(person));
        when(personResponseMapper.toProfileResponse(person)).thenReturn(profileResponse);

        ResponseEntity<ApiResponse> response = authenticationService.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Login successful", response.getBody().getMessage());
    }
}
