package com.university.management.system.dtos.courses;

import com.university.management.system.dtos.users.EmployeeDto;
import com.university.management.system.models.courses.CourseClassStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseClassDto {
    private String id;
    private CourseDto course;
    private EmployeeDto lecturer;
    private String semester;
    private Integer academicYear;
    private Integer currentCapacity;
    private Integer maxCapacity;
    private CourseClassStatus status;
}
