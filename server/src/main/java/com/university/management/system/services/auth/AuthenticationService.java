package com.university.management.system.services.auth;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.auth.AuthResponse;
import com.university.management.system.dtos.auth.LoginRequest;
import com.university.management.system.dtos.auth.RegisterRequest;
import com.university.management.system.models.users.Person;
import com.university.management.system.models.users.PersonRole;
import com.university.management.system.models.users.Role;
import com.university.management.system.models.users.Student;
import com.university.management.system.models.users.StudentStatus;
import com.university.management.system.repositories.users.PersonRepository;
import com.university.management.system.repositories.users.PersonRoleRepository;
import com.university.management.system.repositories.users.StudentRepository;
import com.university.management.system.utils.AuthUtils;
import com.university.management.system.utils.ResponseEntityBuilder;
import com.university.management.system.utils.mappers.users.PersonResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements IAuthenticationService {
    private final PersonRepository personRepository;
    private final PersonResponseMapper personResponseMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthUtils authUtils;
    private final JwtService jwtService;
    private final StudentRepository studentRepository;
    private final PersonRoleRepository personRoleRepository;

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> register(RegisterRequest registerRequest, Boolean isAdmin) {
        if (personRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already exists: " + registerRequest.getEmail());
        }

        Person person = Person.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();

        Person savedPerson = personRepository.save(person);

        PersonRole studentRole = PersonRole.builder()
                .person(savedPerson)
                .role(Role.STUDENT)
                .build();

        personRoleRepository.save(studentRole);
        savedPerson.setPersonRoles(Collections.singleton(studentRole));

        Student student = Student.builder()
                .person(savedPerson)
                .status(StudentStatus.ACTIVE)
                .studentNumber(UUID.randomUUID().toString())
                .build();

        studentRepository.save(student);

        String token = jwtService.generateToken(savedPerson);
        AuthResponse authResponse = personResponseMapper.toAuthResponse(savedPerson, token);

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.CREATED)
                .withData("User", authResponse)
                .withMessage("User registered successfully!")
                .build();
    }

    @Override
    public ResponseEntity<ApiResponse> login(LoginRequest loginRequest) {
        Person person = personRepository.findByEmail(loginRequest.getEmail()).orElse(null);
        if (person == null || !passwordEncoder.matches(loginRequest.getPassword(), person.getPassword())) {
            return ResponseEntityBuilder.create()
                    .withStatus(HttpStatus.BAD_REQUEST)
                    .withMessage("Invalid email or password!")
                    .build();
        }

        String token = jwtService.generateToken(person);
        AuthResponse authResponse = personResponseMapper.toAuthResponse(person, token);

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withData("User", authResponse)
                .withMessage("User logged in successfully!")
                .build();
    }

    @Override
    public ResponseEntity<ApiResponse> refreshToken(String token) {
        return null;
    }

    @Override
    public ResponseEntity<ApiResponse> me() {
        return null;
    }

}