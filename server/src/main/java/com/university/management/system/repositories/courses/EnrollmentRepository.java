package com.university.management.system.repositories.courses;

import com.university.management.system.models.courses.Enrollment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, String> {

    Page<Enrollment> findAllByStudentId(String studentId, Pageable pageable);

    List<Enrollment> findAllByStudentId(String studentId);

    boolean existsByStudentIdAndCourseClassId(String id, String classId);
}
