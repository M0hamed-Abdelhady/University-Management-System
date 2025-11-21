// API Response Types
export interface ApiResponse<T> {
    timestamp: string;
    status: number;
    message: string | null;
    error: string | null;
    path: string;
    data: T;
}

export interface PaginatedResponse<T> {
    items: T[];
    pagination: {
        currentPage: number;
        totalPages: number;
        totalItems: number;
        pageSize: number;
    };
}

// User and Authentication Types
export interface User {
    id: string;
    firstName: string;
    lastName: string;
    email: string;
    roles: Role[];
    token?: string;
}

export interface Person {
    id: string;
    firstName: string;
    lastName: string;
    email: string;
    phone?: string;
    dateOfBirth?: string;
    address?: string;
    role: Role;
}

export interface Profile {
    id: string;
    firstName: string;
    lastName: string;
    email: string;
    phone?: string;
    address?: string;
    dateOfBirth?: string;
    roles: Role[];
}

export interface UpdateProfileRequest {
    firstName: string;
    lastName: string;
    phone?: string;
    dateOfBirth?: string;
    address?: string;
}

// Student Types
export interface Student {
    id: string;
    person: Person;
    studentNumber: string;
    major?: string;
    academicYear?: number;
    gpa?: number;
    status: StudentStatus;
}

export interface StudentFormData {
    firstName: string;
    lastName: string;
    email: string;
    password?: string;
    phone?: string;
    dateOfBirth?: string;
    address?: string;
    major?: string;
    academicYear?: number;
    gpa?: number;
    status: StudentStatus;
}

// Employee Types
export interface Employee {
    id: string;
    person: Person;
    employeeId: string;
    hireDate?: string;
    salary?: number;
    position: Position;
    status: EmployeeStatus;
}

export interface EmployeeFormData {
    firstName: string;
    lastName: string;
    email: string;
    password?: string;
    phone?: string;
    address?: string;
    dateOfBirth?: string;
    hireDate?: string;
    salary?: number;
    position: Position;
    status: EmployeeStatus;
}

// Course Types
export interface Course {
    id: string;
    courseCode: string;
    title: string;
    description?: string;
    credits: number;
}

export interface CourseFormData {
    courseCode: string;
    title: string;
    description?: string;
    credits: number;
}

// Course Class Types
export interface CourseClass {
    id: string;
    course: Course;
    lecturer: Employee;
    semester: string;
    academicYear: number;
    currentCapacity: number;
    maxCapacity: number;
    status: CourseClassStatus;
}

export interface CourseClassFormData {
    courseId: string;
    lecturerId: string;
    semester: string;
    academicYear: number;
    maxCapacity: number;
    status?: CourseClassStatus;
}

// Enrollment Types
export interface Enrollment {
    id: string;
    student: Student;
    courseClass: CourseClass;
    grade?: string;
    status: EnrollmentStatus;
}

export interface EnrollmentFormData {
    studentId: string;
    classId: string;
    grade?: string;
    status: EnrollmentStatus;
}

// Enums
export enum Role {
    ADMIN = 'ADMIN',
    EMPLOYEE = 'EMPLOYEE',
    STUDENT = 'STUDENT',
}

export enum StudentStatus {
    ACTIVE = 'ACTIVE',
    GRADUATED = 'GRADUATED',
    SUSPENDED = 'SUSPENDED',
}

export enum EmployeeStatus {
    ACTIVE = 'ACTIVE',
    TERMINATED = 'TERMINATED',
}

export enum Position {
    LECTURER = 'LECTURER',
    SECRETARY = 'SECRETARY',
    ADMIN_OFFICER = 'ADMIN_OFFICER',
    STUDENT_AFFAIRS = 'STUDENT_AFFAIRS',
    DEAN = 'DEAN',
    TEACHING_ASSISTANT = 'TEACHING_ASSISTANT',
}

export enum CourseClassStatus {
    ACTIVE = 'ACTIVE',
    COMPLETED = 'COMPLETED',
    CANCELLED = 'CANCELLED',
}

export enum EnrollmentStatus {
    ENROLLED = 'ENROLLED',
    COMPLETED = 'COMPLETED',
    DROPPED = 'DROPPED',
    WITHDRAWN = 'WITHDRAWN',
}

// Auth Request/Response Types
export interface LoginRequest {
    email: string;
    password: string;
}

export interface RegisterRequest {
    firstName: string;
    lastName: string;
    email: string;
    password: string;
}

export interface AuthResponse {
    user: User;
}

export interface TokenResponse {
    token: string;
}

export interface ProfileResponse {
    profile: Profile;
}
