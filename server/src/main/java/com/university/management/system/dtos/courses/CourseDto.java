package com.university.management.system.dtos.courses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private String id;
    private String courseCode;
    private String title;
    private String description;
    private Integer credits;
}
