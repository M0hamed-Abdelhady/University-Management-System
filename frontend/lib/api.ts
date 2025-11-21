import { apiClient } from './api-client';
import {
    LoginRequest,
    RegisterRequest,
    AuthResponse,
    TokenResponse,
    ProfileResponse,
    UpdateProfileRequest,
    Student,
    StudentFormData,
    Employee,
    EmployeeFormData,
    Course,
    CourseFormData,
    CourseClass,
    CourseClassFormData,
    Enrollment,
    EnrollmentFormData,
    PaginatedResponse,
} from './types';

// Authentication APIs
export const authApi = {
    login: (data: LoginRequest) =>
        apiClient.post<AuthResponse>('/auth/login', data),

    register: (data: RegisterRequest) =>
        apiClient.post<AuthResponse>('/auth/register', data),

    refreshToken: () => apiClient.post<TokenResponse>('/auth/refresh'),

    getCurrentUser: () => apiClient.get<ProfileResponse>('/auth/me'),

    updateProfile: (data: UpdateProfileRequest) =>
        apiClient.put<ProfileResponse>('/auth/me', data),
};

// Student APIs
export const studentApi = {
    getAll: (page = 0, size = 20) =>
        apiClient.get<{
            students: Student[];
            pagination: PaginatedResponse<Student>['pagination'];
        }>('/students', { page, size }),

    getById: (id: string) =>
        apiClient.get<{ student: Student }>(`/students/${id}`),

    getProfile: () => apiClient.get('/students/me'),

    create: (data: StudentFormData) =>
        apiClient.post<{ student: Student }>('/students', data),

    update: (id: string, data: StudentFormData) =>
        apiClient.put<{ student: Student }>(`/students/${id}`, data),

    delete: (id: string) => apiClient.delete(`/students/${id}`),

    getEnrollments: (page = 0, size = 20) =>
        apiClient.get<{
            enrollments: Enrollment[];
            pagination: PaginatedResponse<Enrollment>['pagination'];
        }>('/students/enrollments', { page, size }),

    enroll: (classId: string) =>
        apiClient.post<{ enrollment: Enrollment }>(
            `/students/enroll?classId=${classId}`
        ),

    dropEnrollment: (enrollmentId: string) =>
        apiClient.post(`/students/drop/${enrollmentId}`),

    updateGPA: (studentId: string) =>
        apiClient.put(`/students/${studentId}/gpa`),

    getClasses: (page = 0, size = 10) =>
        apiClient.get<{
            classes: CourseClass[];
            pagination: PaginatedResponse<CourseClass>['pagination'];
        }>('/students/classes', { page, size }),
};

// Employee APIs
export const employeeApi = {
    getAll: (page = 0, size = 20) =>
        apiClient.get<{
            employees: Employee[];
            pagination: PaginatedResponse<Employee>['pagination'];
        }>('/employees', { page, size }),

    getById: (id: string) =>
        apiClient.get<{ employee: Employee }>(`/employees/${id}`),

    getProfile: () => apiClient.get('/employees/me'),

    create: (data: EmployeeFormData) =>
        apiClient.post<{ employee: Employee }>('/employees', data),

    update: (id: string, data: EmployeeFormData) =>
        apiClient.put<{ employee: Employee }>(`/employees/${id}`, data),

    delete: (id: string) => apiClient.delete(`/employees/${id}`),
};

// Course APIs
export const courseApi = {
    getAll: (page = 0, size = 10) =>
        apiClient.get<{
            courses: Course[];
            pagination: PaginatedResponse<Course>['pagination'];
        }>('/courses', { page, size }),

    getById: (id: string) =>
        apiClient.get<{ course: Course }>(`/courses/${id}`),

    create: (data: CourseFormData) =>
        apiClient.post<{ course: Course }>('/courses', data),

    update: (id: string, data: CourseFormData) =>
        apiClient.put<{ course: Course }>(`/courses/${id}`, data),

    delete: (id: string) => apiClient.delete(`/courses/${id}`),
};

// Course Class APIs
export const courseClassApi = {
    getAll: (page = 0, size = 10) =>
        apiClient.get<{
            classes: CourseClass[];
            pagination: PaginatedResponse<CourseClass>['pagination'];
        }>('/classes', { page, size }),

    getById: (id: string) =>
        apiClient.get<{ class: CourseClass }>(`/classes/${id}`),

    create: (data: CourseClassFormData) =>
        apiClient.post<{ class: CourseClass }>('/classes', data),

    update: (id: string, data: CourseClassFormData) =>
        apiClient.put<{ class: CourseClass }>(`/classes/${id}`, data),

    delete: (id: string) => apiClient.delete(`/classes/${id}`),

    addTA: (classId: string, employeeId: string) =>
        apiClient.post(`/classes/${classId}/tas?employeeId=${employeeId}`),

    removeTA: (classId: string, taId: string) =>
        apiClient.delete(`/classes/${classId}/tas/${taId}`),
};

// Enrollment APIs
export const enrollmentApi = {
    getAll: (page = 0, size = 10) =>
        apiClient.get<{
            enrollments: Enrollment[];
            pagination: PaginatedResponse<Enrollment>['pagination'];
        }>('/enrollments', { page, size }),

    getById: (id: string) =>
        apiClient.get<{ enrollment: Enrollment }>(`/enrollments/${id}`),

    create: (data: EnrollmentFormData) =>
        apiClient.post<{ enrollment: Enrollment }>('/enrollments', data),

    update: (id: string, data: EnrollmentFormData) =>
        apiClient.put<{ enrollment: Enrollment }>(`/enrollments/${id}`, data),

    delete: (id: string) => apiClient.delete(`/enrollments/${id}`),

    updateGrade: (id: string, grade: string) =>
        apiClient.put<{ enrollment: Enrollment }>(
            `/enrollments/${id}/grade`,
            grade,
            {
                headers: {
                    'Content-Type': 'text/plain',
                },
            }
        ),
};
