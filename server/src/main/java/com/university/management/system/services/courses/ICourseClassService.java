package com.university.management.system.services.courses;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.courses.CourseClassRequestDto;
import org.springframework.http.ResponseEntity;

public interface ICourseClassService {
    ResponseEntity<ApiResponse> getAllCourseClasses(Integer page, Integer size);

    ResponseEntity<ApiResponse> getCourseClassById(String id);

    ResponseEntity<ApiResponse> createCourseClass(CourseClassRequestDto courseClassRequestDto);

    ResponseEntity<ApiResponse> updateCourseClass(String id, CourseClassRequestDto courseClassRequestDto);

    ResponseEntity<ApiResponse> deleteCourseClass(String id);

    ResponseEntity<ApiResponse> addTeachingAssistant(String classId, String employeeId);

    ResponseEntity<ApiResponse> removeTeachingAssistant(String classId, String taId);
}
