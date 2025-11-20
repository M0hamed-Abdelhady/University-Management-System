package com.university.management.system.dtos.courses;

import com.university.management.system.models.courses.CourseClassStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseClassRequestDto {
    @NotBlank
    private String courseId;
    @NotBlank
    private String lecturerId;
    @NotBlank
    private String semester;
    @NotNull
    private Integer academicYear;
    @Positive
    private Integer maxCapacity;
    private CourseClassStatus status;
}
