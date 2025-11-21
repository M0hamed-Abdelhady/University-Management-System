package com.university.management.system.utils;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GradesUtils {
    public static final List<String> GRADES = List.of("A+", "A", "B+", "B", "C+", "C", "D+", "D", "F");

    public static double convertGradeToPoint(String grade) {
        return switch (grade.toUpperCase()) {
            case "A+" -> 4.0;
            case "A" -> 3.7;
            case "B+" -> 3.3;
            case "B" -> 3.0;
            case "C+" -> 2.3;
            case "C" -> 2.0;
            case "D+" -> 1.3;
            case "D" -> 1.0;
            case "F" -> 0.0;
            default -> -1.0;
        };
    }

    public static Boolean isValidGrade(String grade) {
        grade = grade.toUpperCase();
        return GRADES.contains(grade);
    }
}
