package com.university.management.system.services.courses;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.courses.EnrollmentRequestDto;
import org.springframework.http.ResponseEntity;

public interface IEnrollmentService {
    ResponseEntity<ApiResponse> getAllEnrollments(Integer page, Integer size);

    ResponseEntity<ApiResponse> getEnrollmentById(String id);

    ResponseEntity<ApiResponse> createEnrollment(EnrollmentRequestDto enrollmentRequestDto);

    ResponseEntity<ApiResponse> updateEnrollment(String id, EnrollmentRequestDto enrollmentRequestDto);

    ResponseEntity<ApiResponse> deleteEnrollment(String id);

    ResponseEntity<ApiResponse> enrollStudent(String studentId, String classId);

    ResponseEntity<ApiResponse> dropStudent(String studentId, String enrollmentId);

    ResponseEntity<ApiResponse> updateGrade(String id, String grade);
}
