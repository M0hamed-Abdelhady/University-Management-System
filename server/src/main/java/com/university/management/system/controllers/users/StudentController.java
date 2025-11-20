package com.university.management.system.controllers.users;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.users.StudentRequestDto;
import com.university.management.system.services.users.IStudentService;
import com.university.management.system.services.courses.IEnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final IStudentService studentService;
    private final IEnrollmentService enrollmentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse> getAllStudents(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {
        return studentService.getAllStudents(page, size);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'STUDENT')")
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
            @Valid @RequestBody StudentRequestDto studentRequestDto) {
        return studentService.updateStudent(id, studentRequestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> deleteStudent(@PathVariable String id) {
        return studentService.deleteStudent(id);
    }

    @PostMapping("/{studentId}/enroll")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'STUDENT')")
    public ResponseEntity<ApiResponse> enrollStudent(@PathVariable String studentId, @RequestParam String classId) {
        return enrollmentService.enrollStudent(studentId, classId);
    }

    @PostMapping("/{studentId}/drop/{enrollmentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE', 'STUDENT')")
    public ResponseEntity<ApiResponse> dropStudent(@PathVariable String studentId, @PathVariable String enrollmentId) {
        return enrollmentService.dropStudent(studentId, enrollmentId);
    }
}
