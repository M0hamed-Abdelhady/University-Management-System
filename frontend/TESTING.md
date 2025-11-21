# 🧪 Testing Guide

## Manual Testing Checklist

### 1. Authentication Flow

#### Registration

-   [ ] Navigate to `/register`
-   [ ] Fill in all fields correctly
-   [ ] Submit form
-   [ ] Verify redirect to dashboard
-   [ ] Verify token stored in localStorage
-   [ ] Verify user info stored in localStorage

**Test Cases:**

-   ✅ Valid registration
-   ❌ Empty fields
-   ❌ Invalid email format
-   ❌ Password less than 8 characters
-   ❌ Passwords don't match
-   ❌ Duplicate email

#### Login

-   [ ] Navigate to `/login`
-   [ ] Enter valid credentials
-   [ ] Submit form
-   [ ] Verify redirect to dashboard
-   [ ] Verify token stored
-   [ ] Verify user info stored

**Test Cases:**

-   ✅ Valid login
-   ❌ Wrong email
-   ❌ Wrong password
-   ❌ Empty fields
-   ❌ Invalid email format

#### Logout

-   [ ] Click logout button
-   [ ] Verify redirect to login
-   [ ] Verify token removed from localStorage
-   [ ] Verify cannot access protected routes

### 2. Protected Routes

#### Without Authentication

-   [ ] Try accessing `/dashboard` (should redirect to login)
-   [ ] Try accessing `/students` (should redirect to login)
-   [ ] Try accessing `/courses` (should redirect to login)

#### With Authentication

-   [ ] Access `/dashboard` (should load)
-   [ ] Access allowed routes based on role
-   [ ] Try accessing unauthorized routes (should show unauthorized page)

### 3. Student Management (Admin/Employee)

#### View Students List

-   [ ] Navigate to `/students`
-   [ ] Verify students load
-   [ ] Verify pagination works
-   [ ] Verify table displays correctly

**Check:**

-   Student number
-   Full name
-   Email
-   Major
-   Academic year
-   GPA
-   Status badge
-   Action buttons

#### Search/Filter (if implemented)

-   [ ] Search by name
-   [ ] Filter by status
-   [ ] Filter by major
-   [ ] Clear filters

#### Delete Student (Admin only)

-   [ ] Click delete button
-   [ ] Verify confirmation dialog
-   [ ] Confirm deletion
-   [ ] Verify student removed from list

### 4. Course Management

#### View Courses

-   [ ] Navigate to `/courses`
-   [ ] Verify courses load
-   [ ] Verify course cards display correctly
-   [ ] Verify pagination works

**Check:**

-   Course code
-   Title
-   Description
-   Credits
-   Action buttons (if admin)

#### Browse as Student

-   [ ] View courses as student
-   [ ] Verify can't see admin actions
-   [ ] Verify cards are clickable

### 5. Class Management

#### View Classes

-   [ ] Navigate to `/classes`
-   [ ] Verify classes load
-   [ ] Verify class information displays

**Check:**

-   Course code
-   Course title
-   Lecturer name
-   Semester
-   Academic year
-   Current/Max capacity
-   Status badge
-   Enroll button (if student)

#### Enroll in Class (Student)

-   [ ] Click "Enroll" button
-   [ ] Verify confirmation dialog
-   [ ] Confirm enrollment
-   [ ] Verify success message
-   [ ] Verify capacity updated

**Test Cases:**

-   ✅ Successful enrollment
-   ❌ Class full
-   ❌ Already enrolled
-   ❌ Class inactive

### 6. Student Enrollments

#### View My Enrollments (Student)

-   [ ] Navigate to `/my-enrollments`
-   [ ] Verify enrollments load
-   [ ] Verify enrollment details display

**Check:**

-   Course code
-   Course title
-   Lecturer
-   Semester
-   Year
-   Credits
-   Grade (if available)
-   Status badge
-   Drop button (if enrolled)

#### Drop Enrollment

-   [ ] Click "Drop" button
-   [ ] Verify confirmation dialog
-   [ ] Confirm drop
-   [ ] Verify enrollment removed
-   [ ] Verify class capacity updated

### 7. Dashboard

#### Role-Based Dashboard

-   [ ] Login as Admin
    -   [ ] Verify all admin cards visible
    -   [ ] Verify all links work
-   [ ] Login as Employee
    -   [ ] Verify employee cards visible
    -   [ ] Verify correct permissions
-   [ ] Login as Student
    -   [ ] Verify student cards visible
    -   [ ] Verify student-only features

### 8. Navigation

#### Navbar

-   [ ] Verify logo redirects to dashboard
-   [ ] Verify all nav links work
-   [ ] Verify role-based links display correctly
-   [ ] Verify user name displays
-   [ ] Verify logout button works

**Test on:**

-   Desktop
-   Tablet
-   Mobile

### 9. UI Components

#### Loading States

-   [ ] Verify loading spinner appears during API calls
-   [ ] Verify loading message displays
-   [ ] Verify spinner disappears when data loads

#### Error Messages

-   [ ] Trigger API error (disconnect backend)
-   [ ] Verify error message displays
-   [ ] Verify error message is readable
-   [ ] Verify error doesn't break UI

#### Empty States

-   [ ] View page with no data
-   [ ] Verify "No items found" message
-   [ ] Verify empty state is centered

#### Pagination

-   [ ] Navigate to next page
-   [ ] Navigate to previous page
-   [ ] Jump to specific page
-   [ ] Verify page numbers update
-   [ ] Verify data changes

### 10. Responsive Design

#### Desktop (1024px+)

-   [ ] Navbar displays fully
-   [ ] Multi-column grids work
-   [ ] Tables display properly
-   [ ] Cards are well-spaced

#### Tablet (768px-1023px)

-   [ ] Layout adjusts appropriately
-   [ ] 2-column grids work
-   [ ] Tables scroll horizontally
-   [ ] Touch targets are adequate

#### Mobile (<768px)

-   [ ] Single column layout
-   [ ] Cards stack properly
-   [ ] Forms are usable
-   [ ] Buttons are tap-friendly
-   [ ] Text is readable

### 11. Forms (if implemented)

#### Form Validation

-   [ ] Submit empty form
-   [ ] Enter invalid email
-   [ ] Enter short password
-   [ ] Verify validation messages

#### Form Submission

-   [ ] Fill form correctly
-   [ ] Submit form
-   [ ] Verify loading state
-   [ ] Verify success message
-   [ ] Verify redirect

### 12. Token Management

#### Token Expiration

-   [ ] Wait for token to expire
-   [ ] Make API call
-   [ ] Verify auto-logout
-   [ ] Verify redirect to login

#### Token Refresh (if implemented)

-   [ ] Make API call near expiration
-   [ ] Verify token refreshes
-   [ ] Verify no logout

## Browser Testing

Test on multiple browsers:

### Chrome

-   [ ] Latest version
-   [ ] One version back

### Firefox

-   [ ] Latest version

### Safari

-   [ ] Latest version (Mac/iOS)

### Edge

-   [ ] Latest version

## Performance Testing

### Page Load Times

-   [ ] Dashboard loads < 2s
-   [ ] Student list loads < 3s
-   [ ] Course list loads < 2s

### API Response Times

-   [ ] Login < 1s
-   [ ] Data fetch < 2s
-   [ ] Create/Update < 1s

### Resource Usage

-   [ ] Network requests are minimal
-   [ ] No memory leaks
-   [ ] Smooth scrolling
-   [ ] No layout shifts

## Accessibility Testing

### Keyboard Navigation

-   [ ] Tab through forms
-   [ ] Enter submits forms
-   [ ] Escape closes dialogs
-   [ ] Arrow keys work where expected

### Screen Reader (if available)

-   [ ] Labels are announced
-   [ ] Buttons are labeled
-   [ ] Error messages are read
-   [ ] Status changes are announced

### Color Contrast

-   [ ] Text is readable
-   [ ] Links are distinguishable
-   [ ] Buttons have good contrast

## Security Testing

### XSS Prevention

-   [ ] Enter `<script>alert('xss')</script>` in forms
-   [ ] Verify it's escaped/sanitized

### CSRF Prevention

-   [ ] Verify token in requests
-   [ ] Verify origin checks

### Authentication

-   [ ] Cannot access protected routes without token
-   [ ] Token expires appropriately
-   [ ] Password is not visible

## Edge Cases

### Network Issues

-   [ ] Disconnect internet
-   [ ] Verify error messages
-   [ ] Reconnect internet
-   [ ] Verify recovery

### Invalid Data

-   [ ] API returns unexpected data
-   [ ] Verify graceful handling
-   [ ] No crashes

### Concurrent Actions

-   [ ] Open multiple tabs
-   [ ] Perform actions in each
-   [ ] Verify data consistency

## Test Data

### Create Test Users

```
Admin:
- Email: admin@test.com
- Password: admin12345

Employee:
- Email: employee@test.com
- Password: employee12345

Student:
- Email: student@test.com
- Password: student12345
```

### Test Scenarios

#### Scenario 1: New Student Enrollment

1. Login as student
2. Browse courses
3. View available classes
4. Enroll in 3 classes
5. View my enrollments
6. Drop 1 class

#### Scenario 2: Admin Management

1. Login as admin
2. Create new student
3. Create new course
4. Create new class
5. View enrollments
6. Update grades

#### Scenario 3: Employee Tasks

1. Login as employee
2. View student list
3. View specific student
4. View enrollments
5. Update student grade

## Regression Testing

After any code changes, retest:

-   [ ] Authentication flow
-   [ ] Main user flows
-   [ ] Critical features
-   [ ] Previously fixed bugs

## Bug Reporting Template

When you find a bug, report it with:

```markdown
**Title:** Brief description

**Severity:** Critical / High / Medium / Low

**Steps to Reproduce:**

1. Step 1
2. Step 2
3. Step 3

**Expected Result:**
What should happen

**Actual Result:**
What actually happened

**Environment:**

-   Browser: Chrome 120
-   OS: Windows 11
-   Screen size: 1920x1080

**Screenshots:**
Attach screenshots if applicable

**Console Errors:**
Any error messages in browser console
```

## Testing Tools (Optional)

### Manual Testing

-   Browser DevTools
-   React Developer Tools
-   Network tab monitoring

### Automated Testing (Future)

-   Jest for unit tests
-   React Testing Library
-   Cypress for E2E tests
-   Playwright for E2E tests

## Test Coverage Goals

-   ✅ All user flows tested
-   ✅ All pages accessible
-   ✅ All forms validated
-   ✅ Error handling tested
-   ✅ Responsive design verified
-   ✅ Cross-browser tested

## Sign Off

Once all tests pass:

-   [ ] All critical features work
-   [ ] No blocking bugs
-   [ ] Performance acceptable
-   [ ] Security verified
-   [ ] Accessibility checked
-   [ ] Documentation complete

**Tested by:** ******\_\_\_******
**Date:** ******\_\_\_******
**Sign off:** ******\_\_\_******
