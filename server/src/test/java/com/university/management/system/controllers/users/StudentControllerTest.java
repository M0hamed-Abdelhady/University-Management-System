package com.university.management.system.controllers.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.users.PersonDto;
import com.university.management.system.dtos.users.StudentDto;
import com.university.management.system.dtos.users.StudentRequestDto;
import com.university.management.system.dtos.users.StudentUpdateDto;
import com.university.management.system.models.users.StudentStatus;
import com.university.management.system.services.users.StudentService;
import com.university.management.system.utils.ResponseEntityBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StudentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class StudentControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private StudentService studentService;

        @MockitoBean
        private com.university.management.system.services.courses.IEnrollmentService enrollmentService;

        @MockitoBean
        private com.university.management.system.services.auth.JwtService jwtService;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        public void whenCreateStudent_thenReturnCreated() throws Exception {
                // given
                StudentRequestDto requestDto = StudentRequestDto.builder()
                                .email("student@example.com")
                                .password("password")
                                .firstName("Student")
                                .lastName("One")
                                .status(StudentStatus.ACTIVE)
                                .build();
                StudentDto responseDto = StudentDto.builder()
                                .id("1")
                                .person(PersonDto.builder().email("student@example.com").build())
                                .build();
                ResponseEntity<ApiResponse> responseEntity = ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.CREATED)
                                .withData("Student", responseDto)
                                .build();

                when(studentService.createStudent(any(StudentRequestDto.class))).thenReturn(responseEntity);

                // when & then
                mockMvc.perform(post("/api/v1/students")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.data.Student.person.email").value("student@example.com"));
        }

        @Test
        public void whenGetStudentById_thenReturnStudent() throws Exception {
                // given
                String id = "1";
                StudentDto responseDto = StudentDto.builder()
                                .id(id)
                                .person(PersonDto.builder().email("student@example.com").build())
                                .build();
                ResponseEntity<ApiResponse> responseEntity = ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.OK)
                                .withData("Student", responseDto)
                                .build();

                when(studentService.getStudentById(anyString())).thenReturn(responseEntity);

                // when & then
                mockMvc.perform(get("/api/v1/students/{id}", id))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.Student.id").value(id));
        }

        @Test
        public void whenUpdateStudent_thenReturnUpdated() throws Exception {
                // given
                String id = "1";
                StudentUpdateDto updateDto = StudentUpdateDto.builder()
                                .email("updated@example.com")
                                .firstName("Updated")
                                .lastName("Student")
                                .status(StudentStatus.ACTIVE)
                                .build();
                StudentDto responseDto = StudentDto.builder()
                                .id(id)
                                .person(PersonDto.builder().email("updated@example.com").build())
                                .build();
                ResponseEntity<ApiResponse> responseEntity = ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.OK)
                                .withData("Student", responseDto)
                                .build();

                when(studentService.updateStudent(anyString(), any(StudentUpdateDto.class))).thenReturn(responseEntity);

                // when & then
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .put("/api/v1/students/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.Student.person.email").value("updated@example.com"));
        }
}
