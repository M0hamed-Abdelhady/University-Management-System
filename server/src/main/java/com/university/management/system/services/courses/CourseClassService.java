package com.university.management.system.services.courses;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.courses.CourseClassDto;
import com.university.management.system.dtos.courses.CourseClassRequestDto;
import com.university.management.system.models.courses.Course;
import com.university.management.system.models.courses.CourseClass;
import com.university.management.system.models.courses.CourseTeachingAssistant;
import com.university.management.system.models.users.Employee;
import com.university.management.system.repositories.courses.CourseClassRepository;
import com.university.management.system.repositories.courses.CourseRepository;
import com.university.management.system.repositories.courses.CourseTeachingAssistantRepository;
import com.university.management.system.repositories.users.EmployeeRepository;
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
public class CourseClassService implements ICourseClassService {

    private final CourseClassRepository courseClassRepository;
    private final CourseRepository courseRepository;
    private final EmployeeRepository employeeRepository;
    private final CourseTeachingAssistantRepository courseTeachingAssistantRepository;
    private final CourseMapper courseMapper;
    private final RepositoryUtils repositoryUtils;

    @Override
    public ResponseEntity<ApiResponse> getAllCourseClasses(Integer page, Integer size) {
        Pageable pageable = repositoryUtils.getPageable(page, size, Sort.Direction.DESC, "createdAt");
        Page<CourseClass> classes = courseClassRepository.findAll(pageable);

        List<CourseClassDto> response = classes.map(courseMapper::toCourseClassDto).toList();

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withData("Classes", response)
                .withData("TotalPages", classes.getTotalPages())
                .withData("TotalElements", classes.getTotalElements())
                .withMessage("Course classes retrieved successfully")
                .build();
    }

    @Override
    public ResponseEntity<ApiResponse> getCourseClassById(String id) {
        CourseClass courseClass = courseClassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course Class not found with id: " + id));

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withData("Class", courseMapper.toCourseClassDto(courseClass))
                .withMessage("Course class retrieved successfully")
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> createCourseClass(CourseClassRequestDto requestDto) {
        Course course = courseRepository.findById(requestDto.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Employee lecturer = employeeRepository.findById(requestDto.getLecturerId())
                .orElseThrow(() -> new RuntimeException("Lecturer (Employee) not found"));

        CourseClass courseClass = CourseClass.builder()
                .course(course)
                .lecturer(lecturer)
                .semester(requestDto.getSemester())
                .academicYear(requestDto.getAcademicYear())
                .maxCapacity(requestDto.getMaxCapacity())
                .currentCapacity(0)
                .status(requestDto.getStatus())
                .build();

        CourseClass savedClass = courseClassRepository.save(courseClass);

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.CREATED)
                .withData("Class", courseMapper.toCourseClassDto(savedClass))
                .withMessage("Course class created successfully")
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> updateCourseClass(String id, CourseClassRequestDto requestDto) {
        CourseClass courseClass = courseClassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course Class not found with id: " + id));

        if (!courseClass.getCourse().getId().equals(requestDto.getCourseId())) {
            Course course = courseRepository.findById(requestDto.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            courseClass.setCourse(course);
        }

        if (!courseClass.getLecturer().getId().equals(requestDto.getLecturerId())) {
            Employee lecturer = employeeRepository.findById(requestDto.getLecturerId())
                    .orElseThrow(() -> new RuntimeException("Lecturer (Employee) not found"));
            courseClass.setLecturer(lecturer);
        }

        courseClass.setSemester(requestDto.getSemester());
        courseClass.setAcademicYear(requestDto.getAcademicYear());
        courseClass.setMaxCapacity(requestDto.getMaxCapacity());
        courseClass.setStatus(requestDto.getStatus());

        CourseClass savedClass = courseClassRepository.save(courseClass);

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withData("Class", courseMapper.toCourseClassDto(savedClass))
                .withMessage("Course class updated successfully")
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> deleteCourseClass(String id) {
        CourseClass courseClass = courseClassRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course Class not found with id: " + id));

        courseClassRepository.delete(courseClass);

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withMessage("Course class deleted successfully")
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> addTeachingAssistant(String classId, String employeeId) {
        CourseClass courseClass = courseClassRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Course Class not found"));

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        CourseTeachingAssistant cta = CourseTeachingAssistant.builder()
                .courseClass(courseClass)
                .teachingAssistant(employee)
                .build();

        courseTeachingAssistantRepository.save(cta);

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.CREATED)
                .withMessage("Teaching Assistant added to class successfully")
                .build();
    }

    @Override
    @Transactional
    public ResponseEntity<ApiResponse> removeTeachingAssistant(String classId, String taId) {
        // Here taId is the ID of the CourseTeachingAssistant entity, or the Employee
        // ID?
        // The API says /tas/{taId}. It's ambiguous. Usually it's the ID of the resource
        // being deleted.
        // If it's the link, it's the CourseTeachingAssistant ID.
        // But if the user passes the Employee ID, we need to find the link.
        // Let's assume it's the CourseTeachingAssistant ID for now as it's a DELETE on
        // a sub-resource.
        // Actually, typically in such APIs, if I want to remove a TA from a class, I
        // might provide the TA's ID (Employee ID).
        // Let's try to find by class and employee.

        // Wait, if the path is /api/classes/{id}/tas/{taId}, taId could be the employee
        // ID.
        // Let's assume taId is the Employee ID for user friendliness, but I need to
        // find the relation.
        // But `CourseTeachingAssistantRepository` doesn't have a method to find by
        // Class and Employee.
        // I should add it or just use the ID of the CourseTeachingAssistant if the
        // frontend knows it.
        // Let's assume taId is the Employee ID.

        CourseClass courseClass = courseClassRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Course Class not found"));

        Employee employee = employeeRepository.findById(taId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // I need a way to find the relation.
        // I will add a method to CourseTeachingAssistantRepository.
        // For now, I'll fetch all TAs for the class and filter. (Not efficient but
        // works for small numbers)
        // Or better, add the method to repository.

        List<CourseTeachingAssistant> tas = courseTeachingAssistantRepository.findByCourseClass(courseClass);
        CourseTeachingAssistant target = tas.stream()
                .filter(cta -> cta.getTeachingAssistant().getId().equals(taId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Teaching Assistant is not assigned to this class"));

        courseTeachingAssistantRepository.delete(target);

        return ResponseEntityBuilder.create()
                .withStatus(HttpStatus.OK)
                .withMessage("Teaching Assistant removed from class successfully")
                .build();
    }
}
