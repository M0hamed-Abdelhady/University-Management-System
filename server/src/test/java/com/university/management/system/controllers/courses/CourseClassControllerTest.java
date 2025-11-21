package com.university.management.system.controllers.courses;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.courses.CourseClassDto;
import com.university.management.system.dtos.courses.CourseClassRequestDto;
import com.university.management.system.models.courses.CourseClassStatus;
import com.university.management.system.services.courses.ICourseClassService;
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

@WebMvcTest(CourseClassController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CourseClassControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ICourseClassService courseClassService;

    @MockitoBean
    private com.university.management.system.services.auth.JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenCreateCourseClass_thenReturnCreated() throws Exception {
        // given
        CourseClassRequestDto requestDto = CourseClassRequestDto.builder()
                .courseId("course1")
                .lecturerId("emp1")
                .semester("Fall")
                .academicYear(2024)
                .maxCapacity(30)
                .status(CourseClassStatus.ACTIVE)
                .build();
        CourseClassDto responseDto = CourseClassDto.builder()
                .id("class1")
                .semester("Fall")
                .academicYear(2024)
                .build();
        ResponseEntity<ApiResponse> responseEntity = ResponseEntityBuilder.create()
                .withStatus(HttpStatus.CREATED)
                .withData("Class", responseDto)
                .build();

        when(courseClassService.createCourseClass(any(CourseClassRequestDto.class))).thenReturn(responseEntity);

        // when & then
        mockMvc.perform(post("/api/v1/classes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.Class.id").value("class1"));
    }

    @Test
    public void whenGetCourseClassById_thenReturnCourseClass() throws Exception {
        // given
        String id = "class1";
        CourseClassDto responseDto = CourseClassDto.builder()
                .id(id)
                .semester("Fall")
                .academicYear(2024)
                .build();
        ResponseEntity<ApiResponse> responseEntity = ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withData("Class", responseDto)
                .build();

        when(courseClassService.getCourseClassById(anyString())).thenReturn(responseEntity);

        // when & then
        mockMvc.perform(get("/api/v1/classes/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.Class.id").value(id));
    }
}
