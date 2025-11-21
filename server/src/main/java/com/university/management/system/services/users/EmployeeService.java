package com.university.management.system.services.users;

import com.university.management.system.exceptions.ResourceNotFoundException;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.users.EmployeeDto;
import com.university.management.system.dtos.users.EmployeeRequestDto;
import com.university.management.system.dtos.users.EmployeeUpdateDto;
import com.university.management.system.dtos.users.PersonRequestDto;
import com.university.management.system.models.users.Employee;
import com.university.management.system.models.users.Person;
import com.university.management.system.models.users.PersonRole;
import com.university.management.system.models.users.Role;
import com.university.management.system.repositories.users.EmployeeRepository;
import com.university.management.system.repositories.users.PersonRepository;
import com.university.management.system.repositories.users.PersonRoleRepository;
import com.university.management.system.utils.AuthUtils;
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
import java.util.HashSet;
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
        private final AuthUtils authUtils;

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
                                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

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

                PersonRequestDto personRequestDto = PersonRequestDto.builder()
                                .firstName(employeeRequestDto.getFirstName())
                                .lastName(employeeRequestDto.getLastName())
                                .email(employeeRequestDto.getEmail())
                                .password(employeeRequestDto.getPassword())
                                .phone(employeeRequestDto.getPhone())
                                .address(employeeRequestDto.getAddress())
                                .dateOfBirth(employeeRequestDto.getDateOfBirth())
                                .build();

                personService.createPerson(personRequestDto);
                Person person = personRepository.findByEmail(employeeRequestDto.getEmail())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Person not found with email: " + employeeRequestDto.getEmail()));

                PersonRole employeeRole = PersonRole.builder()
                                .person(person)
                                .role(Role.EMPLOYEE)
                                .build();

                personRoleRepository.save(employeeRole);
                person.setPersonRoles(new HashSet<>(Collections.singleton(employeeRole)));
                person = personRepository.save(person);

                Employee employee = Employee.builder()
                                .person(person)
                                .employeeNumber(UUID.randomUUID().toString())
                                .hireDate(employeeRequestDto.getHireDate())
                                .salary(employeeRequestDto.getSalary())
                                .position(employeeRequestDto.getPosition())
                                .status(employeeRequestDto.getStatus())
                                .build();

                Employee savedEmployee = employeeRepository.save(employee);

                return ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.CREATED)
                                .withData("Employee", userMapper.toEmployeeDto(savedEmployee))
                                .withMessage("Employee created successfully")
                                .build();
        }

        @Override
        @Transactional
        public ResponseEntity<ApiResponse> updateEmployee(String id, EmployeeUpdateDto employeeUpdateDto) {
                Employee employee = employeeRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

                Person person = employee.getPerson();
                person.setFirstName(employeeUpdateDto.getFirstName());
                person.setLastName(employeeUpdateDto.getLastName());
                person.setPhone(employeeUpdateDto.getPhone());
                person.setAddress(employeeUpdateDto.getAddress());
                person.setDateOfBirth(employeeUpdateDto.getDateOfBirth());
                personRepository.save(person);

                employee.setHireDate(employeeUpdateDto.getHireDate());
                employee.setSalary(employeeUpdateDto.getSalary());
                employee.setPosition(employeeUpdateDto.getPosition());
                employee.setStatus(employeeUpdateDto.getStatus());

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
                                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with id: " + id));

                Person person = employee.getPerson();

                if (personRoleRepository.countPersonRoleByPersonId(person.getId()) == 1) {
                        personRepository.delete(person);
                }

                employeeRepository.delete(employee);

                return ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.OK)
                                .withMessage("Employee deleted successfully")
                                .build();
        }

        @Override
        public ResponseEntity<ApiResponse> getProfile() {
                String personId = authUtils.getCurrentUserId();
                Employee employee = employeeRepository.findByPersonId(personId)
                                .orElseThrow(() -> new ResourceNotFoundException("Employee profile not found"));

                return ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.OK)
                                .withData("Employee", userMapper.toEmployeeDto(employee))
                                .withMessage("Employee profile retrieved successfully")
                                .build();
        }
}
