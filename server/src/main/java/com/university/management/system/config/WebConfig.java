package com.university.management.system.config;

import com.university.management.system.repositories.users.PersonRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.time.Instant;

@Configuration
@RequiredArgsConstructor
public class WebConfig {
    private final PersonRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationEntryPoint accessDeniedHandler() {
        return (HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"timestamp\":\"" + Instant.now() +
                    "\",\"status\":401,\"error\":\"Unauthorized\",\"message\": \"Provide your credentials\",\"path\":\""
                    + request.getRequestURI() + "\"}");
        };
    }
}