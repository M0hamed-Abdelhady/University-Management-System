package com.university.management.system.utils.mappers.users;

import com.university.management.system.dtos.users.*;
import com.university.management.system.models.users.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "role", source = "personRoles", qualifiedByName = "mapPersonRolesToRole")
    PersonDto toPersonDto(Person person);

    @Mapping(target = "personRoles", source = "role", qualifiedByName = "mapRoleToPersonRoles")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    @Mapping(target = "password", ignore = true) // PersonDto doesn't have password
    Person toPerson(PersonDto personDto);

    @Mapping(target = "personRoles", source = "role", qualifiedByName = "mapRoleToPersonRoles")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Person toPerson(PersonRequestDto personRequestDto);

    @Mapping(target = "person", source = "person")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Student toStudent(StudentDto studentDto);

    @Mapping(target = "person", source = "person")
    StudentDto toStudentDto(Student student);

    @Mapping(target = "person", source = "person")
    @Mapping(target = "employeeNumber", source = "employeeId")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Employee toEmployee(EmployeeDto employeeDto);

    @Mapping(target = "person", source = "person")
    @Mapping(target = "employeeId", source = "employeeNumber")
    EmployeeDto toEmployeeDto(Employee employee);

    @Named("mapRoleToPersonRoles")
    default Set<PersonRole> mapRoleToPersonRoles(Role role) {
        if (role == null) {
            return null;
        }
        return new HashSet<>(Set.of(PersonRole.builder().role(role).build()));
    }

    @Named("mapPersonRolesToRole")
    default Role mapPersonRolesToRole(Set<PersonRole> personRoles) {
        if (personRoles == null || personRoles.isEmpty()) {
            return null;
        }
        return personRoles.iterator().next().getRole();
    }
}
