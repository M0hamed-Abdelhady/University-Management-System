package com.university.management.system.dtos.users;

import com.university.management.system.models.users.StudentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {
    private String id;
    private PersonDto person;
    private String studentNumber;
    private String major;
    private Integer academicYear;
    private BigDecimal gpa;
    private StudentStatus status;
}
