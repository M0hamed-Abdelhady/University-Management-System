package com.university.management.system.utils.mappers.users;

import com.university.management.system.dtos.auth.AuthResponse;
import com.university.management.system.dtos.auth.ProfileResponse;
import com.university.management.system.models.users.Person;
import com.university.management.system.models.users.PersonRole;
import com.university.management.system.models.users.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PersonResponseMapper {

        @Mapping(target = "roles", source = "personRoles", qualifiedByName = "mapRoles")
        ProfileResponse toProfileDto(Person person);

        @Mapping(target = "roles", source = "person.personRoles", qualifiedByName = "mapRoles")
        @Mapping(target = "id", source = "person.id")
        @Mapping(target = "firstName", source = "person.firstName")
        @Mapping(target = "lastName", source = "person.lastName")
        @Mapping(target = "email", source = "person.email")
        AuthResponse toAuthResponse(Person person, String token);

        @Named("mapRoles")
        default List<Role> mapRoles(Set<PersonRole> personRoles) {
                if (personRoles == null)
                        return null;
                return personRoles.stream()
                                .map(PersonRole::getRole)
                                .collect(Collectors.toList());
        }
}
