package com.university.management.system.dtos.auth;

import com.university.management.system.models.users.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private List<Role> roles;
    private String token;
}
