package com.university.management.system.services.courses;

import com.university.management.system.exceptions.ResourceNotFoundException;

import com.university.management.system.dtos.ApiResponse;
import com.university.management.system.dtos.courses.CourseDto;
import com.university.management.system.dtos.courses.CourseRequestDto;
import com.university.management.system.models.courses.Course;
import com.university.management.system.repositories.courses.CourseRepository;
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
public class CourseService implements ICourseService {

        private final CourseRepository courseRepository;
        private final CourseMapper courseMapper;
        private final RepositoryUtils repositoryUtils;

        @Override
        public ResponseEntity<ApiResponse> getAllCourses(Integer page, Integer size) {
                Pageable pageable = repositoryUtils.getPageable(page, size, Sort.Direction.ASC, "courseCode");
                Page<Course> courses = courseRepository.findAll(pageable);

                List<CourseDto> response = courses.map(courseMapper::toCourseDto).toList();

                return ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.OK)
                                .withData("Courses", response)
                                .withData("TotalPages", courses.getTotalPages())
                                .withData("TotalElements", courses.getTotalElements())
                                .withMessage("Courses retrieved successfully")
                                .build();
        }

        @Override
        public ResponseEntity<ApiResponse> getCourseById(String id) {
                Course course = courseRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

                return ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.OK)
                                .withData("Course", courseMapper.toCourseDto(course))
                                .withMessage("Course retrieved successfully")
                                .build();
        }

        @Override
        @Transactional
        public ResponseEntity<ApiResponse> createCourse(CourseRequestDto courseRequestDto) {
                if (courseRepository.existsByCourseCode(courseRequestDto.getCourseCode())) {
                        throw new RuntimeException(
                                        "Course with code " + courseRequestDto.getCourseCode() + " already exists");
                }

                Course course = Course.builder()
                                .courseCode(courseRequestDto.getCourseCode())
                                .title(courseRequestDto.getTitle())
                                .description(courseRequestDto.getDescription())
                                .credits(courseRequestDto.getCredits())
                                .build();

                Course savedCourse = courseRepository.save(course);

                return ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.CREATED)
                                .withData("Course", courseMapper.toCourseDto(savedCourse))
                                .withMessage("Course created successfully")
                                .build();
        }

        @Override
        @Transactional
        public ResponseEntity<ApiResponse> updateCourse(String id, CourseRequestDto courseRequestDto) {
                Course course = courseRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

                if (!course.getCourseCode().equals(courseRequestDto.getCourseCode()) &&
                                courseRepository.existsByCourseCode(courseRequestDto.getCourseCode())) {
                        throw new RuntimeException(
                                        "Course with code " + courseRequestDto.getCourseCode() + " already exists");
                }

                course.setCourseCode(courseRequestDto.getCourseCode());
                course.setTitle(courseRequestDto.getTitle());
                course.setDescription(courseRequestDto.getDescription());
                course.setCredits(courseRequestDto.getCredits());

                Course savedCourse = courseRepository.save(course);

                return ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.OK)
                                .withData("Course", courseMapper.toCourseDto(savedCourse))
                                .withMessage("Course updated successfully")
                                .build();
        }

        @Override
        @Transactional
        public ResponseEntity<ApiResponse> deleteCourse(String id) {
                Course course = courseRepository.findById(id)
                                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

                courseRepository.delete(course);

                return ResponseEntityBuilder.create()
                                .withStatus(HttpStatus.OK)
                                .withMessage("Course deleted successfully")
                                .build();
        }
}
