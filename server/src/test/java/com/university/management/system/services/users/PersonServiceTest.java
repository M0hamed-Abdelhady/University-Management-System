package com.university.management.system.services.users;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.users.PersonDto;
import com.university.management.system.dtos.users.PersonRequestDto;
import com.university.management.system.models.users.Person;
import com.university.management.system.repositories.users.PersonRepository;
import com.university.management.system.repositories.users.PersonRoleRepository;
import com.university.management.system.utils.RepositoryUtils;
import com.university.management.system.utils.mappers.users.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PersonRoleRepository personRoleRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RepositoryUtils repositoryUtils;

    @InjectMocks
    private PersonService personService;

    @Test
    public void whenCreatePerson_thenReturnCreated() {
        // given
        PersonRequestDto requestDto = PersonRequestDto.builder()
                .email("test@example.com")
                .password("password")
                .firstName("Test")
                .lastName("User")
                .build();

        Person person = Person.builder()
                .id("1")
                .email("test@example.com")
                .password("encodedPassword")
                .firstName("Test")
                .lastName("User")
                .build();

        PersonDto responseDto = PersonDto.builder()
                .id("1")
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .build();

        when(personRepository.existsByEmail(requestDto.getEmail())).thenReturn(false);
        when(userMapper.toPerson(requestDto)).thenReturn(person);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(personRepository.save(any(Person.class))).thenReturn(person);
        when(userMapper.toPersonDto(person)).thenReturn(responseDto);

        // when
        ResponseEntity<ApiResponse> response = personService.createPerson(requestDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getData().get("Person")).isEqualTo(responseDto);
    }

    @Test
    public void whenGetPersonById_thenReturnPerson() {
        // given
        String id = "1";
        Person person = Person.builder()
                .id(id)
                .email("test@example.com")
                .build();
        PersonDto responseDto = PersonDto.builder()
                .id(id)
                .email("test@example.com")
                .build();

        when(personRepository.findById(id)).thenReturn(Optional.of(person));
        when(userMapper.toPersonDto(person)).thenReturn(responseDto);

        // when
        ResponseEntity<ApiResponse> response = personService.getPersonById(id);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getData().get("Person")).isEqualTo(responseDto);
    }

    @Test
    public void whenUpdatePerson_thenReturnUpdated() {
        // given
        String id = "1";
        PersonRequestDto requestDto = PersonRequestDto.builder()
                .firstName("Updated")
                .lastName("User")
                .email("updated@example.com")
                .build();

        Person person = Person.builder()
                .id(id)
                .email("test@example.com")
                .build();

        Person updatedPerson = Person.builder()
                .id(id)
                .email("updated@example.com")
                .firstName("Updated")
                .lastName("User")
                .build();

        PersonDto responseDto = PersonDto.builder()
                .id(id)
                .email("updated@example.com")
                .firstName("Updated")
                .lastName("User")
                .build();

        when(personRepository.findById(id)).thenReturn(Optional.of(person));
        when(personRepository.save(any(Person.class))).thenReturn(updatedPerson);
        when(userMapper.toPersonDto(updatedPerson)).thenReturn(responseDto);

        // when
        ResponseEntity<ApiResponse> response = personService.updatePerson(id, requestDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getData().get("Person")).isEqualTo(responseDto);
    }

    @Test
    public void whenDeletePerson_thenReturnNoContent() {
        // given
        String id = "1";
        when(personRepository.existsById(id)).thenReturn(true);

        // when
        ResponseEntity<ApiResponse> response = personService.deletePerson(id);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(personRepository, times(1)).deleteById(id);
    }
}
