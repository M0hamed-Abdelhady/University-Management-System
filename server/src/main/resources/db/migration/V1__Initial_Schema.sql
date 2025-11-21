-- V1__Initial_Schema.sql
-- Initial database schema for University Management System

-- Create persons table
CREATE TABLE persons (
    id VARCHAR(36) PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    phone VARCHAR(255),
    date_of_birth DATE,
    address VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT,
    CONSTRAINT uk_person_email UNIQUE (email)
);

-- Create person_roles table
CREATE TABLE person_roles (
    id VARCHAR(36) PRIMARY KEY,
    person_id VARCHAR(36) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT,
    CONSTRAINT fk_person_role_person FOREIGN KEY (person_id) REFERENCES persons(id) ON DELETE CASCADE
);

-- Create employees table
CREATE TABLE employees (
    id VARCHAR(36) PRIMARY KEY,
    person_id VARCHAR(36) NOT NULL UNIQUE,
    employee_number VARCHAR(255) NOT NULL UNIQUE,
    hire_date DATE,
    salary DECIMAL(19, 2),
    position VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT,
    CONSTRAINT uk_employee_person UNIQUE (person_id),
    CONSTRAINT uk_employee_number UNIQUE (employee_number),
    CONSTRAINT fk_employee_person FOREIGN KEY (person_id) REFERENCES persons(id) ON DELETE CASCADE
);

-- Create students table
CREATE TABLE students (
    id VARCHAR(36) PRIMARY KEY,
    person_id VARCHAR(36) NOT NULL UNIQUE,
    student_number VARCHAR(255) NOT NULL UNIQUE,
    major VARCHAR(255),
    academic_year INTEGER,
    gpa DECIMAL(3, 2),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT,
    CONSTRAINT uk_student_person UNIQUE (person_id),
    CONSTRAINT uk_student_number UNIQUE (student_number),
    CONSTRAINT fk_student_person FOREIGN KEY (person_id) REFERENCES persons(id) ON DELETE CASCADE
);

-- Create courses table
CREATE TABLE courses (
    id VARCHAR(36) PRIMARY KEY,
    course_code VARCHAR(255) NOT NULL UNIQUE,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    credits INTEGER,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT,
    CONSTRAINT uk_course_code UNIQUE (course_code)
);

-- Create course_class table
CREATE TABLE course_class (
    id VARCHAR(36) PRIMARY KEY,
    course_id VARCHAR(36) NOT NULL,
    lecturer_id VARCHAR(36) NOT NULL,
    semester VARCHAR(255),
    academic_year INTEGER,
    current_capacity INTEGER,
    max_capacity INTEGER,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT,
    CONSTRAINT fk_course_class_course FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
    CONSTRAINT fk_course_class_lecturer FOREIGN KEY (lecturer_id) REFERENCES employees(id) ON DELETE CASCADE
);

-- Create enrollments table
CREATE TABLE enrollments (
    id VARCHAR(36) PRIMARY KEY,
    student_id VARCHAR(36) NOT NULL,
    class_id VARCHAR(36) NOT NULL,
    grade VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT,
    CONSTRAINT fk_enrollment_student FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    CONSTRAINT fk_enrollment_class FOREIGN KEY (class_id) REFERENCES course_class(id) ON DELETE CASCADE
);

-- Create course_teaching_assistants table
CREATE TABLE course_teaching_assistants (
    id VARCHAR(36) PRIMARY KEY,
    class_id VARCHAR(36) NOT NULL,
    teaching_assistant_id VARCHAR(36) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    version BIGINT,
    CONSTRAINT fk_course_ta_class FOREIGN KEY (class_id) REFERENCES course_class(id) ON DELETE CASCADE,
    CONSTRAINT fk_course_ta_employee FOREIGN KEY (teaching_assistant_id) REFERENCES employees(id) ON DELETE CASCADE
);

-- Create indexes for foreign keys and frequently queried columns
CREATE INDEX idx_person_roles_person_id ON person_roles(person_id);
CREATE INDEX idx_employees_person_id ON employees(person_id);
CREATE INDEX idx_students_person_id ON students(person_id);
CREATE INDEX idx_course_class_course_id ON course_class(course_id);
CREATE INDEX idx_course_class_lecturer_id ON course_class(lecturer_id);
CREATE INDEX idx_enrollments_student_id ON enrollments(student_id);
CREATE INDEX idx_enrollments_class_id ON enrollments(class_id);
CREATE INDEX idx_course_teaching_assistants_class_id ON course_teaching_assistants(class_id);
CREATE INDEX idx_course_teaching_assistants_ta_id ON course_teaching_assistants(teaching_assistant_id);
