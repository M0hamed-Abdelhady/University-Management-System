# University Management System - API Documentation

## Base URL
```
http://localhost:8080/api/v1
```

## Authentication
Most endpoints require authentication via JWT token. Include the token in the Authorization header:
```
Authorization: Bearer <token>
```

## Standard Response Format
All API responses follow this structure:

```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Success message",
  "error": null,
  "path": "/api/v1/endpoint",
  "data": {
    // Response data here
  }
}
```

### Error Response Format
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 400,
  "message": null,
  "error": "Error message describing what went wrong",
  "path": "/api/v1/endpoint",
  "data": null
}
```

---

## Table of Contents
1. [Authentication APIs](#authentication-apis)
2. [Student APIs](#student-apis)
3. [Employee APIs](#employee-apis)
4. [Course APIs](#course-apis)
5. [Course Class APIs](#course-class-apis)
6. [Enrollment APIs](#enrollment-apis)
7. [Enums Reference](#enums-reference)

---

## Authentication APIs

### 1. Register
**Endpoint:** `POST /api/v1/auth/register`  
**Authentication:** Not required  
**Description:** Register a new user account

#### Request Body
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "password": "password123"
}
```

#### Validation Rules
- `firstName`: Required, not blank
- `lastName`: Required, not blank
- `email`: Required, valid email format
- `password`: Required, minimum 8 characters, maximum 255 characters

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "User registered successfully",
  "error": null,
  "path": "/api/v1/auth/register",
  "data": {
    "user": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "roles": ["STUDENT"],
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    }
  }
}
```

---

### 2. Login
**Endpoint:** `POST /api/v1/auth/login`  
**Authentication:** Not required  
**Description:** Login with email and password

#### Request Body
```json
{
  "email": "john.doe@example.com",
  "password": "password123"
}
```

#### Validation Rules
- `email`: Required, valid email format
- `password`: Required, minimum 8 characters

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Login successful",
  "error": null,
  "path": "/api/v1/auth/login",
  "data": {
    "user": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "roles": ["STUDENT"],
      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    }
  }
}
```

#### Error Response (401 Unauthorized)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 401,
  "message": null,
  "error": "Invalid email or password",
  "path": "/api/v1/auth/login",
  "data": null
}
```

---

### 3. Refresh Token
**Endpoint:** `POST /api/v1/auth/refresh`  
**Authentication:** Required  
**Description:** Refresh the JWT token

#### Request Headers
```
Authorization: Bearer <old_token>
```

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Token refreshed successfully",
  "error": null,
  "path": "/api/v1/auth/refresh",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

---

### 4. Get Current User Profile
**Endpoint:** `GET /api/v1/auth/me`  
**Authentication:** Required  
**Description:** Get the current authenticated user's profile

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Profile retrieved successfully",
  "error": null,
  "path": "/api/v1/auth/me",
  "data": {
    "profile": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "firstName": "John",
      "lastName": "Doe",
      "email": "john.doe@example.com",
      "phone": "+1234567890",
      "address": "123 Main St, City",
      "dateOfBirth": "2000-01-15",
      "roles": ["STUDENT"]
    }
  }
}
```

---

## Student APIs

### 1. Get All Students
**Endpoint:** `GET /api/v1/students`  
**Authentication:** Required (ADMIN or EMPLOYEE)  
**Description:** Get paginated list of all students

#### Query Parameters
- `page` (optional, default: 0): Page number
- `size` (optional, default: 20): Number of items per page

#### Example Request
```
GET /api/v1/students?page=0&size=20
```

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Students retrieved successfully",
  "error": null,
  "path": "/api/v1/students",
  "data": {
    "students": [
      {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "person": {
          "id": "660e8400-e29b-41d4-a716-446655440000",
          "firstName": "John",
          "lastName": "Doe",
          "email": "john.doe@example.com",
          "phone": "+1234567890",
          "dateOfBirth": "2000-01-15",
          "address": "123 Main St, City",
          "role": "STUDENT"
        },
        "studentNumber": "STU2024001",
        "major": "Computer Science",
        "academicYear": 2,
        "gpa": 3.75,
        "status": "ACTIVE"
      }
    ],
    "pagination": {
      "currentPage": 0,
      "totalPages": 5,
      "totalItems": 95,
      "pageSize": 20
    }
  }
}
```

---

### 2. Get Student By ID
**Endpoint:** `GET /api/v1/students/{id}`  
**Authentication:** Required  
**Description:** Get a specific student by ID

#### Path Parameters
- `id`: Student ID (UUID)

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Student retrieved successfully",
  "error": null,
  "path": "/api/v1/students/550e8400-e29b-41d4-a716-446655440000",
  "data": {
    "student": {
      "id": "550e8400-e29b-41d4-a716-446655440000",
      "person": {
        "id": "660e8400-e29b-41d4-a716-446655440000",
        "firstName": "John",
        "lastName": "Doe",
        "email": "john.doe@example.com",
        "phone": "+1234567890",
        "dateOfBirth": "2000-01-15",
        "address": "123 Main St, City",
        "role": "STUDENT"
      },
      "studentNumber": "STU2024001",
      "major": "Computer Science",
      "academicYear": 2,
      "gpa": 3.75,
      "status": "ACTIVE"
    }
  }
}
```

#### Error Response (404 Not Found)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 404,
  "message": null,
  "error": "Student not found with id: 550e8400-e29b-41d4-a716-446655440000",
  "path": "/api/v1/students/550e8400-e29b-41d4-a716-446655440000",
  "data": null
}
```

---

### 3. Create Student
**Endpoint:** `POST /api/v1/students`  
**Authentication:** Required (ADMIN only)  
**Description:** Create a new student

#### Request Body
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "email": "jane.smith@example.com",
  "password": "password123",
  "phone": "+1234567890",
  "dateOfBirth": "2001-05-20",
  "address": "456 Oak Ave, City",
  "major": "Business Administration",
  "academicYear": 1,
  "gpa": 3.5,
  "status": "ACTIVE"
}
```

#### Validation Rules
- `firstName`: Required, not blank
- `lastName`: Required, not blank
- `email`: Required, valid email format
- `password`: Required, minimum 8 characters, maximum 255 characters
- `phone`: Optional
- `dateOfBirth`: Optional, must be in the past
- `address`: Optional
- `major`: Optional
- `academicYear`: Optional
- `gpa`: Optional, between 0.0 and 4.0
- `status`: Required, must be valid StudentStatus enum value

#### Success Response (201 Created)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 201,
  "message": "Student created successfully",
  "error": null,
  "path": "/api/v1/students",
  "data": {
    "student": {
      "id": "770e8400-e29b-41d4-a716-446655440000",
      "person": {
        "id": "880e8400-e29b-41d4-a716-446655440000",
        "firstName": "Jane",
        "lastName": "Smith",
        "email": "jane.smith@example.com",
        "phone": "+1234567890",
        "dateOfBirth": "2001-05-20",
        "address": "456 Oak Ave, City",
        "role": "STUDENT"
      },
      "studentNumber": "STU2024025",
      "major": "Business Administration",
      "academicYear": 1,
      "gpa": 3.5,
      "status": "ACTIVE"
    }
  }
}
```

---

### 4. Update Student
**Endpoint:** `PUT /api/v1/students/{id}`  
**Authentication:** Required (ADMIN only)  
**Description:** Update an existing student

#### Path Parameters
- `id`: Student ID (UUID)

#### Request Body
Same as Create Student request body

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Student updated successfully",
  "error": null,
  "path": "/api/v1/students/770e8400-e29b-41d4-a716-446655440000",
  "data": {
    "student": {
      "id": "770e8400-e29b-41d4-a716-446655440000",
      "person": {
        "id": "880e8400-e29b-41d4-a716-446655440000",
        "firstName": "Jane",
        "lastName": "Smith",
        "email": "jane.smith@example.com",
        "phone": "+1234567890",
        "dateOfBirth": "2001-05-20",
        "address": "456 Oak Ave, City",
        "role": "STUDENT"
      },
      "studentNumber": "STU2024025",
      "major": "Computer Science",
      "academicYear": 2,
      "gpa": 3.8,
      "status": "ACTIVE"
    }
  }
}
```

---

### 5. Delete Student
**Endpoint:** `DELETE /api/v1/students/{id}`  
**Authentication:** Required (ADMIN only)  
**Description:** Delete a student

#### Path Parameters
- `id`: Student ID (UUID)

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Student deleted successfully",
  "error": null,
  "path": "/api/v1/students/770e8400-e29b-41d4-a716-446655440000",
  "data": null
}
```

---

### 6. Get Student Enrollments
**Endpoint:** `GET /api/v1/students/enrollments`  
**Authentication:** Required (Current Student)  
**Description:** Get current student's enrollments

#### Query Parameters
- `page` (optional, default: 0): Page number
- `size` (optional, default: 20): Number of items per page

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Enrollments retrieved successfully",
  "error": null,
  "path": "/api/v1/students/enrollments",
  "data": {
    "enrollments": [
      {
        "id": "990e8400-e29b-41d4-a716-446655440000",
        "student": {
          "id": "550e8400-e29b-41d4-a716-446655440000",
          "person": {
            "id": "660e8400-e29b-41d4-a716-446655440000",
            "firstName": "John",
            "lastName": "Doe",
            "email": "john.doe@example.com",
            "phone": "+1234567890",
            "dateOfBirth": "2000-01-15",
            "address": "123 Main St, City",
            "role": "STUDENT"
          },
          "studentNumber": "STU2024001",
          "major": "Computer Science",
          "academicYear": 2,
          "gpa": 3.75,
          "status": "ACTIVE"
        },
        "courseClass": {
          "id": "aa0e8400-e29b-41d4-a716-446655440000",
          "course": {
            "id": "bb0e8400-e29b-41d4-a716-446655440000",
            "courseCode": "CS101",
            "title": "Introduction to Computer Science",
            "description": "Basic concepts of computer science",
            "credits": 3
          },
          "lecturer": {
            "id": "cc0e8400-e29b-41d4-a716-446655440000",
            "person": {
              "id": "dd0e8400-e29b-41d4-a716-446655440000",
              "firstName": "Prof",
              "lastName": "Johnson",
              "email": "prof.johnson@university.com",
              "phone": "+1987654321",
              "dateOfBirth": "1975-03-10",
              "address": "789 University Rd",
              "role": "EMPLOYEE"
            },
            "employeeId": "EMP2024001",
            "hireDate": "2015-09-01",
            "salary": 75000.00,
            "position": "LECTURER",
            "status": "ACTIVE"
          },
          "semester": "Fall 2024",
          "academicYear": 2024,
          "currentCapacity": 28,
          "maxCapacity": 30,
          "status": "ACTIVE"
        },
        "grade": "A",
        "status": "ENROLLED"
      }
    ],
    "pagination": {
      "currentPage": 0,
      "totalPages": 1,
      "totalItems": 5,
      "pageSize": 20
    }
  }
}
```

---

### 7. Enroll Student
**Endpoint:** `POST /api/v1/students/enroll`  
**Authentication:** Required (Current Student)  
**Description:** Enroll current student in a course class

#### Query Parameters
- `classId`: Course Class ID (UUID)

#### Example Request
```
POST /api/v1/students/enroll?classId=aa0e8400-e29b-41d4-a716-446655440000
```

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Student enrolled successfully",
  "error": null,
  "path": "/api/v1/students/enroll",
  "data": {
    "enrollment": {
      "id": "ee0e8400-e29b-41d4-a716-446655440000",
      "student": {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "person": {
          "id": "660e8400-e29b-41d4-a716-446655440000",
          "firstName": "John",
          "lastName": "Doe",
          "email": "john.doe@example.com",
          "phone": "+1234567890",
          "dateOfBirth": "2000-01-15",
          "address": "123 Main St, City",
          "role": "STUDENT"
        },
        "studentNumber": "STU2024001",
        "major": "Computer Science",
        "academicYear": 2,
        "gpa": 3.75,
        "status": "ACTIVE"
      },
      "courseClass": {
        "id": "aa0e8400-e29b-41d4-a716-446655440000",
        "course": {
          "id": "bb0e8400-e29b-41d4-a716-446655440000",
          "courseCode": "CS101",
          "title": "Introduction to Computer Science",
          "description": "Basic concepts of computer science",
          "credits": 3
        },
        "lecturer": {
          "id": "cc0e8400-e29b-41d4-a716-446655440000",
          "person": {
            "id": "dd0e8400-e29b-41d4-a716-446655440000",
            "firstName": "Prof",
            "lastName": "Johnson",
            "email": "prof.johnson@university.com",
            "phone": "+1987654321",
            "dateOfBirth": "1975-03-10",
            "address": "789 University Rd",
            "role": "EMPLOYEE"
          },
          "employeeId": "EMP2024001",
          "hireDate": "2015-09-01",
          "salary": 75000.00,
          "position": "LECTURER",
          "status": "ACTIVE"
        },
        "semester": "Fall 2024",
        "academicYear": 2024,
        "currentCapacity": 29,
        "maxCapacity": 30,
        "status": "ACTIVE"
      },
      "grade": null,
      "status": "ENROLLED"
    }
  }
}
```

---

### 8. Drop Enrollment
**Endpoint:** `POST /api/v1/students/drop/{enrollmentId}`  
**Authentication:** Required (Current Student)  
**Description:** Drop an enrollment

#### Path Parameters
- `enrollmentId`: Enrollment ID (UUID)

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Enrollment dropped successfully",
  "error": null,
  "path": "/api/v1/students/drop/ee0e8400-e29b-41d4-a716-446655440000",
  "data": null
}
```

---

## Employee APIs

### 1. Get All Employees
**Endpoint:** `GET /api/v1/employees`  
**Authentication:** Required (ADMIN only)  
**Description:** Get paginated list of all employees

#### Query Parameters
- `page` (optional, default: 0): Page number
- `size` (optional, default: 20): Number of items per page

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Employees retrieved successfully",
  "error": null,
  "path": "/api/v1/employees",
  "data": {
    "employees": [
      {
        "id": "cc0e8400-e29b-41d4-a716-446655440000",
        "person": {
          "id": "dd0e8400-e29b-41d4-a716-446655440000",
          "firstName": "Prof",
          "lastName": "Johnson",
          "email": "prof.johnson@university.com",
          "phone": "+1987654321",
          "dateOfBirth": "1975-03-10",
          "address": "789 University Rd",
          "role": "EMPLOYEE"
        },
        "employeeId": "EMP2024001",
        "hireDate": "2015-09-01",
        "salary": 75000.00,
        "position": "LECTURER",
        "status": "ACTIVE"
      }
    ],
    "pagination": {
      "currentPage": 0,
      "totalPages": 2,
      "totalItems": 35,
      "pageSize": 20
    }
  }
}
```

---

### 2. Get Employee By ID
**Endpoint:** `GET /api/v1/employees/{id}`  
**Authentication:** Required (ADMIN or EMPLOYEE)  
**Description:** Get a specific employee by ID

#### Path Parameters
- `id`: Employee ID (UUID)

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Employee retrieved successfully",
  "error": null,
  "path": "/api/v1/employees/cc0e8400-e29b-41d4-a716-446655440000",
  "data": {
    "employee": {
      "id": "cc0e8400-e29b-41d4-a716-446655440000",
      "person": {
        "id": "dd0e8400-e29b-41d4-a716-446655440000",
        "firstName": "Prof",
        "lastName": "Johnson",
        "email": "prof.johnson@university.com",
        "phone": "+1987654321",
        "dateOfBirth": "1975-03-10",
        "address": "789 University Rd",
        "role": "EMPLOYEE"
      },
      "employeeId": "EMP2024001",
      "hireDate": "2015-09-01",
      "salary": 75000.00,
      "position": "LECTURER",
      "status": "ACTIVE"
    }
  }
}
```

---

### 3. Create Employee
**Endpoint:** `POST /api/v1/employees`  
**Authentication:** Required (ADMIN only)  
**Description:** Create a new employee

#### Request Body
```json
{
  "firstName": "Sarah",
  "lastName": "Williams",
  "email": "sarah.williams@university.com",
  "password": "password123",
  "phone": "+1555123456",
  "address": "321 Campus Dr",
  "dateOfBirth": "1980-07-15",
  "hireDate": "2024-01-15",
  "salary": 65000.00,
  "position": "SECRETARY",
  "status": "ACTIVE"
}
```

#### Validation Rules
- `firstName`: Required, not blank
- `lastName`: Required, not blank
- `email`: Required, valid email format
- `password`: Required, minimum 8 characters, maximum 255 characters
- `phone`: Optional
- `address`: Optional
- `dateOfBirth`: Optional
- `hireDate`: Optional
- `salary`: Optional, must be positive
- `position`: Required, must be valid Position enum value
- `status`: Required, must be valid EmployeeStatus enum value

#### Success Response (201 Created)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 201,
  "message": "Employee created successfully",
  "error": null,
  "path": "/api/v1/employees",
  "data": {
    "employee": {
      "id": "ff0e8400-e29b-41d4-a716-446655440000",
      "person": {
        "id": "gg0e8400-e29b-41d4-a716-446655440000",
        "firstName": "Sarah",
        "lastName": "Williams",
        "email": "sarah.williams@university.com",
        "phone": "+1555123456",
        "dateOfBirth": "1980-07-15",
        "address": "321 Campus Dr",
        "role": "EMPLOYEE"
      },
      "employeeId": "EMP2024010",
      "hireDate": "2024-01-15",
      "salary": 65000.00,
      "position": "SECRETARY",
      "status": "ACTIVE"
    }
  }
}
```

---

### 4. Update Employee
**Endpoint:** `PUT /api/v1/employees/{id}`  
**Authentication:** Required (ADMIN only)  
**Description:** Update an existing employee

#### Path Parameters
- `id`: Employee ID (UUID)

#### Request Body
Same as Create Employee request body

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Employee updated successfully",
  "error": null,
  "path": "/api/v1/employees/ff0e8400-e29b-41d4-a716-446655440000",
  "data": {
    "employee": {
      "id": "ff0e8400-e29b-41d4-a716-446655440000",
      "person": {
        "id": "gg0e8400-e29b-41d4-a716-446655440000",
        "firstName": "Sarah",
        "lastName": "Williams",
        "email": "sarah.williams@university.com",
        "phone": "+1555123456",
        "dateOfBirth": "1980-07-15",
        "address": "321 Campus Dr",
        "role": "EMPLOYEE"
      },
      "employeeId": "EMP2024010",
      "hireDate": "2024-01-15",
      "salary": 68000.00,
      "position": "ADMIN_OFFICER",
      "status": "ACTIVE"
    }
  }
}
```

---

### 5. Delete Employee
**Endpoint:** `DELETE /api/v1/employees/{id}`  
**Authentication:** Required (ADMIN only)  
**Description:** Delete an employee

#### Path Parameters
- `id`: Employee ID (UUID)

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Employee deleted successfully",
  "error": null,
  "path": "/api/v1/employees/ff0e8400-e29b-41d4-a716-446655440000",
  "data": null
}
```

---

## Course APIs

### 1. Get All Courses
**Endpoint:** `GET /api/v1/courses`  
**Authentication:** Required  
**Description:** Get paginated list of all courses

#### Query Parameters
- `page` (optional, default: 0): Page number
- `size` (optional, default: 10): Number of items per page

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Courses retrieved successfully",
  "error": null,
  "path": "/api/v1/courses",
  "data": {
    "courses": [
      {
        "id": "bb0e8400-e29b-41d4-a716-446655440000",
        "courseCode": "CS101",
        "title": "Introduction to Computer Science",
        "description": "Basic concepts of computer science",
        "credits": 3
      },
      {
        "id": "hh0e8400-e29b-41d4-a716-446655440000",
        "courseCode": "MATH201",
        "title": "Calculus II",
        "description": "Advanced calculus topics",
        "credits": 4
      }
    ],
    "pagination": {
      "currentPage": 0,
      "totalPages": 8,
      "totalItems": 75,
      "pageSize": 10
    }
  }
}
```

---

### 2. Get Course By ID
**Endpoint:** `GET /api/v1/courses/{id}`  
**Authentication:** Required  
**Description:** Get a specific course by ID

#### Path Parameters
- `id`: Course ID (UUID)

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Course retrieved successfully",
  "error": null,
  "path": "/api/v1/courses/bb0e8400-e29b-41d4-a716-446655440000",
  "data": {
    "course": {
      "id": "bb0e8400-e29b-41d4-a716-446655440000",
      "courseCode": "CS101",
      "title": "Introduction to Computer Science",
      "description": "Basic concepts of computer science including programming fundamentals, algorithms, and data structures",
      "credits": 3
    }
  }
}
```

---

### 3. Create Course
**Endpoint:** `POST /api/v1/courses`  
**Authentication:** Required (ADMIN only)  
**Description:** Create a new course

#### Request Body
```json
{
  "courseCode": "CS202",
  "title": "Data Structures and Algorithms",
  "description": "Advanced data structures and algorithm design techniques",
  "credits": 4
}
```

#### Validation Rules
- `courseCode`: Required, not blank
- `title`: Required, not blank
- `description`: Optional
- `credits`: Required, must be positive

#### Success Response (201 Created)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 201,
  "message": "Course created successfully",
  "error": null,
  "path": "/api/v1/courses",
  "data": {
    "course": {
      "id": "ii0e8400-e29b-41d4-a716-446655440000",
      "courseCode": "CS202",
      "title": "Data Structures and Algorithms",
      "description": "Advanced data structures and algorithm design techniques",
      "credits": 4
    }
  }
}
```

---

### 4. Update Course
**Endpoint:** `PUT /api/v1/courses/{id}`  
**Authentication:** Required (ADMIN only)  
**Description:** Update an existing course

#### Path Parameters
- `id`: Course ID (UUID)

#### Request Body
Same as Create Course request body

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Course updated successfully",
  "error": null,
  "path": "/api/v1/courses/ii0e8400-e29b-41d4-a716-446655440000",
  "data": {
    "course": {
      "id": "ii0e8400-e29b-41d4-a716-446655440000",
      "courseCode": "CS202",
      "title": "Data Structures and Algorithms II",
      "description": "Advanced data structures and algorithm design techniques with focus on optimization",
      "credits": 4
    }
  }
}
```

---

### 5. Delete Course
**Endpoint:** `DELETE /api/v1/courses/{id}`  
**Authentication:** Required (ADMIN only)  
**Description:** Delete a course

#### Path Parameters
- `id`: Course ID (UUID)

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Course deleted successfully",
  "error": null,
  "path": "/api/v1/courses/ii0e8400-e29b-41d4-a716-446655440000",
  "data": null
}
```

---

## Course Class APIs

### 1. Get All Course Classes
**Endpoint:** `GET /api/v1/classes`  
**Authentication:** Required  
**Description:** Get paginated list of all course classes

#### Query Parameters
- `page` (optional, default: 0): Page number
- `size` (optional, default: 10): Number of items per page

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Course classes retrieved successfully",
  "error": null,
  "path": "/api/v1/classes",
  "data": {
    "classes": [
      {
        "id": "aa0e8400-e29b-41d4-a716-446655440000",
        "course": {
          "id": "bb0e8400-e29b-41d4-a716-446655440000",
          "courseCode": "CS101",
          "title": "Introduction to Computer Science",
          "description": "Basic concepts of computer science",
          "credits": 3
        },
        "lecturer": {
          "id": "cc0e8400-e29b-41d4-a716-446655440000",
          "person": {
            "id": "dd0e8400-e29b-41d4-a716-446655440000",
            "firstName": "Prof",
            "lastName": "Johnson",
            "email": "prof.johnson@university.com",
            "phone": "+1987654321",
            "dateOfBirth": "1975-03-10",
            "address": "789 University Rd",
            "role": "EMPLOYEE"
          },
          "employeeId": "EMP2024001",
          "hireDate": "2015-09-01",
          "salary": 75000.00,
          "position": "LECTURER",
          "status": "ACTIVE"
        },
        "semester": "Fall 2024",
        "academicYear": 2024,
        "currentCapacity": 28,
        "maxCapacity": 30,
        "status": "ACTIVE"
      }
    ],
    "pagination": {
      "currentPage": 0,
      "totalPages": 4,
      "totalItems": 38,
      "pageSize": 10
    }
  }
}
```

---

### 2. Get Course Class By ID
**Endpoint:** `GET /api/v1/classes/{id}`  
**Authentication:** Required  
**Description:** Get a specific course class by ID

#### Path Parameters
- `id`: Course Class ID (UUID)

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Course class retrieved successfully",
  "error": null,
  "path": "/api/v1/classes/aa0e8400-e29b-41d4-a716-446655440000",
  "data": {
    "class": {
      "id": "aa0e8400-e29b-41d4-a716-446655440000",
      "course": {
        "id": "bb0e8400-e29b-41d4-a716-446655440000",
        "courseCode": "CS101",
        "title": "Introduction to Computer Science",
        "description": "Basic concepts of computer science",
        "credits": 3
      },
      "lecturer": {
        "id": "cc0e8400-e29b-41d4-a716-446655440000",
        "person": {
          "id": "dd0e8400-e29b-41d4-a716-446655440000",
          "firstName": "Prof",
          "lastName": "Johnson",
          "email": "prof.johnson@university.com",
          "phone": "+1987654321",
          "dateOfBirth": "1975-03-10",
          "address": "789 University Rd",
          "role": "EMPLOYEE"
        },
        "employeeId": "EMP2024001",
        "hireDate": "2015-09-01",
        "salary": 75000.00,
        "position": "LECTURER",
        "status": "ACTIVE"
      },
      "semester": "Fall 2024",
      "academicYear": 2024,
      "currentCapacity": 28,
      "maxCapacity": 30,
      "status": "ACTIVE"
    }
  }
}
```

---

### 3. Create Course Class
**Endpoint:** `POST /api/v1/classes`  
**Authentication:** Required (ADMIN only)  
**Description:** Create a new course class

#### Request Body
```json
{
  "courseId": "bb0e8400-e29b-41d4-a716-446655440000",
  "lecturerId": "cc0e8400-e29b-41d4-a716-446655440000",
  "semester": "Spring 2025",
  "academicYear": 2025,
  "maxCapacity": 35,
  "status": "ACTIVE"
}
```

#### Validation Rules
- `courseId`: Required, not blank
- `lecturerId`: Required, not blank
- `semester`: Required, not blank
- `academicYear`: Required
- `maxCapacity`: Required, must be positive
- `status`: Optional

#### Success Response (201 Created)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 201,
  "message": "Course class created successfully",
  "error": null,
  "path": "/api/v1/classes",
  "data": {
    "class": {
      "id": "jj0e8400-e29b-41d4-a716-446655440000",
      "course": {
        "id": "bb0e8400-e29b-41d4-a716-446655440000",
        "courseCode": "CS101",
        "title": "Introduction to Computer Science",
        "description": "Basic concepts of computer science",
        "credits": 3
      },
      "lecturer": {
        "id": "cc0e8400-e29b-41d4-a716-446655440000",
        "person": {
          "id": "dd0e8400-e29b-41d4-a716-446655440000",
          "firstName": "Prof",
          "lastName": "Johnson",
          "email": "prof.johnson@university.com",
          "phone": "+1987654321",
          "dateOfBirth": "1975-03-10",
          "address": "789 University Rd",
          "role": "EMPLOYEE"
        },
        "employeeId": "EMP2024001",
        "hireDate": "2015-09-01",
        "salary": 75000.00,
        "position": "LECTURER",
      "semester": "Spring 2025",
      "academicYear": 2025,
      "currentCapacity": 0,
      "maxCapacity": 35,
      "status": "ACTIVE"
    }
  }
}
```

---

### 4. Update Course Class
**Endpoint:** `PUT /api/v1/classes/{id}`  
**Authentication:** Required (ADMIN only)  
**Description:** Update an existing course class

#### Path Parameters
- `id`: Course Class ID (UUID)

#### Request Body
Same as Create Course Class request body

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Course class updated successfully",
  "error": null,
  "path": "/api/v1/classes/jj0e8400-e29b-41d4-a716-446655440000",
  "data": {
    "class": { /* updated course class details */ }
  }
}
```

---

### 5. Delete Course Class
**Endpoint:** `DELETE /api/v1/classes/{id}`  
**Authentication:** Required (ADMIN only)  
**Description:** Delete a course class

#### Path Parameters
- `id`: Course Class ID (UUID)

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Course class deleted successfully",
  "error": null,
  "path": "/api/v1/classes/jj0e8400-e29b-41d4-a716-446655440000",
  "data": null
}
```

---

### 6. Add Teaching Assistant
**Endpoint:** `POST /api/v1/classes/{id}/tas`  
**Authentication:** Required (ADMIN or EMPLOYEE)  
**Description:** Add a teaching assistant to a course class

#### Path Parameters
- `id`: Course Class ID (UUID)

#### Query Parameters
- `employeeId`: Employee ID (UUID)

#### Example Request
```
POST /api/v1/classes/aa0e8400-e29b-41d4-a716-446655440000/tas?employeeId=kk0e8400-e29b-41d4-a716-446655440000
```

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Teaching assistant added successfully",
  "error": null,
  "path": "/api/v1/classes/aa0e8400-e29b-41d4-a716-446655440000/tas",
  "data": {
    "class": { /* updated course class with TA details */ }
  }
}
```

---

### 7. Remove Teaching Assistant
**Endpoint:** `DELETE /api/v1/classes/{id}/tas/{taId}`  
**Authentication:** Required (ADMIN or EMPLOYEE)  
**Description:** Remove a teaching assistant from a course class

#### Path Parameters
- `id`: Course Class ID (UUID)
- `taId`: Teaching Assistant (Employee) ID (UUID)

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Teaching assistant removed successfully",
  "error": null,
  "path": "/api/v1/classes/aa0e8400-e29b-41d4-a716-446655440000/tas/kk0e8400-e29b-41d4-a716-446655440000",
  "data": null
}
```

---

## Enrollment APIs

### 1. Get All Enrollments
**Endpoint:** `GET /api/v1/enrollments`  
**Authentication:** Required (ADMIN or EMPLOYEE)  
**Description:** Get paginated list of all enrollments

#### Query Parameters
- `page` (optional, default: 0): Page number
- `size` (optional, default: 10): Number of items per page

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Enrollments retrieved successfully",
  "error": null,
  "path": "/api/v1/enrollments",
  "data": {
    "enrollments": [
      {
        "id": "990e8400-e29b-41d4-a716-446655440000",
        "student": { /* student details */ },
        "courseClass": { /* course class details */ },
        "grade": "A",
        "status": "COMPLETED"
      }
    ],
    "pagination": {
      "currentPage": 0,
      "totalPages": 15,
      "totalItems": 147,
      "pageSize": 10
    }
  }
}
```

---

### 2. Get Enrollment By ID
**Endpoint:** `GET /api/v1/enrollments/{id}`  
**Authentication:** Required  
**Description:** Get a specific enrollment by ID

#### Path Parameters
- `id`: Enrollment ID (UUID)

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Enrollment retrieved successfully",
  "error": null,
  "path": "/api/v1/enrollments/990e8400-e29b-41d4-a716-446655440000",
  "data": {
    "enrollment": {
      "id": "990e8400-e29b-41d4-a716-446655440000",
      "student": { /* student details */ },
      "courseClass": { /* course class details */ },
      "grade": "A",
      "status": "COMPLETED"
    }
  }
}
```

---

### 3. Create Enrollment
**Endpoint:** `POST /api/v1/enrollments`  
**Authentication:** Required (ADMIN or EMPLOYEE)  
**Description:** Create a new enrollment

#### Request Body
```json
{
  "studentId": "550e8400-e29b-41d4-a716-446655440000",
  "classId": "aa0e8400-e29b-41d4-a716-446655440000",
  "grade": null,
  "status": "ENROLLED"
}
```

#### Validation Rules
- `studentId`: Required, not blank
- `classId`: Required, not blank
- `grade`: Optional
- `status`: Required, must be valid EnrollmentStatus enum value

#### Success Response (201 Created)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 201,
  "message": "Enrollment created successfully",
  "error": null,
  "path": "/api/v1/enrollments",
  "data": {
    "enrollment": {
      "id": "ll0e8400-e29b-41d4-a716-446655440000",
      "student": { /* student details */ },
      "courseClass": { /* course class details */ },
      "grade": null,
      "status": "ENROLLED"
    }
  }
}
```

---

### 4. Update Enrollment
**Endpoint:** `PUT /api/v1/enrollments/{id}`  
**Authentication:** Required (ADMIN or EMPLOYEE)  
**Description:** Update an existing enrollment

#### Path Parameters
- `id`: Enrollment ID (UUID)

#### Request Body
Same as Create Enrollment request body

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Enrollment updated successfully",
  "error": null,
  "path": "/api/v1/enrollments/ll0e8400-e29b-41d4-a716-446655440000",
  "data": {
    "enrollment": {
      "id": "ll0e8400-e29b-41d4-a716-446655440000",
      "student": {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "person": {
          "id": "660e8400-e29b-41d4-a716-446655440000",
          "firstName": "John",
          "lastName": "Doe",
          "email": "john.doe@example.com",
          "phone": "+1234567890",
          "dateOfBirth": "2000-01-15",
          "address": "123 Main St, City",
          "role": "STUDENT"
        },
        "studentNumber": "STU2024001",
        "major": "Computer Science",
        "academicYear": 2,
        "gpa": 3.75,
        "status": "ACTIVE"
      },
      "courseClass": {
        "id": "aa0e8400-e29b-41d4-a716-446655440000",
        "course": {
          "id": "bb0e8400-e29b-41d4-a716-446655440000",
          "courseCode": "CS101",
          "title": "Introduction to Computer Science",
          "description": "Basic concepts of computer science",
          "credits": 3
        },
        "lecturer": {
          "id": "cc0e8400-e29b-41d4-a716-446655440000",
          "person": {
            "id": "dd0e8400-e29b-41d4-a716-446655440000",
            "firstName": "Prof",
            "lastName": "Johnson",
            "email": "prof.johnson@university.com",
            "phone": "+1987654321",
            "dateOfBirth": "1975-03-10",
            "address": "789 University Rd",
            "role": "EMPLOYEE"
          },
          "employeeId": "EMP2024001",
          "hireDate": "2015-09-01",
          "salary": 75000.00,
          "position": "LECTURER",
          "status": "ACTIVE"
        },
        "semester": "Fall 2024",
        "academicYear": 2024,
        "currentCapacity": 28,
        "maxCapacity": 30,
        "status": "ACTIVE"
      },
      "grade": "B+",
      "status": "COMPLETED"
    }
  }
}
```

---

### 5. Delete Enrollment
**Endpoint:** `DELETE /api/v1/enrollments/{id}`  
**Authentication:** Required (ADMIN or EMPLOYEE)  
**Description:** Delete an enrollment

#### Path Parameters
- `id`: Enrollment ID (UUID)

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Enrollment deleted successfully",
  "error": null,
  "path": "/api/v1/enrollments/ll0e8400-e29b-41d4-a716-446655440000",
  "data": null
}
```

---

### 6. Update Grade
**Endpoint:** `PUT /api/v1/enrollments/{id}/grade`  
**Authentication:** Required (ADMIN or EMPLOYEE)  
**Description:** Update the grade for an enrollment

#### Path Parameters
- `id`: Enrollment ID (UUID)

#### Query Parameters
- `grade`: Grade value (e.g., "A", "B+", "C", etc.)

#### Example Request
```
PUT /api/v1/enrollments/ll0e8400-e29b-41d4-a716-446655440000/grade?grade=A-
```

#### Success Response (200 OK)
```json
{
  "timestamp": "2025-11-21T10:30:45",
  "status": 200,
  "message": "Grade updated successfully",
  "error": null,
  "path": "/api/v1/enrollments/ll0e8400-e29b-41d4-a716-446655440000/grade",
  "data": {
    "enrollment": {
      "id": "ll0e8400-e29b-41d4-a716-446655440000",
      "student": {
        "id": "550e8400-e29b-41d4-a716-446655440000",
        "person": {
          "id": "660e8400-e29b-41d4-a716-446655440000",
          "firstName": "John",
          "lastName": "Doe",
          "email": "john.doe@example.com",
          "phone": "+1234567890",
          "dateOfBirth": "2000-01-15",
          "address": "123 Main St, City",
          "role": "STUDENT"
        },
        "studentNumber": "STU2024001",
        "major": "Computer Science",
        "academicYear": 2,
        "gpa": 3.75,
        "status": "ACTIVE"
      },
      "courseClass": {
        "id": "aa0e8400-e29b-41d4-a716-446655440000",
        "course": {
          "id": "bb0e8400-e29b-41d4-a716-446655440000",
          "courseCode": "CS101",
          "title": "Introduction to Computer Science",
          "description": "Basic concepts of computer science",
          "credits": 3
        },
        "lecturer": {
          "id": "cc0e8400-e29b-41d4-a716-446655440000",
          "person": {
            "id": "dd0e8400-e29b-41d4-a716-446655440000",
            "firstName": "Prof",
            "lastName": "Johnson",
            "email": "prof.johnson@university.com",
            "phone": "+1987654321",
            "dateOfBirth": "1975-03-10",
            "address": "789 University Rd",
            "role": "EMPLOYEE"
          },
          "employeeId": "EMP2024001",
          "hireDate": "2015-09-01",
          "salary": 75000.00,
          "position": "LECTURER",
          "status": "ACTIVE"
        },
        "semester": "Fall 2024",
        "academicYear": 2024,
        "currentCapacity": 28,
        "maxCapacity": 30,
        "status": "ACTIVE"
      },
      "grade": "A-",
      "status": "ENROLLED"
    }
  }
}
```

---

## Enums Reference

### Role
User roles in the system:
- `ADMIN` - Administrator with full access
- `EMPLOYEE` - University employee (lecturers, staff)
- `STUDENT` - Student user

### StudentStatus
Student enrollment status:
- `ACTIVE` - Currently enrolled and active
- `GRADUATED` - Completed studies
- `SUSPENDED` - Temporarily suspended

### EmployeeStatus
Employee employment status:
- `ACTIVE` - Currently employed
- `TERMINATED` - No longer employed

### Position
Employee position types:
- `LECTURER` - Teaching faculty
- `SECRETARY` - Administrative secretary
- `ADMIN_OFFICER` - Administrative officer
- `STUDENT_AFFAIRS` - Student affairs officer
- `DEAN` - Dean of faculty
- `TEACHING_ASSISTANT` - Teaching assistant

### CourseClassStatus
Course class status:
- `ACTIVE` - Currently running
- `COMPLETED` - Finished
- `CANCELLED` - Cancelled

### EnrollmentStatus
Enrollment status:
- `ENROLLED` - Currently enrolled
- `COMPLETED` - Successfully completed
- `DROPPED` - Dropped by student
- `WITHDRAWN` - Withdrawn from course

---

## Common HTTP Status Codes

- **200 OK** - Request successful
- **201 Created** - Resource created successfully
- **400 Bad Request** - Invalid request data or validation error
- **401 Unauthorized** - Authentication required or invalid credentials
- **403 Forbidden** - User doesn't have permission for this action
- **404 Not Found** - Resource not found
- **500 Internal Server Error** - Server error

---

## Authentication Flow

1. **Register** or **Login** to get JWT token
2. Include token in subsequent requests: `Authorization: Bearer <token>`
3. Use **Refresh Token** endpoint to renew expired tokens
4. Use **Get Current User Profile** to verify authentication status

---

## Pagination

Most list endpoints support pagination with these query parameters:
- `page`: Page number (0-based, default: 0)
- `size`: Items per page (default varies by endpoint)

Pagination response includes:
- `currentPage`: Current page number
- `totalPages`: Total number of pages
- `totalItems`: Total number of items
- `pageSize`: Items per page

---

## Notes for Frontend Developers

1. **All timestamps are in ISO 8601 format** (e.g., "2025-11-21T10:30:45")
2. **All IDs are UUIDs** in string format
3. **Dates are in ISO format** (e.g., "2000-01-15")
4. **Monetary values** (salary) are in decimal format
5. **GPA values** are decimal between 0.0 and 4.0
6. **Token expiration** is managed by the backend; refresh tokens before they expire
7. **Error messages** are in the `error` field, success messages in the `message` field
8. **Null values** are excluded from responses (non-null fields only)
9. **Role-based access** is enforced; check user role before showing UI elements
10. **Validation errors** return 400 status with detailed error messages

---

## Example Frontend Integration

### Login Flow
```javascript
// Login request
const response = await fetch('http://localhost:8080/api/v1/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    email: 'user@example.com',
    password: 'password123'
  })
});

const data = await response.json();
if (response.ok) {
  // Store token
  localStorage.setItem('token', data.data.user.token);
  // Store user info
  localStorage.setItem('user', JSON.stringify(data.data.user));
}
```

### Authenticated Request
```javascript
const token = localStorage.getItem('token');
const response = await fetch('http://localhost:8080/api/v1/students', {
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
});

const data = await response.json();
if (response.ok) {
  // Process students data
  console.log(data.data.students);
}
```

### Error Handling
```javascript
const response = await fetch('http://localhost:8080/api/v1/students/123', {
  headers: { 'Authorization': `Bearer ${token}` }
});

const data = await response.json();
if (!response.ok) {
  // Handle error
  console.error(`Error ${data.status}: ${data.error}`);
  if (data.status === 401) {
    // Redirect to login
  }
}
```

---

**Last Updated:** November 21, 2025  
**API Version:** v1  
**Base URL:** http://localhost:8080/api/v1

