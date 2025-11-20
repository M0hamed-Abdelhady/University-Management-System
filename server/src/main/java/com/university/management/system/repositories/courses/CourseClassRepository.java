package com.university.management.system.repositories.courses;

import com.university.management.system.models.courses.Course;
import com.university.management.system.models.courses.CourseClass;
import com.university.management.system.models.courses.CourseClassStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseClassRepository extends JpaRepository<CourseClass, String> {

    Page<CourseClass> findByCourse(Course course, Pageable pageable);

    Page<CourseClass> findByStatus(CourseClassStatus status, Pageable pageable);

    Page<CourseClass> findBySemester(String semester, Pageable pageable);

    Page<CourseClass> findByAcademicYear(Integer academicYear, Pageable pageable);

    Optional<CourseClass> findByCourseAndSemesterAndAcademicYear(Course course, String semester, Integer academicYear);
}
