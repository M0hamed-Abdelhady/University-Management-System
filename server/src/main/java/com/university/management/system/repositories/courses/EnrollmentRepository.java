package com.university.management.system.repositories.courses;

import com.university.management.system.models.courses.CourseClass;
import com.university.management.system.models.courses.Enrollment;
import com.university.management.system.models.courses.EnrollmentStatus;
import com.university.management.system.models.users.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {

    List<Enrollment> findByStudent(Student student);

    Page<Enrollment> findByCourseClass(CourseClass courseClass, Pageable pageable);

    Page<Enrollment> findByStatus(EnrollmentStatus status, Pageable pageable);

    Optional<Enrollment> findByStudentAndCourseClass(Student student, CourseClass courseClass);

    List<Enrollment> findByStudentAndStatus(Student student, EnrollmentStatus status);

    long countByCourseClass(CourseClass courseClass);
}
