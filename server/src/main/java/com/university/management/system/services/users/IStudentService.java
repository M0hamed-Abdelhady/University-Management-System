package com.university.management.system.services.users;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.users.StudentRequestDto;
import com.university.management.system.dtos.users.StudentUpdateDto;
import org.springframework.http.ResponseEntity;

public interface IStudentService {
    ResponseEntity<ApiResponse> getAllStudents(Integer page, Integer size);

    ResponseEntity<ApiResponse> getStudentById(String id);

    ResponseEntity<ApiResponse> createStudent(StudentRequestDto studentRequestDto);

    ResponseEntity<ApiResponse> updateStudent(String id, StudentUpdateDto studentUpdateDto);

    ResponseEntity<ApiResponse> deleteStudent(String id);

    ResponseEntity<ApiResponse> getStudentEnrollments(Integer page, Integer size);

    ResponseEntity<ApiResponse> enrollStudent(String classId);

    ResponseEntity<ApiResponse> dropStudent(String enrollmentId);

    ResponseEntity<ApiResponse> getStudentClasses(Integer page, Integer size);

    ResponseEntity<ApiResponse> updateGPA(String id);

    ResponseEntity<ApiResponse> getProfile();
}
