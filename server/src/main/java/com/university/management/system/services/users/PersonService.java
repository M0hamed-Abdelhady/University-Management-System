package com.university.management.system.services.users;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.users.PersonDto;
import com.university.management.system.dtos.users.PersonRequestDto;
import com.university.management.system.models.users.Person;
import com.university.management.system.repositories.users.PersonRepository;
import com.university.management.system.repositories.users.PersonRoleRepository;
import com.university.management.system.utils.RepositoryUtils;
import com.university.management.system.utils.ResponseEntityBuilder;
import com.university.management.system.utils.mappers.users.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonService implements IPersonService {

    private final PersonRepository personRepository;
    private final PersonRoleRepository personRoleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RepositoryUtils repositoryUtils;

    @Override
    public ResponseEntity<ApiResponse> getAllPersons(Integer page, Integer size) {
        Pageable pageable = repositoryUtils.getPageable(page, size, Sort.Direction.ASC, "createdAt");
        Page<Person> persons = personRepository.findAll(pageable);

        List<PersonDto> response = persons
                .map(userMapper::toPersonDto)
                .toList();

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withData("Persons", response)
                .withData("pagination", java.util.Map.of(
                        "currentPage", persons.getNumber(),
                        "totalPages", persons.getTotalPages(),
                        "totalItems", persons.getTotalElements(),
                        "pageSize", persons.getSize()))
                .withMessage("Persons retrieved successfully")
                .build();
    }

    @Override
    public ResponseEntity<ApiResponse> getPersonById(String id) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withData("Person", userMapper.toPersonDto(person))
                .withMessage("Person retrieved successfully")
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> createPerson(PersonRequestDto personRequestDto) {
        if (personRepository.existsByEmail(personRequestDto.getEmail())) {
            throw new RuntimeException("Email already exists: " + personRequestDto.getEmail());
        }

        Person person = userMapper.toPerson(personRequestDto);
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        Person savedPerson = personRepository.save(person);

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.CREATED)
                .withData("Person", userMapper.toPersonDto(savedPerson))
                .withMessage("Person created successfully")
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> updatePerson(String id, PersonRequestDto personRequestDto) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
        person.setFirstName(personRequestDto.getFirstName());
        person.setLastName(personRequestDto.getLastName());
        person.setEmail(personRequestDto.getEmail());
        person.setPhone(personRequestDto.getPhone());
        person.setAddress(personRequestDto.getAddress());
        person = personRepository.save(person);

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withData("Person", userMapper.toPersonDto(person))
                .withMessage("Person updated successfully")
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> deletePerson(String id) {
        if (!personRepository.existsById(id)) {
            throw new RuntimeException("Person not found with id: " + id);
        }
        personRepository.deleteById(id);

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.NO_CONTENT)
                .withMessage("Person deleted successfully")
                .build();
    }
}
