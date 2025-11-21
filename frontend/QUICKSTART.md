# Quick Start Guide

## 🚀 Running the Application

1. **Start the backend API** (ensure it's running on http://localhost:8080)

2. **Install dependencies:**

    ```bash
    npm install
    ```

3. **Start the development server:**

    ```bash
    npm run dev
    ```

4. **Open your browser:**
   Navigate to [http://localhost:3000](http://localhost:3000)

## 👤 Default Test Accounts

After registering, you'll have a STUDENT role by default. For testing different roles, you can:

1. **Register as a new user** at http://localhost:3000/register
2. **Login** at http://localhost:3000/login

## 🎯 Features by Role

### Student Users Can:

-   ✅ View available courses
-   ✅ View available classes
-   ✅ Enroll in classes
-   ✅ View their enrollments
-   ✅ Drop enrollments
-   ✅ See their grades

### Employee Users Can:

-   ✅ View student records
-   ✅ View courses and classes
-   ✅ Manage enrollments
-   ✅ Update student grades

### Admin Users Can:

-   ✅ Full CRUD operations on students
-   ✅ Full CRUD operations on employees
-   ✅ Full CRUD operations on courses
-   ✅ Full CRUD operations on classes
-   ✅ Manage all enrollments
-   ✅ Update grades

## 📱 Main Pages

-   **/** - Redirects to dashboard or login
-   **/login** - User login
-   **/register** - New user registration
-   **/dashboard** - Role-based dashboard
-   **/students** - Student management (Admin/Employee)
-   **/employees** - Employee management (Admin)
-   **/courses** - Course catalog (All users)
-   **/classes** - Class schedules (All users)
-   **/my-enrollments** - Student's enrollments (Students)
-   **/enrollments** - All enrollments (Admin/Employee)

## 🎨 UI Theme

-   **Primary Color:** Baby Blue (#93C5FD / blue-300)
-   **Accent Color:** Blue (#60A5FA / blue-400)
-   **Background:** White to Blue gradient
-   **Cards:** White with blue borders
-   **Buttons:** Blue with hover effects

## 🔧 Configuration

Edit `.env.local` to change the API URL:

```env
NEXT_PUBLIC_API_BASE_URL=http://localhost:8080/api/v1
```

## ⚠️ Common Issues

### Port Already in Use

If port 3000 is already in use:

```bash
npm run dev -- -p 3001
```

### API Connection Issues

-   Ensure backend is running on http://localhost:8080
-   Check CORS settings on backend
-   Verify `.env.local` has correct API URL

### Login Issues

-   Ensure you're using valid email format
-   Password must be at least 8 characters
-   Check backend API is responding

## 📝 Development Tips

1. **Hot Reload:** The app automatically reloads when you save files
2. **TypeScript:** Use TypeScript for type safety
3. **Tailwind:** Use existing utility classes from `globals.css`
4. **API Errors:** Check browser console for detailed error messages

## 🧪 Testing

Test different roles:

1. Register multiple accounts
2. Use backend admin tools to change user roles
3. Test role-specific features

## 📚 Learn More

-   Check `README.md` for detailed documentation
-   Review `lib/types.ts` for data structures
-   See `lib/api.ts` for API endpoints
-   Look at existing pages for code examples

## 🆘 Need Help?

-   Check browser console for errors
-   Verify API is responding: http://localhost:8080/api/v1
-   Review network tab for failed requests
-   Check token in localStorage
