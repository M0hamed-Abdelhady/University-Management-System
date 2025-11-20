package com.university.management.system.services.courses;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.courses.CourseRequestDto;
import org.springframework.http.ResponseEntity;

public interface ICourseService {
    ResponseEntity<ApiResponse> getAllCourses(Integer page, Integer size);

    ResponseEntity<ApiResponse> getCourseById(String id);

    ResponseEntity<ApiResponse> createCourse(CourseRequestDto courseRequestDto);

    ResponseEntity<ApiResponse> updateCourse(String id, CourseRequestDto courseRequestDto);

    ResponseEntity<ApiResponse> deleteCourse(String id);
}
