package com.university.management.system.repositories.courses;

import com.university.management.system.models.courses.Course;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CourseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void whenFindByCourseCode_thenReturnCourse() {
        // given
        Course course = Course.builder()
                .courseCode("CS101")
                .title("Introduction to Computer Science")
                .credits(3)
                .description("Basic concepts of CS")
                .build();
        entityManager.persist(course);
        entityManager.flush();

        // when
        Optional<Course> found = courseRepository.findByCourseCode(course.getCourseCode());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getCourseCode()).isEqualTo(course.getCourseCode());
    }

    @Test
    public void whenExistsByCourseCode_thenReturnTrue() {
        // given
        Course course = Course.builder()
                .courseCode("CS102")
                .title("Data Structures")
                .credits(3)
                .description("Advanced data structures")
                .build();
        entityManager.persist(course);
        entityManager.flush();

        // when
        boolean exists = courseRepository.existsByCourseCode(course.getCourseCode());

        // then
        assertThat(exists).isTrue();
    }
}
