package com.university.management.system.controllers.courses;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.courses.EnrollmentRequestDto;
import com.university.management.system.services.courses.IEnrollmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.university.management.system.utils.Constants.API_VERSION;

@RestController
@RequestMapping(API_VERSION + "/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final IEnrollmentService enrollmentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse> getAllEnrollments(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return enrollmentService.getAllEnrollments(page, size);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getEnrollmentById(@PathVariable String id) {
        return enrollmentService.getEnrollmentById(id);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse> createEnrollment(@Valid @RequestBody EnrollmentRequestDto requestDto) {
        return enrollmentService.createEnrollment(requestDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse> updateEnrollment(@PathVariable String id,
                                                        @Valid @RequestBody EnrollmentRequestDto requestDto) {
        return enrollmentService.updateEnrollment(id, requestDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse> deleteEnrollment(@PathVariable String id) {
        return enrollmentService.deleteEnrollment(id);
    }

    @PutMapping("/{id}/grade")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<ApiResponse> updateGrade(@PathVariable String id, @RequestParam String grade) {
        return enrollmentService.updateGrade(id, grade);
    }
}
