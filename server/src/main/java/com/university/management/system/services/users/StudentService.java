package com.university.management.system.services.users;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.courses.CourseClassDto;
import com.university.management.system.dtos.courses.EnrollmentDto;
import com.university.management.system.dtos.courses.EnrollmentRequestDto;
import com.university.management.system.dtos.users.PersonRequestDto;
import com.university.management.system.dtos.users.StudentDto;
import com.university.management.system.dtos.users.StudentRequestDto;
import com.university.management.system.dtos.users.StudentUpdateDto;
import com.university.management.system.exceptions.ResourceNotFoundException;
import com.university.management.system.models.courses.Course;
import com.university.management.system.models.courses.CourseClass;
import com.university.management.system.models.courses.Enrollment;
import com.university.management.system.models.courses.EnrollmentStatus;
import com.university.management.system.models.users.Person;
import com.university.management.system.models.users.PersonRole;
import com.university.management.system.models.users.Role;
import com.university.management.system.models.users.Student;
import com.university.management.system.repositories.courses.CourseClassRepository;
import com.university.management.system.repositories.courses.EnrollmentRepository;
import com.university.management.system.repositories.users.PersonRepository;
import com.university.management.system.repositories.users.PersonRoleRepository;
import com.university.management.system.repositories.users.StudentRepository;
import com.university.management.system.services.courses.EnrollmentService;
import com.university.management.system.utils.AuthUtils;
import com.university.management.system.utils.GradesUtils;
import com.university.management.system.utils.RepositoryUtils;
import com.university.management.system.utils.ResponseEntityBuilder;
import com.university.management.system.utils.mappers.courses.CourseMapper;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService implements IStudentService {
        private final PersonService personService;
        private final EnrollmentService enrollmentService;
        private final CourseClassRepository courseClassRepository;
        private final EnrollmentRepository enrollmentRepository;
        private final StudentRepository studentRepository;
        private final PersonRepository personRepository;
        private final PersonRoleRepository personRoleRepository;
        private final UserMapper userMapper;
        private final CourseMapper courseMapper;
        private final PasswordEncoder passwordEncoder;
        private final RepositoryUtils repositoryUtils;
        private final AuthUtils authUtils;
        private final GradesUtils gradesUtils;

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
                                .withData("pagination", java.util.Map.of(
                                                "currentPage", students.getNumber(),
                                                "totalPages", students.getTotalPages(),
                                                "totalItems", students.getTotalElements(),
                                                "pageSize", students.getSize()))
                                .withMessage("Students retrieved successfully")
                                .build();
        }

        @Override
        public ResponseEntity<ApiResponse> getStudentById(String id) {
                Student student = studentRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

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

                PersonRequestDto personRequestDto = PersonRequestDto.builder()
                                .firstName(studentRequestDto.getFirstName())
                                .lastName(studentRequestDto.getLastName())
                                .email(studentRequestDto.getEmail())
                                .password(studentRequestDto.getPassword())
                                .phone(studentRequestDto.getPhone())
                                .address(studentRequestDto.getAddress())
                                .dateOfBirth(studentRequestDto.getDateOfBirth())
                                .build();

                personService.createPerson(personRequestDto);
                Person person = personRepository.findByEmail(studentRequestDto.getEmail())
                                .orElseThrow(() -> new ResourceNotFoundException(
                                                "Person not found with email: " + studentRequestDto.getEmail()));

                PersonRole studentRole = PersonRole.builder()
                                .person(person)
                                .role(Role.STUDENT)
                                .build();

                personRoleRepository.save(studentRole);
                person.setPersonRoles(new HashSet<>(Collections.singleton(studentRole)));
                person = personRepository.save(person);

                Student student = Student.builder()
                                .person(person)
                                .studentNumber(UUID.randomUUID().toString())
                                .major(studentRequestDto.getMajor())
                                .academicYear(studentRequestDto.getAcademicYear())
                                .gpa(studentRequestDto.getGpa())
                                .status(studentRequestDto.getStatus())
                                .build();

                Student savedStudent = studentRepository.save(student);

                return ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.CREATED)
                                .withData("Student", userMapper.toStudentDto(savedStudent))
                                .withMessage("Student created successfully")
                                .build();
        }

        @Override
        @Transactional
        public ResponseEntity<ApiResponse> updateStudent(String id, StudentUpdateDto studentUpdateDto) {
                Student student = studentRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

                Person person = student.getPerson();
                person.setFirstName(studentUpdateDto.getFirstName());
                person.setLastName(studentUpdateDto.getLastName());
                person.setPhone(studentUpdateDto.getPhone());
                person.setAddress(studentUpdateDto.getAddress());
                person.setDateOfBirth(studentUpdateDto.getDateOfBirth());
                personRepository.save(person);

                student.setMajor(studentUpdateDto.getMajor());
                student.setAcademicYear(studentUpdateDto.getAcademicYear());
                student.setGpa(studentUpdateDto.getGpa());
                student.setStatus(studentUpdateDto.getStatus());

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
                                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

                Person person = student.getPerson();

                if (personRoleRepository.countPersonRoleByPersonId(person.getId()) == 1) {
                        personRepository.delete(person);
                }

                studentRepository.delete(student);

                return ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.OK)
                                .withMessage("Student deleted successfully")
                                .build();
        }

        @Override
        public ResponseEntity<ApiResponse> getStudentEnrollments(Integer page, Integer size) {
                String personId = authUtils.getCurrentUserId();
                Student student = studentRepository.findByPersonId(personId)
                                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

                Pageable pageable = repositoryUtils.getPageable(page, size, Sort.Direction.ASC, "createdAt");
                Page<Enrollment> enrollments = enrollmentRepository.findAllByStudentId(student.getId(), pageable);

                List<EnrollmentDto> response = enrollments
                                .map(courseMapper::toEnrollmentDto)
                                .toList();

                return ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.OK)
                                .withData("Enrollments", response)
                                .withData("pagination", java.util.Map.of(
                                                "currentPage", enrollments.getNumber(),
                                                "totalPages", enrollments.getTotalPages(),
                                                "totalItems", enrollments.getTotalElements(),
                                                "pageSize", enrollments.getSize()))
                                .withMessage("Enrollments retrieved successfully")
                                .build();
        }

        @Transactional
        @Override
        public ResponseEntity<ApiResponse> enrollStudent(String classId) {
                String personId = authUtils.getCurrentUserId();
                Student student = studentRepository.findByPersonId(personId)
                                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

                if (!courseClassRepository.existsById(classId)) {
                        throw new ResourceNotFoundException("Course Class not found");
                }

                if (enrollmentRepository.existsByStudentIdAndCourseClassId(student.getId(), classId)) {
                        return ResponseEntityBuilder.create()
                                        .withStatus(HttpStatus.BAD_REQUEST)
                                        .withMessage("Student is already enrolled in this class")
                                        .build();
                }

                EnrollmentRequestDto enrollmentRequestDto = EnrollmentRequestDto.builder()
                                .studentId(student.getId())
                                .classId(classId)
                                .status(EnrollmentStatus.ENROLLED)
                                .build();
                return enrollmentService.createEnrollment(enrollmentRequestDto);
        }

        @Override
        @Transactional
        public ResponseEntity<ApiResponse> dropStudent(String enrollmentId) {
                String personId = authUtils.getCurrentUserId();
                Student student = studentRepository.findByPersonId(personId)
                                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

                Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

                if (!enrollment.getStudent().getId().equals(student.getId())) {
                        return ResponseEntityBuilder.create()
                                        .withStatus(HttpStatus.BAD_REQUEST)
                                        .withMessage("Enrollment does not belong to the specified student")
                                        .build();
                }

                return enrollmentService.deleteEnrollment(enrollmentId);
        }

        @Override
        public ResponseEntity<ApiResponse> getStudentClasses(Integer page, Integer size) {
                String personId = authUtils.getCurrentUserId();
                Student student = studentRepository.findByPersonId(personId)
                                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

                List<Enrollment> enrolledClasses = enrollmentRepository.findAllByStudentId(student.getId());

                Pageable pageable = repositoryUtils.getPageable(page, size, Sort.Direction.ASC, "createdAt");

                Page<CourseClass> classes;
                List<String> enrolledClassIds = enrolledClasses.stream()
                                .map(enrollment -> enrollment.getCourseClass().getId())
                                .toList();

                if (enrolledClassIds.isEmpty()) {
                        classes = courseClassRepository.findAll(pageable);
                } else {
                        classes = courseClassRepository.findAllByIdNotIn(enrolledClassIds, pageable);
                }

                List<CourseClassDto> response = classes.map(courseMapper::toCourseClassDto).toList();

                return ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.OK)
                                .withData("Classes", response)
                                .withData("pagination", java.util.Map.of(
                                                "currentPage", classes.getNumber(),
                                                "totalPages", classes.getTotalPages(),
                                                "totalItems", classes.getTotalElements(),
                                                "pageSize", classes.getSize()))
                                .withMessage("Classes retrieved successfully")
                                .build();
        }

        @Override
        public ResponseEntity<ApiResponse> updateGPA(String id) {
                Student student = studentRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));

                List<Enrollment> enrollments = enrollmentRepository.findAllByStudentId(student.getId());
                double totalCredits = 0.0;
                double totalGradePoints = 0.0;

                for (Enrollment enrollment : enrollments) {
                        if (enrollment.getGrade() != null && !enrollment.getGrade().isEmpty()) {
                                CourseClass courseClass = enrollment.getCourseClass();
                                Course course = courseClass.getCourse();
                                Integer credits = course.getCredits();

                                if (credits != null && credits > 0) {
                                        Double gradePoint = gradesUtils.convertGradeToPoint(enrollment.getGrade());
                                        totalCredits += credits;
                                        totalGradePoints += gradePoint * credits;
                                }
                        }
                }
                BigDecimal gpa = totalCredits > 0
                                ? BigDecimal.valueOf(totalGradePoints / totalCredits).setScale(2, RoundingMode.HALF_UP)
                                : BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
                student.setGpa(gpa);
                Student savedStudent = studentRepository.save(student);

                return ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.OK)
                                .withData("Student", userMapper.toStudentDto(savedStudent))
                                .withData("GPA", gpa)
                                .withMessage("GPA updated successfully")
                                .build();
        }

        @Override
        public ResponseEntity<ApiResponse> getProfile() {
                String personId = authUtils.getCurrentUserId();
                Student student = studentRepository.findByPersonId(personId)
                                .orElseThrow(() -> new ResourceNotFoundException("Student profile not found"));

                return ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.OK)
                                .withData("Student", userMapper.toStudentDto(student))
                                .withMessage("Student profile retrieved successfully")
                                .build();
        }
}
