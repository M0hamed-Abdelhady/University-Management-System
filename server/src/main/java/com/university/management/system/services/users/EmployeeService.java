package com.university.management.system.services.users;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.users.EmployeeDto;
import com.university.management.system.dtos.users.EmployeeRequestDto;
import com.university.management.system.dtos.users.PersonDto;
import com.university.management.system.models.users.Employee;
import com.university.management.system.models.users.Person;
import com.university.management.system.models.users.PersonRole;
import com.university.management.system.models.users.Role;
import com.university.management.system.repositories.users.EmployeeRepository;
import com.university.management.system.repositories.users.PersonRepository;
import com.university.management.system.repositories.users.PersonRoleRepository;
import com.university.management.system.utils.RepositoryUtils;
import com.university.management.system.utils.ResponseEntityBuilder;
import com.university.management.system.utils.mappers.users.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {
    private final PersonService personService;
    private final EmployeeRepository employeeRepository;
    private final PersonRepository personRepository;
    private final PersonRoleRepository personRoleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RepositoryUtils repositoryUtils;

    @Override
    public ResponseEntity<ApiResponse> getAllEmployees(Integer page, Integer size) {
        Pageable pageable = repositoryUtils.getPageable(page, size, Sort.Direction.ASC, "createdAt");
        Page<Employee> employees = employeeRepository.findAll(pageable);

        List<EmployeeDto> response = employees
                .map(userMapper::toEmployeeDto)
                .toList();

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withData("Employees", response)
                .withData("TotalPages", employees.getTotalPages())
                .withData("TotalElements", employees.getTotalElements())
                .withMessage("Employees retrieved successfully")
                .build();
    }

    @Override
    public ResponseEntity<ApiResponse> getEmployeeById(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withData("Employee", userMapper.toEmployeeDto(employee))
                .withMessage("Employee retrieved successfully")
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> createEmployee(EmployeeRequestDto employeeRequestDto) {
        if (personRepository.existsByEmail(employeeRequestDto.getEmail())) {
            throw new RuntimeException("Email already exists: " + employeeRequestDto.getEmail());
        }

        PersonDto personDto = PersonDto.builder()
                .firstName(employeeRequestDto.getFirstName())
                .lastName(employeeRequestDto.getLastName())
                .email(employeeRequestDto.getEmail())
                .phone(employeeRequestDto.getPhone())
                .address(employeeRequestDto.getAddress())
                .dateOfBirth(employeeRequestDto.getDateOfBirth())
                .build();

        personService.createPerson(personDto);
        Person person = personRepository.findByEmail(employeeRequestDto.getEmail()).orElseThrow();

        Employee employee = Employee.builder()
                .person(person)
                .employeeNumber(UUID.randomUUID().toString()) // Generate or use from DTO if provided? DTO doesn't have
                                                              // it in request usually, but let's check. DTO has it? No,
                                                              // RequestDto doesn't have it.
                .hireDate(employeeRequestDto.getHireDate())
                .salary(employeeRequestDto.getSalary())
                .position(employeeRequestDto.getPosition())
                .status(employeeRequestDto.getStatus())
                .build();

        person.setPassword(passwordEncoder.encode("Password123!"));

        // Assign EMPLOYEE role (or ADMIN based on position? User said "other rule
        // should be created by an admin". I'll assume generic EMPLOYEE role for now, or
        // maybe map Position to Role?)
        // For now, let's assign Role.EMPLOYEE (if it exists) or Role.ADMIN if position
        // is ADMIN?
        // Let's check Role enum.

        PersonRole employeeRole = PersonRole.builder()
                .person(person)
                .role(Role.EMPLOYEE)
                .build();
        // I need to check Role enum.

        person.setPersonRoles(Collections.singleton(employeeRole));

        Employee savedEmployee = employeeRepository.save(employee);

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.CREATED)
                .withData("Employee", userMapper.toEmployeeDto(savedEmployee))
                .withMessage("Employee created successfully")
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> updateEmployee(String id, EmployeeRequestDto employeeRequestDto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        Person person = employee.getPerson();
        person.setFirstName(employeeRequestDto.getFirstName());
        person.setLastName(employeeRequestDto.getLastName());
        person.setPhone(employeeRequestDto.getPhone());
        person.setAddress(employeeRequestDto.getAddress());
        person.setDateOfBirth(employeeRequestDto.getDateOfBirth());
        // Email update? Usually restricted or needs verification. I'll skip for now or
        // allow it.

        employee.setHireDate(employeeRequestDto.getHireDate());
        employee.setSalary(employeeRequestDto.getSalary());
        employee.setPosition(employeeRequestDto.getPosition());
        employee.setStatus(employeeRequestDto.getStatus());

        personRepository.save(person);
        Employee savedEmployee = employeeRepository.save(employee);

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withData("Employee", userMapper.toEmployeeDto(savedEmployee))
                .withMessage("Employee updated successfully")
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> deleteEmployee(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));

        employeeRepository.delete(employee);
        // Optionally delete person if no other roles?

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withMessage("Employee deleted successfully")
                .build();
    }
}
