package com.university.management.system.repositories.users;

import com.university.management.system.models.users.Person;
import com.university.management.system.models.users.PersonRole;
import com.university.management.system.models.users.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRoleRepository extends JpaRepository<PersonRole, String> {

    List<PersonRole> findByPerson(Person person);

    Page<PersonRole> findByRole(Role role, Pageable pageable);

    List<PersonRole> findByPersonAndRole(Person person, Role role);

    long countPersonRoleByPersonId(String personId);
}
