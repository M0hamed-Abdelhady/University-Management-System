package com.university.management.system.repositories.users;

import com.university.management.system.models.users.Student;
import com.university.management.system.models.users.StudentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

    Optional<Student> findByStudentNumber(String studentNumber);

    boolean existsByStudentNumber(String studentNumber);

    Page<Student> findByStatus(StudentStatus status, Pageable pageable);

    Page<Student> findByMajor(String major, Pageable pageable);

    Page<Student> findByAcademicYear(Integer academicYear, Pageable pageable);

    boolean existsByPersonId(String personId);

    Optional<Student> findByPersonId(String personId);
}
