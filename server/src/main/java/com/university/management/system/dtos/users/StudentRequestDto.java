package com.university.management.system.dtos.users;

import com.university.management.system.models.users.StudentStatus;
import jakarta.validation.constraints.*;
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
public class StudentRequestDto {
    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    @Email
    private String email;

    private String phone;

    @Past
    private LocalDate dateOfBirth;

    private String address;

    private String major;

    private Integer academicYear;

    @DecimalMin("0.0")
    @DecimalMax("4.0")
    private BigDecimal gpa;

    @NotNull
    private StudentStatus status;

}
