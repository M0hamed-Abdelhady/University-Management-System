package com.university.management.system.controllers.courses;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.courses.CourseClassRequestDto;
import com.university.management.system.services.courses.ICourseClassService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.university.management.system.utils.Constants.API_VERSION;

@RestController
@RequestMapping(API_VERSION + "/classes")
@RequiredArgsConstructor
public class CourseClassController {

    private final ICourseClassService courseClassService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllCourseClasses(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return courseClassService.getAllCourseClasses(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getCourseClassById(@PathVariable String id) {
        return courseClassService.getCourseClassById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createCourseClass(@Valid @RequestBody CourseClassRequestDto requestDto) {
        return courseClassService.createCourseClass(requestDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateCourseClass(@PathVariable String id,
                                                         @Valid @RequestBody CourseClassRequestDto requestDto) {
        return courseClassService.updateCourseClass(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteCourseClass(@PathVariable String id) {
        return courseClassService.deleteCourseClass(id);
    }

    @PostMapping("/{id}/tas")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse> addTeachingAssistant(@PathVariable String id, @RequestParam String employeeId) {
        return courseClassService.addTeachingAssistant(id, employeeId);
    }

    @DeleteMapping("/{id}/tas/{taId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse> removeTeachingAssistant(@PathVariable String id, @PathVariable String taId) {
        return courseClassService.removeTeachingAssistant(id, taId);
    }
}
