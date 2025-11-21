package com.university.management.system.controllers.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.auth.AuthResponse;
import com.university.management.system.dtos.auth.LoginRequest;
import com.university.management.system.dtos.auth.RegisterRequest;
import com.university.management.system.services.auth.AuthenticationService;
import com.university.management.system.utils.ResponseEntityBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private com.university.management.system.services.auth.JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenRegister_thenReturnCreated() throws Exception {
        // given
        RegisterRequest request = RegisterRequest.builder()
                .email("test@example.com")
                .password("password")
                .firstName("Test")
                .lastName("User")
                .build();
        AuthResponse authResponse = AuthResponse.builder()
                .email("test@example.com")
                .token("jwt-token")
                .build();
        ResponseEntity<ApiResponse> responseEntity = ResponseEntityBuilder.create()
                .withStatus(HttpStatus.CREATED)
                .withData("User", authResponse)
                .build();

        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(responseEntity);

        // when & then
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.User.email").value("test@example.com"));
    }

    @Test
    public void whenLogin_thenReturnOk() throws Exception {
        // given
        LoginRequest request = LoginRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();
        AuthResponse authResponse = AuthResponse.builder()
                .email("test@example.com")
                .token("jwt-token")
                .build();
        ResponseEntity<ApiResponse> responseEntity = ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withData("User", authResponse)
                .build();

        when(authenticationService.login(any(LoginRequest.class))).thenReturn(responseEntity);

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.User.token").value("jwt-token"));
    }
}
