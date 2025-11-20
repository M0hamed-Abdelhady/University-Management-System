package com.university.management.system.dtos.courses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequestDto {
    @NotBlank
    private String courseCode;
    @NotBlank
    private String title;
    private String description;
    @Positive
    private Integer credits;
}
