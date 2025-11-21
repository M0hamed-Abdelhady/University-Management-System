package com.university.management.system.services.auth;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.auth.AuthResponse;
import com.university.management.system.dtos.auth.LoginRequest;
import com.university.management.system.dtos.auth.RegisterRequest;
import com.university.management.system.models.users.Person;
import com.university.management.system.models.users.PersonRole;
import com.university.management.system.models.users.Student;
import com.university.management.system.repositories.users.PersonRepository;
import com.university.management.system.repositories.users.PersonRoleRepository;
import com.university.management.system.repositories.users.StudentRepository;
import com.university.management.system.utils.AuthUtils;
import com.university.management.system.utils.mappers.users.PersonResponseMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonResponseMapper personResponseMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthUtils authUtils;

    @Mock
    private JwtService jwtService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private PersonRoleRepository personRoleRepository;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    public void whenRegister_thenUserRegistered() {
        // given
        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .password("password")
                .firstName("Test")
                .lastName("User")
                .build();
        Person person = Person.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .build();
        AuthResponse authResponse = AuthResponse.builder()
                .email("test@example.com")
                .token("jwt-token")
                .build();

        when(personRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(personRepository.save(any(Person.class))).thenReturn(person);
        when(personRoleRepository.save(any(PersonRole.class))).thenReturn(new PersonRole());
        when(studentRepository.save(any(Student.class))).thenReturn(new Student());
        when(jwtService.generateToken(any(Person.class))).thenReturn("jwt-token");
        when(personResponseMapper.toAuthResponse(any(Person.class), anyString())).thenReturn(authResponse);

        // when
        ResponseEntity<ApiResponse> response = authenticationService.register(request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getData()).containsEntry("User", authResponse);
        verify(personRepository).save(any(Person.class));
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    public void whenLogin_thenUserLoggedIn() {
        // given
        LoginRequest request = LoginRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();
        Person person = Person.builder()
                .email("test@example.com")
                .password("encodedPassword")
                .build();
        AuthResponse authResponse = AuthResponse.builder()
                .email("test@example.com")
                .token("jwt-token")
                .build();

        when(personRepository.findByEmail(anyString())).thenReturn(Optional.of(person));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(jwtService.generateToken(any(Person.class))).thenReturn("jwt-token");
        when(personResponseMapper.toAuthResponse(any(Person.class), anyString())).thenReturn(authResponse);

        // when
        ResponseEntity<ApiResponse> response = authenticationService.login(request);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getData()).containsEntry("User", authResponse);
    }
}
