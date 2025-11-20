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
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDto {
    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;


    @NotNull
    @Email
    private String email;

    @NotNull
    @NotBlank
    @Length(min = 8, max = 255)
    private String password;

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
