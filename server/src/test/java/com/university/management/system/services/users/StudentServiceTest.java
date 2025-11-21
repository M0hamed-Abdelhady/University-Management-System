package com.university.management.system.services.users;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.users.PersonDto;
import com.university.management.system.dtos.users.PersonRequestDto;
import com.university.management.system.dtos.users.StudentDto;
import com.university.management.system.dtos.users.StudentRequestDto;
import com.university.management.system.dtos.users.StudentUpdateDto;
import com.university.management.system.models.users.Person;
import com.university.management.system.models.users.PersonRole;
import com.university.management.system.models.users.Student;
import com.university.management.system.models.users.StudentStatus;
import com.university.management.system.repositories.users.PersonRepository;
import com.university.management.system.repositories.users.PersonRoleRepository;
import com.university.management.system.repositories.users.StudentRepository;
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
public class StudentServiceTest {

        @Mock
        private PersonService personService;

        @Mock
        private StudentRepository studentRepository;

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
        private StudentService studentService;

        @Test
        public void whenCreateStudent_thenStudentCreated() {
                // given
                StudentRequestDto requestDto = StudentRequestDto.builder()
                                .email("student@example.com")
                                .password("password")
                                .firstName("Student")
                                .lastName("One")
                                .build();
                Person person = Person.builder()
                                .id("1")
                                .email("student@example.com")
                                .build();
                Student student = Student.builder()
                                .id("1")
                                .person(person)
                                .build();
                StudentDto responseDto = StudentDto.builder()
                                .id("1")
                                .person(PersonDto.builder().email("student@example.com").build())
                                .build();

                when(personRepository.existsByEmail(anyString())).thenReturn(false);
                when(personRepository.findByEmail(anyString())).thenReturn(Optional.of(person));
                when(personRoleRepository.save(any(PersonRole.class))).thenReturn(new PersonRole());
                when(personRepository.save(any(Person.class))).thenReturn(person);
                when(studentRepository.save(any(Student.class))).thenReturn(student);
                when(userMapper.toStudentDto(any(Student.class))).thenReturn(responseDto);

                // when
                ResponseEntity<ApiResponse> response = studentService.createStudent(requestDto);

                // then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
                assertThat(response.getBody().getData()).containsEntry("Student", responseDto);
                verify(personService).createPerson(any(PersonRequestDto.class));
                verify(studentRepository).save(any(Student.class));
        }

        @Test
        public void whenUpdateStudent_thenStudentUpdated() {
                // given
                String id = "1";
                StudentUpdateDto updateDto = StudentUpdateDto.builder()
                                .email("updated@example.com")
                                .firstName("Updated")
                                .lastName("Student")
                                .status(StudentStatus.ACTIVE)
                                .build();
                Person person = Person.builder()
                                .id("1")
                                .email("student@example.com")
                                .build();
                Student student = Student.builder()
                                .id(id)
                                .person(person)
                                .build();
                StudentDto responseDto = StudentDto.builder()
                                .id(id)
                                .person(PersonDto.builder().email("updated@example.com").build())
                                .build();

                when(studentRepository.findById(id)).thenReturn(Optional.of(student));
                when(personRepository.save(any(Person.class))).thenReturn(person);
                when(studentRepository.save(any(Student.class))).thenReturn(student);
                when(userMapper.toStudentDto(any(Student.class))).thenReturn(responseDto);

                // when
                ResponseEntity<ApiResponse> response = studentService.updateStudent(id, updateDto);

                // then
                assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
                assertThat(response.getBody().getData()).containsEntry("Student", responseDto);
                verify(personRepository).save(any(Person.class));
                verify(studentRepository).save(any(Student.class));
        }
}
