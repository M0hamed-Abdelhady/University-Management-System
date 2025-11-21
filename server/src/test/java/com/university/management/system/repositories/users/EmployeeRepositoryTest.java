package com.university.management.system.repositories.users;

import com.university.management.system.models.users.Employee;
import com.university.management.system.models.users.EmployeeStatus;
import com.university.management.system.models.users.Person;
import com.university.management.system.models.users.Position;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    public void whenFindById_thenReturnEmployee() {
        // given
        Person person = Person.builder()
                .firstName("Employee")
                .lastName("One")
                .email("employee.one@example.com")
                .password("password")
                .build();
        entityManager.persist(person);

        Employee employee = Employee.builder()
                .person(person)
                .employeeNumber("E12345")
                .hireDate(LocalDate.now())
                .salary(new BigDecimal("5000.00"))
                .position(Position.LECTURER)
                .status(EmployeeStatus.ACTIVE)
                .build();
        entityManager.persist(employee);
        entityManager.flush();

        // when
        Optional<Employee> found = employeeRepository.findById(employee.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getEmployeeNumber()).isEqualTo(employee.getEmployeeNumber());
    }
}
