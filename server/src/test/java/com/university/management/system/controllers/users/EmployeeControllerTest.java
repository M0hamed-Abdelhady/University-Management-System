package com.university.management.system.controllers.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.users.EmployeeDto;
import com.university.management.system.dtos.users.EmployeeRequestDto;
import com.university.management.system.dtos.users.EmployeeUpdateDto;
import com.university.management.system.dtos.users.PersonDto;
import com.university.management.system.models.users.EmployeeStatus;
import com.university.management.system.models.users.Position;
import com.university.management.system.services.users.EmployeeService;
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

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class EmployeeControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockitoBean
        private EmployeeService employeeService;

        @MockitoBean
        private com.university.management.system.services.auth.JwtService jwtService;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        public void whenCreateEmployee_thenReturnCreated() throws Exception {
                // given
                EmployeeRequestDto requestDto = EmployeeRequestDto.builder()
                                .email("employee@example.com")
                                .password("password")
                                .firstName("Employee")
                                .lastName("One")
                                .position(Position.LECTURER)
                                .status(EmployeeStatus.ACTIVE)
                                .build();
                EmployeeDto responseDto = EmployeeDto.builder()
                                .id("1")
                                .person(PersonDto.builder().email("employee@example.com").build())
                                .build();
                ResponseEntity<ApiResponse> responseEntity = ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.CREATED)
                                .withData("Employee", responseDto)
                                .build();

                when(employeeService.createEmployee(any(EmployeeRequestDto.class))).thenReturn(responseEntity);

                // when & then
                mockMvc.perform(post("/api/v1/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(requestDto)))
                                .andExpect(status().isCreated())
                                .andExpect(jsonPath("$.data.Employee.person.email").value("employee@example.com"));
        }

        @Test
        public void whenGetEmployeeById_thenReturnEmployee() throws Exception {
                // given
                String id = "1";
                EmployeeDto responseDto = EmployeeDto.builder()
                                .id(id)
                                .person(PersonDto.builder().email("employee@example.com").build())
                                .build();
                ResponseEntity<ApiResponse> responseEntity = ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.OK)
                                .withData("Employee", responseDto)
                                .build();

                when(employeeService.getEmployeeById(anyString())).thenReturn(responseEntity);

                // when & then
                mockMvc.perform(get("/api/v1/employees/{id}", id))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.Employee.id").value(id));
        }

        @Test
        public void whenUpdateEmployee_thenReturnUpdated() throws Exception {
                // given
                String id = "1";
                EmployeeUpdateDto updateDto = EmployeeUpdateDto.builder()
                                .email("updated@example.com")
                                .firstName("Updated")
                                .lastName("Employee")
                                .position(Position.LECTURER)
                                .status(EmployeeStatus.ACTIVE)
                                .build();
                EmployeeDto responseDto = EmployeeDto.builder()
                                .id(id)
                                .person(PersonDto.builder().email("updated@example.com").build())
                                .build();
                ResponseEntity<ApiResponse> responseEntity = ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.OK)
                                .withData("Employee", responseDto)
                                .build();

                when(employeeService.updateEmployee(anyString(), any(EmployeeUpdateDto.class)))
                                .thenReturn(responseEntity);

                // when & then
                mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                                .put("/api/v1/employees/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(updateDto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.data.Employee.person.email").value("updated@example.com"));
        }
}
