package com.university.management.system.services.users;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.users.EmployeeDto;
import com.university.management.system.dtos.users.EmployeeRequestDto;
import com.university.management.system.dtos.users.EmployeeUpdateDto;
import com.university.management.system.dtos.users.PersonDto;
import com.university.management.system.dtos.users.PersonRequestDto;
import com.university.management.system.models.users.Employee;
import com.university.management.system.models.users.EmployeeStatus;
import com.university.management.system.models.users.Person;
import com.university.management.system.models.users.PersonRole;
import com.university.management.system.models.users.Position;
import com.university.management.system.repositories.users.EmployeeRepository;
import com.university.management.system.repositories.users.PersonRepository;
import com.university.management.system.repositories.users.PersonRoleRepository;
import com.university.management.system.utils.RepositoryUtils;
import com.university.management.system.utils.mappers.users.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

        @Mock
        private PersonService personService;

        @Mock
        private EmployeeRepository employeeRepository;

        @Mock
        private PersonRepository personRepository;

        @Mock
        private PersonRoleRepository personRoleRepository;

        @Mock
        private UserMapper userMapper;

        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private RepositoryUtils repositoryUtils;

        @InjectMocks
        private EmployeeService employeeService;

        @Test
        public void whenCreateEmployee_thenEmployeeCreated() {
                // given
                EmployeeRequestDto requestDto = EmployeeRequestDto.builder()
                                .email("employee@example.com")
                                .password("password")
                                .firstName("Employee")
                                .lastName("One")
                                .build();
                Person person = Person.builder()
                                .id("1")
                                .email("employee@example.com")
                                .build();
                Employee employee = Employee.builder()
                                .id("1")
                                .person(person)
                                .build();
                EmployeeDto responseDto = EmployeeDto.builder()
                                .id("1")
                                .person(PersonDto.builder().email("employee@example.com").build())
                                .build();

                when(personRepository.existsByEmail(anyString())).thenReturn(false);
                when(personRepository.findByEmail(anyString())).thenReturn(Optional.of(person));
                when(personRoleRepository.save(any(PersonRole.class))).thenReturn(new PersonRole());
                when(personRepository.save(any(Person.class))).thenReturn(person);
                when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
                when(userMapper.toEmployeeDto(any(Employee.class))).thenReturn(responseDto);

                // when
                ResponseEntity<ApiResponse> response = employeeService.createEmployee(requestDto);

                // then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
                assertThat(response.getBody().getData()).containsEntry("Employee", responseDto);
                verify(personService).createPerson(any(PersonRequestDto.class));
                verify(employeeRepository).save(any(Employee.class));
        }

        @Test
        public void whenUpdateEmployee_thenEmployeeUpdated() {
                // given
                String id = "1";
                EmployeeUpdateDto updateDto = EmployeeUpdateDto.builder()
                                .email("updated@example.com")
                                .firstName("Updated")
                                .lastName("Employee")
                                .position(Position.LECTURER)
                                .status(EmployeeStatus.ACTIVE)
                                .build();
                Person person = Person.builder()
                                .id("1")
                                .email("employee@example.com")
                                .build();
                Employee employee = Employee.builder()
                                .id(id)
                                .person(person)
                                .build();
                EmployeeDto responseDto = EmployeeDto.builder()
                                .id(id)
                                .person(PersonDto.builder().email("updated@example.com").build())
                                .build();

                when(employeeRepository.findById(id)).thenReturn(Optional.of(employee));
                when(personRepository.save(any(Person.class))).thenReturn(person);
                when(employeeRepository.save(any(Employee.class))).thenReturn(employee);
                when(userMapper.toEmployeeDto(any(Employee.class))).thenReturn(responseDto);

                // when
                ResponseEntity<ApiResponse> response = employeeService.updateEmployee(id, updateDto);

                // then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody().getData()).containsEntry("Employee", responseDto);
                verify(personRepository).save(any(Person.class));
                verify(employeeRepository).save(any(Employee.class));
        }
}
