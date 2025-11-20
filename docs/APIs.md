# University Management System - API Endpoints

## Authentication & Authorization

### Auth
```
POST   /api/auth/login
POST   /api/auth/register (only register as a student)
POST   /api/auth/refresh
GET    /api/auth/me
PUT    /api/auth/me
```

---

## Student Management

### Students
```
GET    /api/students
GET    /api/students/{id}
POST   /api/students
PUT    /api/students/{id}
DELETE /api/students/{id}
GET    /api/students/{id}/enrollments
GET    /api/students/{id}/courses
GET    /api/students/{id}/transcript
```

### Query Parameters
```
?page=1&limit=20
?major=Computer Science
?academic_year=3
?status=ACTIVE
?gpa_min=3.0&gpa_max=4.0
?search=john
```

---

## Employee Management

### Employees
```
GET    /api/employees
GET    /api/employees/{id}
POST   /api/employees
PUT    /api/employees/{id}
DELETE /api/employees/{id}
```

### Query Parameters
```
?page=1&limit=20
?position=LECTURER
?status=ACTIVE
?search=jane
```

---

## Course Management

### Courses
```
GET    /api/courses
GET    /api/courses/{id}
POST   /api/courses
PUT    /api/courses/{id}
DELETE /api/courses/{id}
GET    /api/courses/{id}/classes
GET    /api/courses/{id}/lecturers
POST   /api/courses/{id}/lecturers
DELETE /api/courses/{id}/lecturers/{id}
```

### Query Parameters
```
?page=1&limit=20
?search=introduction
?credits=3
?course_code=CS101
```

---

## Course Class Management

### Course Classes
```
GET    /api/classes
GET    /api/classes/{id}
POST   /api/classes
PUT    /api/classes/{id}
DELETE /api/classes/{id}
GET    /api/classes/{id}/lecturers

GET    /api/classes/{id}/tas
POST   /api/classes/{id}/tas
DELETE /api/classes/{id}/tas/{taId}
GET    /api/classes/{id}/enrollments
GET    /api/classes/{id}/students
GET    /api/classes/{id}/capacity
```

### Query Parameters
```
?page=1&limit=20
?semester=Fall 2024
?year=2024
?status=ACTIVE
?course_id=course_123
?instructor_id=instructor_456
```

---

## Enrollment Management

### Enrollments
```
GET    /api/enrollments
GET    /api/enrollments/{id}
POST   /api/enrollments
PUT    /api/enrollments/{id}
DELETE /api/enrollments/{id}
POST   /api/enrollments/bulk
```

### Special Enrollment Endpoints
```
POST   /api/students/{studentId}/enroll
POST   /api/students/{studentId}/drop/{enrollmentId}
PUT    /api/enrollments/{id}/grade
```

### Query Parameters
```
?page=1&limit=20
?student_id=student_123
?class_id=class_456
?status=ENROLLED
?semester=Fall 2024
?grade=A
```

---

## Pagination Response Format

```json
{
  "success": true,
  "data": [...],
  "pagination": {
    "current_page": 1,
    "total_pages": 5,
    "total_items": 95,
    "items_per_page": 20,
    "has_next": true,
    "has_prev": false
  }
}
```