package com.university.management.system.utils.mappers.courses;

import com.university.management.system.dtos.courses.CourseClassDto;
import com.university.management.system.dtos.courses.CourseDto;
import com.university.management.system.dtos.courses.EnrollmentDto;
import com.university.management.system.models.courses.Course;
import com.university.management.system.models.courses.CourseClass;
import com.university.management.system.models.courses.Enrollment;
import com.university.management.system.utils.mappers.users.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = { UserMapper.class })
public interface CourseMapper {

    CourseDto toCourseDto(Course course);

    @Mapping(target = "courseClasses", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Course toCourse(CourseDto courseDto);

    @Mapping(target = "course", source = "course")
    @Mapping(target = "lecturer", source = "lecturer")
    CourseClassDto toCourseClassDto(CourseClass courseClass);

    @Mapping(target = "course", source = "course")
    @Mapping(target = "lecturer", source = "lecturer")
    @Mapping(target = "enrollments", ignore = true)
    @Mapping(target = "courseTeachingAssistants", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    CourseClass toCourseClass(CourseClassDto courseClassDto);

    @Mapping(target = "student", source = "student")
    @Mapping(target = "courseClass", source = "courseClass")
    EnrollmentDto toEnrollmentDto(Enrollment enrollment);

    @Mapping(target = "student", source = "student")
    @Mapping(target = "courseClass", source = "courseClass")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "version", ignore = true)
    Enrollment toEnrollment(EnrollmentDto enrollmentDto);
}
