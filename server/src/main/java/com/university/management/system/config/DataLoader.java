package com.university.management.system.config;

import com.university.management.system.models.users.Person;
import com.university.management.system.models.users.PersonRole;
import com.university.management.system.models.users.Role;
import com.university.management.system.repositories.users.PersonRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Profile("!test")
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public void loadData() {
        loadAdmins();
        loadUsers();
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

            admin.setPersonRoles(Set.of(adminRole));
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

            student.setPersonRoles(Set.of(studentRole));
            personRepository.save(student);
        }
    }

    @Override
    public void run(String... args) throws Exception {
        loadData();
    }
}
