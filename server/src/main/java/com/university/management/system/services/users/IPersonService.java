package com.university.management.system.services.users;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.users.PersonDto;
import org.springframework.http.ResponseEntity;

public interface IPersonService {
    ResponseEntity<ApiResponse> getAllPersons(Integer page, Integer size);

    ResponseEntity<ApiResponse> getPersonById(String id);

    ResponseEntity<ApiResponse> createPerson(PersonDto personDto);

    ResponseEntity<ApiResponse> updatePerson(String id, PersonDto personDto);

    ResponseEntity<ApiResponse> deletePerson(String id);
}
