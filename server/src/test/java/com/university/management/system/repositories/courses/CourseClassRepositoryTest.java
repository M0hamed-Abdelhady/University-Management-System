package com.university.management.system.repositories.courses;

import com.university.management.system.models.courses.Course;
import com.university.management.system.models.courses.CourseClass;
import com.university.management.system.models.courses.CourseClassStatus;
import com.university.management.system.models.users.Employee;
import com.university.management.system.models.users.EmployeeStatus;
import com.university.management.system.models.users.Person;
import com.university.management.system.models.users.Position;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CourseClassRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseClassRepository courseClassRepository;

    @Test
    public void whenFindByCourse_thenReturnCourseClasses() {
        // given
        Course course = Course.builder()
                .courseCode("CS101")
                .title("Intro to CS")
                .credits(3)
                .build();
        entityManager.persist(course);

        Person person = Person.builder()
                .email("lecturer1@example.com")
                .password("password")
                .firstName("John")
                .lastName("Doe")
                .build();
        entityManager.persist(person);

        Employee lecturer = Employee.builder()
                .person(person)
                .employeeNumber("EMP001")
                .position(Position.LECTURER)
                .status(EmployeeStatus.ACTIVE)
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
        entityManager.flush();

        // when
        Page<CourseClass> found = courseClassRepository.findByCourse(course, PageRequest.of(0, 10));

        // then
        assertThat(found.getContent()).hasSize(1);
        assertThat(found.getContent().get(0).getCourse().getCourseCode()).isEqualTo("CS101");
    }

    @Test
    public void whenFindByStatus_thenReturnCourseClasses() {
        // given
        Course course = Course.builder()
                .courseCode("CS102")
                .title("Data Structures")
                .credits(3)
                .build();
        entityManager.persist(course);

        Person person = Person.builder()
                .email("lecturer2@example.com")
                .password("password")
                .firstName("Jane")
                .lastName("Smith")
                .build();
        entityManager.persist(person);

        Employee lecturer = Employee.builder()
                .person(person)
                .employeeNumber("EMP002")
                .position(Position.LECTURER)
                .status(EmployeeStatus.ACTIVE)
                .build();
        entityManager.persist(lecturer);

        CourseClass courseClass = CourseClass.builder()
                .course(course)
                .lecturer(lecturer)
                .semester("Spring")
                .academicYear(2024)
                .status(CourseClassStatus.ACTIVE)
                .maxCapacity(30)
                .currentCapacity(0)
                .build();
        entityManager.persist(courseClass);
        entityManager.flush();

        // when
        Page<CourseClass> found = courseClassRepository.findByStatus(CourseClassStatus.ACTIVE, PageRequest.of(0, 10));

        // then
        assertThat(found.getContent()).isNotEmpty();
        assertThat(found.getContent().get(0).getStatus()).isEqualTo(CourseClassStatus.ACTIVE);
    }

    @Test
    public void whenFindByCourseAndSemesterAndAcademicYear_thenReturnCourseClass() {
        // given
        Course course = Course.builder()
                .courseCode("CS103")
                .title("Algorithms")
                .credits(3)
                .build();
        entityManager.persist(course);

        Person person = Person.builder()
                .email("lecturer3@example.com")
                .password("password")
                .firstName("Bob")
                .lastName("Jones")
                .build();
        entityManager.persist(person);

        Employee lecturer = Employee.builder()
                .person(person)
                .employeeNumber("EMP003")
                .position(Position.LECTURER)
                .status(EmployeeStatus.ACTIVE)
                .build();
        entityManager.persist(lecturer);

        CourseClass courseClass = CourseClass.builder()
                .course(course)
                .lecturer(lecturer)
                .semester("Fall")
                .academicYear(2024)
                .status(CourseClassStatus.ACTIVE)
                .maxCapacity(40)
                .currentCapacity(0)
                .build();
        entityManager.persist(courseClass);
        entityManager.flush();

        // when
        Optional<CourseClass> found = courseClassRepository.findByCourseAndSemesterAndAcademicYear(course, "Fall",
                2024);

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getSemester()).isEqualTo("Fall");
        assertThat(found.get().getAcademicYear()).isEqualTo(2024);
    }
}
