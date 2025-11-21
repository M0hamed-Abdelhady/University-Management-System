package com.university.management.system.dtos.users;

import com.university.management.system.models.users.EmployeeStatus;
import com.university.management.system.models.users.Position;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateDto {
    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    @NotNull
    @Email
    private String email;

    private String phone;

    private String address;

    private LocalDate dateOfBirth;

    private LocalDate hireDate;

    @Positive
    private BigDecimal salary;

    @NotNull
    private Position position;

    @NotNull
    private EmployeeStatus status;
}
