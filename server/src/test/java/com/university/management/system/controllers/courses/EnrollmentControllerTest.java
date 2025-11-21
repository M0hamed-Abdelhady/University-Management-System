package com.university.management.system.controllers.courses;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.courses.EnrollmentDto;
import com.university.management.system.dtos.courses.EnrollmentRequestDto;
import com.university.management.system.models.courses.EnrollmentStatus;
import com.university.management.system.services.courses.IEnrollmentService;
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

@WebMvcTest(EnrollmentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private IEnrollmentService enrollmentService;

    @MockitoBean
    private com.university.management.system.services.auth.JwtService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void whenCreateEnrollment_thenReturnCreated() throws Exception {
        // given
        EnrollmentRequestDto requestDto = EnrollmentRequestDto.builder()
                .studentId("student1")
                .classId("class1")
                .status(EnrollmentStatus.ENROLLED)
                .build();
        EnrollmentDto responseDto = EnrollmentDto.builder()
                .id("enrollment1")
                .status(EnrollmentStatus.ENROLLED)
                .build();
        ResponseEntity<ApiResponse> responseEntity = ResponseEntityBuilder.create()
                .withStatus(HttpStatus.CREATED)
                .withData("Enrollment", responseDto)
                .build();

        when(enrollmentService.createEnrollment(any(EnrollmentRequestDto.class))).thenReturn(responseEntity);

        // when & then
        mockMvc.perform(post("/api/v1/enrollments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.Enrollment.id").value("enrollment1"));
    }

    @Test
    public void whenGetEnrollmentById_thenReturnEnrollment() throws Exception {
        // given
        String id = "enrollment1";
        EnrollmentDto responseDto = EnrollmentDto.builder()
                .id(id)
                .status(EnrollmentStatus.ENROLLED)
                .build();
        ResponseEntity<ApiResponse> responseEntity = ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withData("Enrollment", responseDto)
                .build();

        when(enrollmentService.getEnrollmentById(anyString())).thenReturn(responseEntity);

        // when & then
        mockMvc.perform(get("/api/v1/enrollments/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.Enrollment.id").value(id));
    }
}
