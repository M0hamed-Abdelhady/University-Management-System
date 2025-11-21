package com.university.management.system.controllers.courses;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.courses.CourseDto;
import com.university.management.system.dtos.courses.CourseRequestDto;
import com.university.management.system.services.courses.ICourseService;
import com.university.management.system.utils.ResponseEntityBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ICourseService courseService;

    @MockitoBean
    private com.university.management.system.services.auth.JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenCreateCourse_thenReturnCreated() throws Exception {
        // given
        CourseRequestDto requestDto = CourseRequestDto.builder()
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
        ResponseEntity<ApiResponse> responseEntity = ResponseEntityBuilder.create()
                .withStatus(HttpStatus.CREATED)
                .withData("Course", responseDto)
                .build();

        when(courseService.createCourse(any(CourseRequestDto.class))).thenReturn(responseEntity);

        // when & then
        mockMvc.perform(post("/api/v1/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.Course.courseCode").value("CS101"));
    }

    @Test
    public void whenGetCourseById_thenReturnCourse() throws Exception {
        // given
        String id = "1";
        CourseDto responseDto = CourseDto.builder()
                .id(id)
                .courseCode("CS101")
                .title("Intro to CS")
                .credits(3)
                .build();
        ResponseEntity<ApiResponse> responseEntity = ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withData("Course", responseDto)
                .build();

        when(courseService.getCourseById(anyString())).thenReturn(responseEntity);

        // when & then
        mockMvc.perform(get("/api/v1/courses/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.Course.id").value(id));
    }
}
