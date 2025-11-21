package com.university.management.system.repositories.users;

import com.university.management.system.models.users.Person;
import com.university.management.system.models.users.PersonRole;
import com.university.management.system.models.users.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class PersonRoleRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonRoleRepository personRoleRepository;

    @Test
    public void whenFindByPerson_thenReturnRoles() {
        // given
        Person person = Person.builder()
                .email("test@example.com")
                .password("password")
                .firstName("Test")
                .lastName("User")
                .build();
        entityManager.persist(person);

        PersonRole personRole = PersonRole.builder()
                .person(person)
                .role(Role.STUDENT)
                .build();
        entityManager.persist(personRole);
        entityManager.flush();

        // when
        List<PersonRole> found = personRoleRepository.findByPerson(person);

        // then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getRole()).isEqualTo(Role.STUDENT);
    }

    @Test
    public void whenFindByRole_thenReturnPersonRoles() {
        // given
        Person person = Person.builder()
                .email("admin@example.com")
                .password("password")
                .firstName("Admin")
                .lastName("User")
                .build();
        entityManager.persist(person);

        PersonRole personRole = PersonRole.builder()
                .person(person)
                .role(Role.ADMIN)
                .build();
        entityManager.persist(personRole);
        entityManager.flush();

        // when
        Page<PersonRole> found = personRoleRepository.findByRole(Role.ADMIN, PageRequest.of(0, 10));

        // then
        assertThat(found.getContent()).isNotEmpty();
        assertThat(found.getContent().get(0).getRole()).isEqualTo(Role.ADMIN);
    }

    @Test
    public void whenCountPersonRoleByPersonId_thenReturnCount() {
        // given
        Person person = Person.builder()
                .email("count@example.com")
                .password("password")
                .firstName("Count")
                .lastName("User")
                .build();
        entityManager.persist(person);

        PersonRole personRole1 = PersonRole.builder()
                .person(person)
                .role(Role.STUDENT)
                .build();
        entityManager.persist(personRole1);

        PersonRole personRole2 = PersonRole.builder()
                .person(person)
                .role(Role.EMPLOYEE)
                .build();
        entityManager.persist(personRole2);
        entityManager.flush();

        // when
        long count = personRoleRepository.countPersonRoleByPersonId(person.getId());

        // then
        assertThat(count).isEqualTo(2);
    }
}
