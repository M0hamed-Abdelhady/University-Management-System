package com.university.management.system.repositories.courses;

import com.university.management.system.models.courses.Course;
import com.university.management.system.models.courses.CourseClass;
import com.university.management.system.models.courses.CourseClassStatus;
import com.university.management.system.models.courses.Enrollment;
import com.university.management.system.models.courses.EnrollmentStatus;
import com.university.management.system.models.users.Person;
import com.university.management.system.models.users.Student;
import com.university.management.system.models.users.StudentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EnrollmentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Test
    public void whenFindAllByStudentId_thenReturnEnrollments() {
        // given
        Person person = Person.builder()
                .email("student@test.com")
                .password("password")
                .firstName("Student")
                .lastName("One")
                .build();
        entityManager.persist(person);

        Student student = Student.builder()
                .person(person)
                .studentNumber("STU001")
                .status(StudentStatus.ACTIVE)
                .build();
        entityManager.persist(student);

        Course course = Course.builder()
                .courseCode("CS101")
                .title("Intro to CS")
                .credits(3)
                .build();
        entityManager.persist(course);

        Person lecturerPerson = Person.builder()
                .email("lecturer@test.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .build();
        entityManager.persist(lecturerPerson);

        com.university.management.system.models.users.Employee lecturer = com.university.management.system.models.users.Employee
                .builder()
                .person(lecturerPerson)
                .employeeNumber("EMP001")
                .position(com.university.management.system.models.users.Position.LECTURER)
                .status(com.university.management.system.models.users.EmployeeStatus.ACTIVE)
                .build();
        entityManager.persist(lecturer);

        CourseClass courseClass = CourseClass.builder()
                .course(course)
                .lecturer(lecturer)
                .semester("Fall")
                .academicYear(2023)
                .status(CourseClassStatus.ACTIVE)
                .maxCapacity(30)
                .currentCapacity(0)
                .build();
        entityManager.persist(courseClass);

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .courseClass(courseClass)
                .status(EnrollmentStatus.ENROLLED)
                .build();
        entityManager.persist(enrollment);
        entityManager.flush();

        // when
        Page<Enrollment> found = enrollmentRepository.findAllByStudentId(student.getId(), PageRequest.of(0, 10));

        // then
        assertThat(found.getContent()).hasSize(1);
        assertThat(found.getContent().get(0).getStudent().getId()).isEqualTo(student.getId());
    }
}
