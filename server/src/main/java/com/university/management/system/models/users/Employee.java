package com.university.management.system.models.users;

import com.university.management.system.models.AbstractEntity;
import com.university.management.system.models.courses.CourseClass;
import com.university.management.system.models.courses.CourseTeachingAssistant;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "employees", uniqueConstraints = {
        @UniqueConstraint(name = "uk_employee_person", columnNames = "person_id"),
        @UniqueConstraint(name = "uk_employee_number", columnNames = "employee_number")
})
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Employee extends AbstractEntity {

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "person_id", nullable = false, unique = true, foreignKey = @ForeignKey(name = "fk_employee_person"))
    private Person person;

    @NotBlank
    @Column(nullable = false, unique = true)
    private String employeeNumber;

    private LocalDate hireDate;

    @Positive
    private BigDecimal salary;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Position position;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EmployeeStatus status;

    @OneToMany(mappedBy = "lecturer", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CourseClass> courseClassesAsLecturer;

    @OneToMany(mappedBy = "teachingAssistant", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CourseTeachingAssistant> courseTeachingAssistantRoles;
}
