package com.university.management.system.services.courses;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.courses.EnrollmentDto;
import com.university.management.system.dtos.courses.EnrollmentRequestDto;
import com.university.management.system.models.courses.CourseClass;
import com.university.management.system.models.courses.Enrollment;
import com.university.management.system.models.courses.EnrollmentStatus;
import com.university.management.system.models.users.Student;
import com.university.management.system.repositories.courses.CourseClassRepository;
import com.university.management.system.repositories.courses.EnrollmentRepository;
import com.university.management.system.repositories.users.StudentRepository;
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
public class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseClassRepository courseClassRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private EnrollmentService enrollmentService;

    @Test
    public void whenCreateEnrollment_thenReturnCreated() {
        // given
        EnrollmentRequestDto requestDto = EnrollmentRequestDto.builder()
                .studentId("student1")
                .classId("class1")
                .status(EnrollmentStatus.ENROLLED)
                .build();

        Student student = Student.builder().id("student1").build();
        CourseClass courseClass = CourseClass.builder()
                .id("class1")
                .maxCapacity(30)
                .currentCapacity(0)
                .build();
        Enrollment enrollment = Enrollment.builder()
                .id("enrollment1")
                .student(student)
                .courseClass(courseClass)
                .build();
        EnrollmentDto responseDto = EnrollmentDto.builder().id("enrollment1").build();

        when(studentRepository.findById("student1")).thenReturn(Optional.of(student));
        when(courseClassRepository.findById("class1")).thenReturn(Optional.of(courseClass));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);
        when(courseMapper.toEnrollmentDto(enrollment)).thenReturn(responseDto);

        // when
        ResponseEntity<ApiResponse> response = enrollmentService.createEnrollment(requestDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getData().get("Enrollment")).isEqualTo(responseDto);
    }

    @Test
    public void whenDeleteEnrollment_thenReturnOk() {
        // given
        String id = "enrollment1";
        CourseClass courseClass = CourseClass.builder()
                .id("class1")
                .currentCapacity(1)
                .build();
        Enrollment enrollment = Enrollment.builder()
                .id(id)
                .courseClass(courseClass)
                .build();

        when(enrollmentRepository.findById(id)).thenReturn(Optional.of(enrollment));

        // when
        ResponseEntity<ApiResponse> response = enrollmentService.deleteEnrollment(id);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(enrollmentRepository, times(1)).delete(enrollment);
        verify(courseClassRepository, times(1)).save(courseClass);
    }
}
