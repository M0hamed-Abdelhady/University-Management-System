package com.university.management.system.services.courses;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.courses.CourseDto;
import com.university.management.system.dtos.courses.CourseRequestDto;
import com.university.management.system.models.courses.Course;
import com.university.management.system.repositories.courses.CourseRepository;
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
public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseService courseService;

    @Test
    public void whenCreateCourse_thenReturnCreated() {
        // given
        CourseRequestDto requestDto = CourseRequestDto.builder()
                .courseCode("CS101")
                .title("Intro to CS")
                .credits(3)
                .build();

        Course course = Course.builder()
                .courseCode("CS101")
                .title("Intro to CS")
                .credits(3)
                .build();

        CourseDto responseDto = CourseDto.builder()
                .id("1")
                .courseCode("CS101")
                .title("Intro to CS")
                .credits(3)
                .build();

        when(courseRepository.existsByCourseCode(requestDto.getCourseCode())).thenReturn(false);
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(courseMapper.toCourseDto(course)).thenReturn(responseDto);

        // when
        ResponseEntity<ApiResponse> response = courseService.createCourse(requestDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().getData().get("Course")).isEqualTo(responseDto);
    }

    @Test
    public void whenUpdateCourse_thenReturnUpdated() {
        // given
        String id = "1";
        CourseRequestDto requestDto = CourseRequestDto.builder()
                .courseCode("CS101")
                .title("Intro to CS Updated")
                .credits(3)
                .build();

        Course course = Course.builder()
                .id(id)
                .courseCode("CS101")
                .title("Intro to CS")
                .credits(3)
                .build();

        CourseDto responseDto = CourseDto.builder()
                .id(id)
                .courseCode("CS101")
                .title("Intro to CS Updated")
                .credits(3)
                .build();

        when(courseRepository.findById(id)).thenReturn(Optional.of(course));
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(courseMapper.toCourseDto(course)).thenReturn(responseDto);

        // when
        ResponseEntity<ApiResponse> response = courseService.updateCourse(id, requestDto);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getData().get("Course")).isEqualTo(responseDto);
    }

    @Test
    public void whenDeleteCourse_thenReturnOk() {
        // given
        String id = "1";
        Course course = Course.builder()
                .id(id)
                .courseCode("CS101")
                .build();

        when(courseRepository.findById(id)).thenReturn(Optional.of(course));

        // when
        ResponseEntity<ApiResponse> response = courseService.deleteCourse(id);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(courseRepository, times(1)).delete(course);
    }
}
