package com.university.management.system.repositories.users;

import com.university.management.system.models.users.Person;
import com.university.management.system.models.users.Student;
import com.university.management.system.models.users.StudentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class StudentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void whenFindById_thenReturnStudent() {
        // given
        Person person = Person.builder()
                .firstName("Student")
                .lastName("One")
                .email("student.one@example.com")
                .password("password")
                .build();
        entityManager.persist(person);

        Student student = Student.builder()
                .person(person)
                .studentNumber("S12345")
                .status(StudentStatus.ACTIVE)
                .build();
        entityManager.persist(student);
        entityManager.flush();

        // when
        Optional<Student> found = studentRepository.findById(student.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getStudentNumber()).isEqualTo(student.getStudentNumber());
    }
}
