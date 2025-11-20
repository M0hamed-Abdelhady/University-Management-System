package com.university.management.system.models.courses;

import com.university.management.system.models.AbstractEntity;
import com.university.management.system.models.users.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Table(name = "course_class")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CourseClass extends AbstractEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false, foreignKey = @ForeignKey(name = "fk_course_class_course"))
    private Course course;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecturer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_course_class_lecturer"))
    private Employee lecturer;

    private String semester;

    private Integer academicYear;

    @PositiveOrZero
    private Integer currentCapacity;

    @Positive
    private Integer maxCapacity;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseClassStatus status;

    @OneToMany(mappedBy = "courseClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Enrollment> enrollments;

    @OneToMany(mappedBy = "courseClass", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CourseTeachingAssistant> courseTeachingAssistants;
}
