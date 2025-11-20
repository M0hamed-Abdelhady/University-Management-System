package com.university.management.system.dtos.courses;

import com.university.management.system.dtos.users.StudentDto;
import com.university.management.system.models.courses.EnrollmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDto {
    private String id;
    private StudentDto student;
    private CourseClassDto courseClass;
    private String grade;
    private EnrollmentStatus status;
}
