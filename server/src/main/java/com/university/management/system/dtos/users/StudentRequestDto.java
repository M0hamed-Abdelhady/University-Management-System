package com.university.management.system.dtos.users;

import com.university.management.system.models.users.StudentStatus;
import jakarta.validation.constraints.*;
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
public class StudentRequestDto {
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
