package com.university.management.system.repositories.users;

import com.university.management.system.models.users.Employee;
import com.university.management.system.models.users.Position;
import com.university.management.system.models.users.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, String> {

    Optional<Employee> findByEmployeeNumber(String employeeNumber);

    boolean existsByEmployeeNumber(String employeeNumber);

    Page<Employee> findByPosition(Position position, Pageable pageable);

    Page<Employee> findByStatus(EmployeeStatus status, Pageable pageable);

    Page<Employee> findByPositionAndStatus(Position position, EmployeeStatus status, Pageable pageable);

    Optional<Employee> findByPersonId(String personId);

    boolean existsByPersonId(String personId);
}
