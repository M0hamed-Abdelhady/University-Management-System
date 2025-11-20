package com.university.management.system.services.users;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.users.PersonDto;
import com.university.management.system.dtos.users.StudentDto;
import com.university.management.system.dtos.users.StudentRequestDto;
import com.university.management.system.models.users.Person;
import com.university.management.system.models.users.PersonRole;
import com.university.management.system.models.users.Role;
import com.university.management.system.models.users.Student;
import com.university.management.system.repositories.users.PersonRepository;
import com.university.management.system.repositories.users.PersonRoleRepository;
import com.university.management.system.repositories.users.StudentRepository;
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
public class StudentService implements IStudentService {
    private final PersonService personService;
    private final StudentRepository studentRepository;
    private final PersonRepository personRepository;
    private final PersonRoleRepository personRoleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RepositoryUtils repositoryUtils;

    @Override
    public ResponseEntity<ApiResponse> getAllStudents(Integer page, Integer size) {
        Pageable pageable = repositoryUtils.getPageable(page, size, Sort.Direction.ASC, "createdAt");
        Page<Student> students = studentRepository.findAll(pageable);

        List<StudentDto> response = students
                .map(userMapper::toStudentDto)
                .toList();

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withData("Students", response)
                .withData("TotalPages", students.getTotalPages())
                .withData("TotalElements", students.getTotalElements())
                .withMessage("Students retrieved successfully")
                .build();
    }

    @Override
    public ResponseEntity<ApiResponse> getStudentById(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withData("Student", userMapper.toStudentDto(student))
                .withMessage("Student retrieved successfully")
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> createStudent(StudentRequestDto studentRequestDto) {
        if (personRepository.existsByEmail(studentRequestDto.getEmail())) {
            throw new RuntimeException("Email already exists: " + studentRequestDto.getEmail());
        }

        PersonDto personDto = PersonDto.builder()
                .firstName(studentRequestDto.getFirstName())
                .lastName(studentRequestDto.getLastName())
                .email(studentRequestDto.getEmail())
                .phone(studentRequestDto.getPhone())
                .address(studentRequestDto.getAddress())
                .dateOfBirth(studentRequestDto.getDateOfBirth())
                .build();

        personService.createPerson(personDto);
        Person person = personRepository.findByEmail(studentRequestDto.getEmail()).orElseThrow();
        Student student = Student.builder()
                .person(person)
                .studentNumber(UUID.randomUUID().toString())
                .major(studentRequestDto.getMajor())
                .academicYear(studentRequestDto.getAcademicYear())
                .gpa(studentRequestDto.getGpa())
                .status(studentRequestDto.getStatus())
                .build();

        person.setPassword(passwordEncoder.encode("Password123!"));

        // Assign STUDENT role
        PersonRole studentRole = PersonRole.builder()
                .person(person)
                .role(Role.STUDENT)
                .build();
        person.setPersonRoles(Collections.singleton(studentRole));

        Student savedStudent = studentRepository.save(student);

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.CREATED)
                .withData("Student", userMapper.toStudentDto(savedStudent))
                .withMessage("Student created successfully")
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> updateStudent(String id, StudentRequestDto studentRequestDto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        Person person = student.getPerson();
        person.setFirstName(studentRequestDto.getFirstName());
        person.setLastName(studentRequestDto.getLastName());
        person.setPhone(studentRequestDto.getPhone());
        person.setAddress(studentRequestDto.getAddress());
        person.setDateOfBirth(studentRequestDto.getDateOfBirth());

        student.setMajor(studentRequestDto.getMajor());
        student.setAcademicYear(studentRequestDto.getAcademicYear());
        student.setGpa(studentRequestDto.getGpa());
        student.setStatus(studentRequestDto.getStatus());

        personRepository.save(person);
        Student savedStudent = studentRepository.save(student);

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withData("Student", userMapper.toStudentDto(savedStudent))
                .withMessage("Student updated successfully")
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> deleteStudent(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));

        studentRepository.delete(student);

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withMessage("Student deleted successfully")
                .build();
    }
}
