package com.university.management.system.services.courses;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.courses.EnrollmentDto;
import com.university.management.system.dtos.courses.EnrollmentRequestDto;
import com.university.management.system.models.courses.CourseClass;
import com.university.management.system.models.courses.Enrollment;
import com.university.management.system.models.courses.EnrollmentStatus;
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
                .withData("TotalPages", enrollments.getTotalPages())
                .withData("TotalElements", enrollments.getTotalElements())
                .withMessage("Enrollments retrieved successfully")
                .build();
    }

    @Override
    public ResponseEntity<ApiResponse> getEnrollmentById(String id) {
        Enrollment enrollment = enrollmentRepository.findById(id).orElse(null);

        if (enrollment == null) {
            return ResponseEntityBuilder.create()
                    .withStatus(HttpStatus.NOT_FOUND)
                    .withMessage("Enrollment not found")
                    .build();
        }

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withData("Enrollment", courseMapper.toEnrollmentDto(enrollment))
                .withMessage("Enrollment retrieved successfully")
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> createEnrollment(EnrollmentRequestDto requestDto) {
        Student student = studentRepository.findById(requestDto.getStudentId()).orElse(null);

        if (student == null) {
            return ResponseEntityBuilder.create()
                    .withStatus(HttpStatus.NOT_FOUND)
                    .withMessage("Student not found")
                    .build();
        }

        CourseClass courseClass = courseClassRepository.findById(requestDto.getClassId()).orElse(null);

        if (courseClass == null) {
            return ResponseEntityBuilder.create()
                    .withStatus(HttpStatus.NOT_FOUND)
                    .withMessage("Course Class not found")
                    .build();
        }

        if (courseClass.getCurrentCapacity() >= courseClass.getMaxCapacity()) {
            return ResponseEntityBuilder.create()
                    .withStatus(HttpStatus.BAD_REQUEST)
                    .withMessage("Class is full")
                    .build();
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .courseClass(courseClass)
                .grade(requestDto.getGrade())
                .status(requestDto.getStatus())
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
    public ResponseEntity<ApiResponse> updateEnrollment(String id, EnrollmentRequestDto requestDto) {
        Enrollment enrollment = enrollmentRepository.findById(id).orElse(null);

        if (enrollment == null) {
            return ResponseEntityBuilder.create()
                    .withStatus(HttpStatus.NOT_FOUND)
                    .withMessage("Enrollment not found")
                    .build();
        }

        if (!enrollment.getStudent().getId().equals(requestDto.getStudentId())) {
            Student student = studentRepository.findById(requestDto.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));
            enrollment.setStudent(student);
        }

        if (!enrollment.getCourseClass().getId().equals(requestDto.getClassId())) {
            CourseClass courseClass = courseClassRepository.findById(requestDto.getClassId())
                    .orElseThrow(() -> new RuntimeException("Course Class not found"));

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

        enrollment.setGrade(requestDto.getGrade());
        enrollment.setStatus(requestDto.getStatus());

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
        Enrollment enrollment = enrollmentRepository.findById(id).orElse(null);

        if (enrollment == null) {
            return ResponseEntityBuilder.create()
                    .withStatus(HttpStatus.NOT_FOUND)
                    .withMessage("Enrollment not found")
                    .build();
        }

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
    public ResponseEntity<ApiResponse> enrollStudent(String studentId, String classId) {
        if (!studentRepository.existsById(studentId)) {
            return ResponseEntityBuilder.create()
                    .withStatus(HttpStatus.NOT_FOUND)
                    .withMessage("Student not found")
                    .build();
        }
        if (!courseClassRepository.existsById(classId)) {
            return ResponseEntityBuilder.create()
                    .withStatus(HttpStatus.NOT_FOUND)
                    .withMessage("Course Class not found")
                    .build();
        }

        EnrollmentRequestDto requestDto = EnrollmentRequestDto.builder()
                .studentId(studentId)
                .classId(classId)
                .status(EnrollmentStatus.ENROLLED)
                .build();
        return createEnrollment(requestDto);
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> dropStudent(String studentId, String enrollmentId) {
        if (!studentRepository.existsById(studentId)) {
            return ResponseEntityBuilder.create()
                    .withStatus(HttpStatus.NOT_FOUND)
                    .withMessage("Student not found")
                    .build();
        }

        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElse(null);

        if (enrollment == null) {
            return ResponseEntityBuilder.create()
                    .withStatus(HttpStatus.NOT_FOUND)
                    .withMessage("Enrollment not found")
                    .build();
        }

        if (!enrollment.getStudent().getId().equals(studentId)) {
            return ResponseEntityBuilder.create()
                    .withStatus(HttpStatus.BAD_REQUEST)
                    .withMessage("Enrollment does not belong to the specified student")
                    .build();
        }

        return deleteEnrollment(enrollmentId);
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> updateGrade(String id, String grade) {
        Enrollment enrollment = enrollmentRepository.findById(id).orElse(null);

        if (enrollment == null) {
            return ResponseEntityBuilder.create()
                    .withStatus(HttpStatus.NOT_FOUND)
                    .withMessage("Enrollment not found")
                    .build();
        }

        enrollment.setGrade(grade);
        Enrollment savedEnrollment = enrollmentRepository.save(enrollment);

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withData("Enrollment", courseMapper.toEnrollmentDto(savedEnrollment))
                .withMessage("Grade updated successfully")
                .build();
    }
}
