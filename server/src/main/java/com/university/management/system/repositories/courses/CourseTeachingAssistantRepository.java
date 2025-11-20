package com.university.management.system.repositories.courses;

import com.university.management.system.models.courses.CourseClass;
import com.university.management.system.models.courses.CourseTeachingAssistant;
import com.university.management.system.models.users.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseTeachingAssistantRepository extends JpaRepository<CourseTeachingAssistant, String> {

    List<CourseTeachingAssistant> findByCourseClass(CourseClass courseClass);

    Page<CourseTeachingAssistant> findByTeachingAssistant(Employee teachingAssistant, Pageable pageable);

    Optional<CourseTeachingAssistant> findByCourseClassIdAndTeachingAssistantId(String courseClassId, String teachingAssistantId);
}
