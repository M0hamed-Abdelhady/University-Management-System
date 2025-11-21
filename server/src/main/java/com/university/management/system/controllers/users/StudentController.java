package com.university.management.system.controllers.users;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.users.StudentRequestDto;
import com.university.management.system.dtos.users.StudentUpdateDto;
import com.university.management.system.services.users.IStudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.university.management.system.utils.Constants.API_VERSION;

@RestController
@RequestMapping(API_VERSION + "/students")
@RequiredArgsConstructor
public class StudentController {

    private final IStudentService studentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse> getAllStudents(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        return studentService.getAllStudents(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getStudentById(@PathVariable String id) {
        return studentService.getStudentById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createStudent(@Valid @RequestBody StudentRequestDto studentRequestDto) {
        return studentService.createStudent(studentRequestDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> updateStudent(
            @PathVariable String id,
            @Valid @RequestBody StudentUpdateDto studentUpdateDto) {
        return studentService.updateStudent(id, studentUpdateDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable String id) {
        return studentService.deleteStudent(id);
    }

    @GetMapping("/enrollments")
    public ResponseEntity<ApiResponse> getStudentEnrollments(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        return studentService.getStudentEnrollments(page, size);
    }

    @PostMapping("/enroll")
    public ResponseEntity<ApiResponse> enrollStudent(@RequestParam String classId) {
        return studentService.enrollStudent(classId);
    }

    @PostMapping("/drop/{enrollmentId}")
    public ResponseEntity<ApiResponse> dropStudent(@PathVariable String enrollmentId) {
        return studentService.dropStudent(enrollmentId);
    }

    @GetMapping("/classes")
    public ResponseEntity<ApiResponse> getStudentClasses(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        return studentService.getStudentClasses(page, size);
    }

    @PutMapping("/{id}/gpa")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse> updateGPA(@PathVariable String id) {
        return studentService.updateGPA(id);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse> getProfile() {
        return studentService.getProfile();
    }
}
