# University Management System - Frontend

A modern, responsive frontend application for managing university operations including students, employees, courses, and enrollments.

## 🎨 Design Theme

-   **Colors**: White and baby blue theme
-   **Style**: Modern, clean, and professional
-   **UI**: No overlapping elements, clear spacing and hierarchy

## ✨ Features

### Authentication

-   User registration and login
-   JWT token-based authentication
-   Automatic token refresh
-   Role-based access control (Admin, Employee, Student)

### Admin Features

-   Manage students (CRUD operations)
-   Manage employees (CRUD operations)
-   Manage courses (CRUD operations)
-   Manage course classes (CRUD operations)
-   View and manage all enrollments
-   Update student grades

### Employee Features

-   View student records
-   View courses and classes
-   Manage enrollments
-   Update grades

### Student Features

-   View available courses
-   View available classes
-   Enroll in classes
-   View personal enrollments
-   Drop enrollments

## 🚀 Getting Started

### Prerequisites

-   Node.js 18+ installed
-   Backend API running on `http://localhost:8080`

### Installation

1. Install dependencies:

```bash
npm install
```

2. Configure environment variables:

```bash
cp .env.example .env.local
```

Edit `.env.local` and set your API base URL:

```
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api/v1
```

3. Run the development server:

```bash
npm run dev
```

4. Open [http://localhost:3000](http://localhost:3000) in your browser

## 📁 Project Structure

```
app/
├── classes/          # Course class management
├── courses/          # Course catalog
├── dashboard/        # Main dashboard
├── employees/        # Employee management
├── enrollments/      # Enrollment management
├── login/           # Login page
├── register/        # Registration page
├── students/        # Student management
├── my-enrollments/  # Student's enrollments
├── unauthorized/    # Access denied page
├── globals.css      # Global styles
├── layout.tsx       # Root layout
└── page.tsx         # Home redirect

components/
├── LoadingSpinner.tsx    # Loading indicator
├── Navbar.tsx           # Navigation bar
├── PageHeader.tsx       # Page header component
├── Pagination.tsx       # Pagination component
└── ProtectedRoute.tsx   # Route protection HOC

contexts/
└── AuthContext.tsx      # Authentication context

lib/
├── api-client.ts        # Axios HTTP client
├── api.ts              # API service functions
└── types.ts            # TypeScript type definitions
```

## 🎯 User Roles

### Admin

-   Full access to all features
-   Can create, read, update, and delete all resources
-   Can manage students, employees, courses, and classes

### Employee

-   Can view students and courses
-   Can manage enrollments
-   Can update student grades

### Student

-   Can view available courses and classes
-   Can enroll in classes
-   Can view and drop their own enrollments
-   Can see their grades

## 🔐 Authentication Flow

1. User registers or logs in
2. JWT token is received and stored in localStorage
3. Token is automatically included in all API requests
4. Token is refreshed when needed
5. User is redirected to login if token expires

## 🎨 Theme Customization

The application uses Tailwind CSS with custom utility classes defined in `globals.css`:

-   `.btn-primary` - Primary action buttons (blue)
-   `.btn-secondary` - Secondary action buttons (white with blue border)
-   `.btn-danger` - Destructive action buttons (red)
-   `.card` - Card containers
-   `.input` - Form inputs
-   `.label` - Form labels
-   `.table-header` - Table header styling
-   `.table-row` - Table row styling

## 📱 Responsive Design

The application is fully responsive and works on:

-   Desktop (1024px+)
-   Tablet (768px - 1023px)
-   Mobile (< 768px)

## 🛠️ Technologies Used

-   **Next.js 16** - React framework
-   **React 19** - UI library
-   **TypeScript** - Type safety
-   **Tailwind CSS** - Styling
-   **Axios** - HTTP client
-   **date-fns** - Date formatting

## 📝 API Integration

The application integrates with the University Management System backend API:

-   Base URL: `http://localhost:8080/api/v1`
-   Authentication: JWT Bearer tokens
-   All responses follow standard API response format

## 🚦 Build and Deployment

### Development Build

```bash
npm run dev
```

### Production Build

```bash
npm run build
npm start
```

### Linting

```bash
npm run lint
```
