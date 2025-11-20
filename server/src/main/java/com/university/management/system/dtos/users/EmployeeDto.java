package com.university.management.system.dtos.users;

import com.university.management.system.models.users.EmployeeStatus;
import com.university.management.system.models.users.Position;
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
public class EmployeeDto {
    private String id;
    private PersonRequestDto person;
    private String employeeId;
    private LocalDate hireDate;
    private BigDecimal salary;
    private Position position;
    private EmployeeStatus status;
}
