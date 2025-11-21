package com.university.management.system.services.users;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.users.EmployeeRequestDto;
import com.university.management.system.dtos.users.EmployeeUpdateDto;
import org.springframework.http.ResponseEntity;

public interface IEmployeeService {
    ResponseEntity<ApiResponse> getAllEmployees(Integer page, Integer size);

    ResponseEntity<ApiResponse> getEmployeeById(String id);

    ResponseEntity<ApiResponse> createEmployee(EmployeeRequestDto employeeRequestDto);

    ResponseEntity<ApiResponse> updateEmployee(String id, EmployeeUpdateDto employeeUpdateDto);

    ResponseEntity<ApiResponse> deleteEmployee(String id);

    ResponseEntity<ApiResponse> getProfile();
}
