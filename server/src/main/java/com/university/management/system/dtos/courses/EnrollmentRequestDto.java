package com.university.management.system.dtos.courses;

import com.university.management.system.models.courses.EnrollmentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRequestDto {
    @NotBlank
    private String studentId;
    @NotBlank
    private String classId;
    private String grade;
    @NotNull
    private EnrollmentStatus status;
}
