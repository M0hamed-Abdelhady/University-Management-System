package com.university.management.system.models.courses;

import com.university.management.system.models.AbstractEntity;
import com.university.management.system.models.users.Employee;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "course_teaching_assistants")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class CourseTeachingAssistant extends AbstractEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id", nullable = false, foreignKey = @ForeignKey(name = "fk_course_ta_class"))
    private CourseClass courseClass;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teaching_assistant_id", nullable = false, foreignKey = @ForeignKey(name = "fk_course_ta_employee"))
    private Employee teachingAssistant;
}
