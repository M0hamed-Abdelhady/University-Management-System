package com.university.management.system.repositories.users;

import com.university.management.system.models.users.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PersonRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonRepository personRepository;

    @Test
    public void whenExistsByEmail_thenReturnTrue() {
        // given
        Person person = Person.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password")
                .build();
        entityManager.persist(person);
        entityManager.flush();

        // when
        Boolean found = personRepository.existsByEmail(person.getEmail());

        // then
        assertThat(found).isTrue();
    }

    @Test
    public void whenFindByEmail_thenReturnPerson() {
        // given
        Person person = Person.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .password("password")
                .build();
        entityManager.persist(person);
        entityManager.flush();

        // when
        Optional<Person> found = personRepository.findByEmail(person.getEmail());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo(person.getEmail());
    }
}
