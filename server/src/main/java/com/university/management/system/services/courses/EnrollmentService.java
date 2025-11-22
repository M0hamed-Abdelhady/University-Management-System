package com.university.management.system.services.courses;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.courses.EnrollmentDto;
import com.university.management.system.dtos.courses.EnrollmentRequestDto;
import com.university.management.system.exceptions.ResourceNotFoundException;
import com.university.management.system.models.courses.CourseClass;
import com.university.management.system.models.courses.Enrollment;
import com.university.management.system.models.users.Student;
import com.university.management.system.repositories.courses.CourseClassRepository;
import com.university.management.system.repositories.courses.EnrollmentRepository;
import com.university.management.system.repositories.users.StudentRepository;
import com.university.management.system.utils.RepositoryUtils;
import com.university.management.system.utils.ResponseEntityBuilder;
import com.university.management.system.utils.mappers.courses.CourseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService implements IEnrollmentService {

        private final EnrollmentRepository enrollmentRepository;
        private final StudentRepository studentRepository;
        private final CourseClassRepository courseClassRepository;
        private final CourseMapper courseMapper;
        private final RepositoryUtils repositoryUtils;

        @Override
        public ResponseEntity<ApiResponse> getAllEnrollments(Integer page, Integer size) {
                Pageable pageable = repositoryUtils.getPageable(page, size, Sort.Direction.DESC, "createdAt");
                Page<Enrollment> enrollments = enrollmentRepository.findAll(pageable);

                List<EnrollmentDto> response = enrollments.map(courseMapper::toEnrollmentDto).toList();

                return ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.OK)
                                .withData("Enrollments", response)
                                .withData("pagination", java.util.Map.of(
                                                "currentPage", enrollments.getNumber() + 1,
                                                "totalPages", enrollments.getTotalPages(),
                                                "totalItems", enrollments.getTotalElements(),
                                                "pageSize", enrollments.getSize()))
                                .withMessage("Enrollments retrieved successfully")
                                .build();
        }

        @Override
        public ResponseEntity<ApiResponse> getEnrollmentById(String id) {
                Enrollment enrollment = enrollmentRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

                return ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.OK)
                                .withData("Enrollment", courseMapper.toEnrollmentDto(enrollment))
                                .withMessage("Enrollment retrieved successfully")
                                .build();
        }

        @Override
        @Transactional
        public ResponseEntity<ApiResponse> createEnrollment(EnrollmentRequestDto enrollmentRequestDto) {
                Student student = studentRepository.findById(enrollmentRequestDto.getStudentId())
                                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

                CourseClass courseClass = courseClassRepository.findById(enrollmentRequestDto.getClassId())
                                .orElseThrow(() -> new ResourceNotFoundException("Course Class not found"));

                if (courseClass.getCurrentCapacity() >= courseClass.getMaxCapacity()) {
                        return ResponseEntityBuilder.create()
                                        .withStatus(HttpStatus.BAD_REQUEST)
                                        .withMessage("Class is full")
                                        .build();
                }

                Enrollment enrollment = Enrollment.builder()
                                .student(student)
                                .courseClass(courseClass)
                                .grade(enrollmentRequestDto.getGrade())
                                .status(enrollmentRequestDto.getStatus())
                                .build();

                Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

                courseClass.setCurrentCapacity(courseClass.getCurrentCapacity() + 1);
                courseClassRepository.save(courseClass);

                return ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.CREATED)
                                .withData("Enrollment", courseMapper.toEnrollmentDto(savedEnrollment))
                                .withMessage("Enrollment created successfully")
                                .build();
        }

        @Override
        @Transactional
        public ResponseEntity<ApiResponse> updateEnrollment(String id, EnrollmentRequestDto enrollmentRequestDto) {
                Enrollment enrollment = enrollmentRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

                if (!enrollment.getStudent().getId().equals(enrollmentRequestDto.getStudentId())) {
                        Student student = studentRepository.findById(enrollmentRequestDto.getStudentId())
                                        .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
                        enrollment.setStudent(student);
                }

                if (!enrollment.getCourseClass().getId().equals(enrollmentRequestDto.getClassId())) {
                        CourseClass courseClass = courseClassRepository.findById(enrollmentRequestDto.getClassId())
                                        .orElseThrow(() -> new ResourceNotFoundException("Course Class not found"));

                        if (courseClass.getCurrentCapacity() >= courseClass.getMaxCapacity()) {
                                throw new RuntimeException("New Class is full");
                        }
                        CourseClass oldClass = enrollment.getCourseClass();
                        oldClass.setCurrentCapacity(oldClass.getCurrentCapacity() - 1);
                        courseClassRepository.save(oldClass);
                        courseClass.setCurrentCapacity(courseClass.getCurrentCapacity() + 1);
                        courseClassRepository.save(courseClass);
                        enrollment.setCourseClass(courseClass);
                }

                enrollment.setGrade(enrollmentRequestDto.getGrade());
                enrollment.setStatus(enrollmentRequestDto.getStatus());

                Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

                return ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.OK)
                                .withData("Enrollment", courseMapper.toEnrollmentDto(savedEnrollment))
                                .withMessage("Enrollment updated successfully")
                                .build();
        }

        @Override
        @Transactional
        public ResponseEntity<ApiResponse> deleteEnrollment(String id) {
                Enrollment enrollment = enrollmentRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

                CourseClass courseClass = enrollment.getCourseClass();
                courseClass.setCurrentCapacity(courseClass.getCurrentCapacity() - 1);
                courseClassRepository.save(courseClass);

                enrollmentRepository.delete(enrollment);

                return ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.OK)
                                .withMessage("Enrollment deleted successfully")
                                .build();
        }

        @Override
        @Transactional
        public ResponseEntity<ApiResponse> updateGrade(String id, String grade) {
                Enrollment enrollment = enrollmentRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Enrollment not found"));

                grade = grade.toUpperCase();
                enrollment.setGrade(grade);
                Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

                return ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.OK)
                                .withData("Enrollment", courseMapper.toEnrollmentDto(savedEnrollment))
                                .withMessage("Grade updated successfully")
                                .build();
        }
}
