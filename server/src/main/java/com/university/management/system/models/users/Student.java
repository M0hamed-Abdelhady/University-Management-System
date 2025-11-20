package com.university.management.system.models.users;

import com.university.management.system.models.AbstractEntity;
import com.university.management.system.models.courses.Enrollment;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "students", uniqueConstraints = {
        @UniqueConstraint(name = "uk_student_person", columnNames = "person_id"),
        @UniqueConstraint(name = "uk_student_number", columnNames = "student_number")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Student extends AbstractEntity {

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_student_person"))
    private Person person;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String studentNumber;

    private String major;

    private Integer academicYear;

    @DecimalMin("0.0")
    @DecimalMax("4.0")
    @Column(precision = 3, scale = 2)
    private BigDecimal gpa;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StudentStatus status;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Enrollment> enrollments;
}
