package com.university.management.system.config;

import com.university.management.system.models.courses.*;
import com.university.management.system.models.users.*;
import com.university.management.system.repositories.courses.*;
import com.university.management.system.repositories.users.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@Profile("test")
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final PersonRepository personRepository;
    private final EmployeeRepository employeeRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;
    private final CourseClassRepository courseClassRepository;
    private final CourseTeachingAssistantRepository courseTeachingAssistantRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void loadData() {
        loadAdmins();
        loadUsers();
        loadUniversityData();
    }

    private void loadAdmins() {
        if (!personRepository.existsByEmail("admin@university.com")) {
            Person admin = Person.builder()
                    .firstName("Admin")
                    .lastName("User")
                    .email("admin@university.com")
                    .password(passwordEncoder.encode("password"))
                    .build();

            PersonRole adminRole = PersonRole.builder()
                    .person(admin)
                    .role(Role.ADMIN)
                    .build();

            admin.setPersonRoles(new HashSet<>(Set.of(adminRole)));
            personRepository.save(admin);
        }
    }

    private void loadUsers() {
        if (!personRepository.existsByEmail("student@university.com")) {
            Person student = Person.builder()
                    .firstName("John")
                    .lastName("Doe")
                    .email("student@university.com")
                    .password(passwordEncoder.encode("password"))
                    .build();

            PersonRole studentRole = PersonRole.builder()
                    .person(student)
                    .role(Role.STUDENT)
                    .build();

            student.setPersonRoles(new HashSet<>(Set.of(studentRole)));
            personRepository.save(student);
        }
    }

    private void loadUniversityData() {
        if (courseRepository.count() > 0) {
            return;
        }

        List<Employee> lecturers = new ArrayList<>();
        List<Employee> teachingAssistants = new ArrayList<>();
        List<Student> students = new ArrayList<>();
        List<Course> courses = new ArrayList<>();
        List<CourseClass> courseClasses = new ArrayList<>();

        // 1. Create 10 Lecturers
        for (int i = 1; i <= 10; i++) {
            Person person = Person.builder()
                    .firstName("Lecturer" + i)
                    .lastName("Smith")
                    .email("lecturer" + i + "@university.com")
                    .password(passwordEncoder.encode("password"))
                    .build();

            PersonRole role = PersonRole.builder()
                    .person(person)
                    .role(Role.EMPLOYEE)
                    .build();
            person.setPersonRoles(new HashSet<>(Set.of(role)));
            personRepository.save(person);

            Employee lecturer = Employee.builder()
                    .person(person)
                    .employeeNumber("LEC" + String.format("%03d", i))
                    .hireDate(LocalDate.now())
                    .salary(BigDecimal.valueOf(5000 + (i * 100)))
                    .position(Position.LECTURER)
                    .status(EmployeeStatus.ACTIVE)
                    .build();
            lecturers.add(employeeRepository.save(lecturer));
        }

        // 2. Create 10 Teaching Assistants
        for (int i = 1; i <= 10; i++) {
            Person person = Person.builder()
                    .firstName("TA" + i)
                    .lastName("Jones")
                    .email("ta" + i + "@university.com")
                    .password(passwordEncoder.encode("password"))
                    .build();

            PersonRole role = PersonRole.builder()
                    .person(person)
                    .role(Role.EMPLOYEE)
                    .build();
            person.setPersonRoles(new HashSet<>(Set.of(role)));
            personRepository.save(person);

            Employee ta = Employee.builder()
                    .person(person)
                    .employeeNumber("TA" + String.format("%03d", i))
                    .hireDate(LocalDate.now())
                    .salary(BigDecimal.valueOf(2000 + (i * 50)))
                    .position(Position.TEACHING_ASSISTANT)
                    .status(EmployeeStatus.ACTIVE)
                    .build();
            teachingAssistants.add(employeeRepository.save(ta));
        }

        // 3. Create 10 Students
        for (int i = 1; i <= 10; i++) {
            Person person = Person.builder()
                    .firstName("Student" + i)
                    .lastName("Brown")
                    .email("student" + i + "@test.com")
                    .password(passwordEncoder.encode("password"))
                    .build();

            PersonRole role = PersonRole.builder()
                    .person(person)
                    .role(Role.STUDENT)
                    .build();
            person.setPersonRoles(new HashSet<>(Set.of(role)));
            personRepository.save(person);

            Student student = Student.builder()
                    .person(person)
                    .studentNumber("STU" + String.format("%03d", i))
                    .major("Computer Science")
                    .academicYear(1)
                    .gpa(BigDecimal.valueOf(3.5))
                    .status(StudentStatus.ACTIVE)
                    .build();
            students.add(studentRepository.save(student));
        }

        // 4. Create 10 Courses
        for (int i = 1; i <= 10; i++) {
            Course course = Course.builder()
                    .courseCode("CS" + String.format("%03d", i))
                    .title("Course Title " + i)
                    .description("Description for Course " + i)
                    .credits(3)
                    .build();
            courses.add(courseRepository.save(course));
        }

        // 5. Create 10 ClassCourses (1 Lecturer per class)
        for (int i = 0; i < 10; i++) {
            CourseClass courseClass = CourseClass.builder()
                    .course(courses.get(i))
                    .lecturer(lecturers.get(i)) // Distinct lecturer for each class
                    .semester("Fall 2025")
                    .academicYear(2025)
                    .currentCapacity(0)
                    .maxCapacity(50)
                    .status(CourseClassStatus.ACTIVE)
                    .build();
            courseClasses.add(courseClassRepository.save(courseClass));
        }

        // 6. Assign 1 TA per ClassCourse
        for (int i = 0; i < 10; i++) {
            CourseTeachingAssistant cta = CourseTeachingAssistant.builder()
                    .courseClass(courseClasses.get(i))
                    .teachingAssistant(teachingAssistants.get(i)) // Distinct TA for each class
                    .build();
            courseTeachingAssistantRepository.save(cta);
        }

        // 7. Enroll all Students in all ClassCourses
        for (CourseClass courseClass : courseClasses) {
            for (Student student : students) {
                Enrollment enrollment = Enrollment.builder()
                        .courseClass(courseClass)
                        .student(student)
                        .status(EnrollmentStatus.ENROLLED)
                        .build();
                enrollmentRepository.save(enrollment);
            }
            // Update capacity once per class
            courseClass.setCurrentCapacity(courseClass.getCurrentCapacity() + students.size());
            courseClassRepository.save(courseClass);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        loadData();
    }
}
