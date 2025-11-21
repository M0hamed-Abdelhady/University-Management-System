package com.university.management.system.services.courses;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.courses.CourseClassDto;
import com.university.management.system.dtos.courses.CourseClassRequestDto;
import com.university.management.system.models.courses.Course;
import com.university.management.system.models.courses.CourseClass;
import com.university.management.system.models.courses.CourseClassStatus;
import com.university.management.system.models.users.Employee;
import com.university.management.system.repositories.courses.CourseClassRepository;
import com.university.management.system.repositories.courses.CourseRepository;
import com.university.management.system.repositories.users.EmployeeRepository;
import com.university.management.system.utils.mappers.courses.CourseMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseClassServiceTest {

    @Mock
    private CourseClassRepository courseClassRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseClassService courseClassService;

    @Test
    public void whenCreateCourseClass_thenReturnCreated() {
        // given
        CourseClassRequestDto requestDto = CourseClassRequestDto.builder()
                .courseId("course1")
                .lecturerId("emp1")
                .semester("Fall")
                .academicYear(2024)
                .maxCapacity(30)
                .status(CourseClassStatus.ACTIVE)
                .build();

        Course course = Course.builder().id("course1").build();
        Employee lecturer = Employee.builder().id("emp1").build();
        CourseClass courseClass = CourseClass.builder()
                .id("class1")
                .course(course)
                .lecturer(lecturer)
                .build();
        CourseClassDto responseDto = CourseClassDto.builder().id("class1").build();

        when(courseRepository.findById("course1")).thenReturn(Optional.of(course));
        when(employeeRepository.findById("emp1")).thenReturn(Optional.of(lecturer));
        when(courseClassRepository.save(any(CourseClass.class))).thenReturn(courseClass);
        when(courseMapper.toCourseClassDto(courseClass)).thenReturn(responseDto);

        // when
        ResponseEntity<ApiResponse> response = courseClassService.createCourseClass(requestDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getData().get("Class")).isEqualTo(responseDto);
    }

    @Test
    public void whenUpdateCourseClass_thenReturnUpdated() {
        // given
        String id = "class1";
        CourseClassRequestDto requestDto = CourseClassRequestDto.builder()
                .courseId("course1")
                .lecturerId("emp1")
                .semester("Spring")
                .academicYear(2025)
                .maxCapacity(40)
                .status(CourseClassStatus.ACTIVE)
                .build();

        Course course = Course.builder().id("course1").build();
        Employee lecturer = Employee.builder().id("emp1").build();
        CourseClass courseClass = CourseClass.builder()
                .id(id)
                .course(course)
                .lecturer(lecturer)
                .build();
        CourseClassDto responseDto = CourseClassDto.builder().id(id).build();

        when(courseClassRepository.findById(id)).thenReturn(Optional.of(courseClass));
        when(courseClassRepository.save(any(CourseClass.class))).thenReturn(courseClass);
        when(courseMapper.toCourseClassDto(courseClass)).thenReturn(responseDto);

        // when
        ResponseEntity<ApiResponse> response = courseClassService.updateCourseClass(id, requestDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getData().get("Class")).isEqualTo(responseDto);
    }

    @Test
    public void whenDeleteCourseClass_thenReturnOk() {
        // given
        String id = "class1";
        CourseClass courseClass = CourseClass.builder().id(id).build();

        when(courseClassRepository.findById(id)).thenReturn(Optional.of(courseClass));

        // when
        ResponseEntity<ApiResponse> response = courseClassService.deleteCourseClass(id);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(courseClassRepository, times(1)).delete(courseClass);
    }
}
