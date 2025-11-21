# Visual Design Guide

## 🎨 Color Palette

### Primary Colors

-   **White:** `#FFFFFF` - Main background
-   **Baby Blue (Light):** `#E0F2FE` - Background gradients
-   **Baby Blue:** `#93C5FD` - Accents and borders (blue-300)
-   **Blue:** `#60A5FA` - Primary buttons and links (blue-400)
-   **Dark Blue:** `#3B82F6` - Hover states (blue-500)

### Secondary Colors

-   **Gray 50:** `#F9FAFB` - Subtle backgrounds
-   **Gray 100:** `#F3F4F6` - Borders
-   **Gray 200:** `#E5E7EB` - Dividers
-   **Gray 600:** `#4B5563` - Body text
-   **Gray 700:** `#374151` - Labels
-   **Gray 900:** `#111827` - Headings

### Status Colors

-   **Green:** `#10B981` - Active/Success
-   **Red:** `#EF4444` - Error/Delete
-   **Yellow:** `#F59E0B` - Warning/Grades

## 📐 Layout Structure

### Page Layout

```
┌─────────────────────────────────────────────┐
│              Navbar (White)                  │
│  Logo | Dashboard | Students | ...  | User  │
└─────────────────────────────────────────────┘
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │  Page Header                           │ │
│  │  Title + Description        [+ Button] │ │
│  └────────────────────────────────────────┘ │
│                                              │
│  ┌────────────────────────────────────────┐ │
│  │                                        │ │
│  │         Content Area                   │ │
│  │         (Cards/Tables)                 │ │
│  │                                        │ │
│  └────────────────────────────────────────┘ │
│                                              │
│         [Pagination if needed]               │
│                                              │
└─────────────────────────────────────────────┘
```

## 🧩 Component Styles

### Buttons

**Primary Button**

```
Background: Blue (#60A5FA)
Text: White
Padding: 10px 24px
Border Radius: 8px
Shadow: Small
Hover: Darker blue (#3B82F6) + shadow
```

**Secondary Button**

```
Background: White
Text: Blue (#60A5FA)
Border: 1px Blue (#93C5FD)
Padding: 10px 24px
Border Radius: 8px
Hover: Light blue background (#F0F9FF)
```

**Danger Button**

```
Background: Red (#EF4444)
Text: White
Padding: 10px 24px
Border Radius: 8px
Hover: Darker red (#DC2626)
```

### Cards

```
┌──────────────────────────────────┐
│  Card Header                     │
│  ┌────────────────────────────┐  │
│  │                            │  │
│  │  Card Content              │  │
│  │                            │  │
│  └────────────────────────────┘  │
│  [Action Buttons]                │
└──────────────────────────────────┘

Background: White
Border: 1px Blue-100 (#DBEAFE)
Border Radius: 12px
Padding: 24px
Shadow: Medium
```

### Tables

```
┌───────────────────────────────────────┐
│ Header Row (Baby Blue background)     │
├───────────────────────────────────────┤
│ Data Row 1 (White)                    │
├───────────────────────────────────────┤
│ Data Row 2 (Hover: Light blue)        │
├───────────────────────────────────────┤
│ Data Row 3 (White)                    │
└───────────────────────────────────────┘
```

### Forms

**Input Fields**

```
┌─────────────────────────────────┐
│ Label (Gray-700, font-medium)   │
│ ┌─────────────────────────────┐ │
│ │ Input field                 │ │
│ │ Border: Gray-300            │ │
│ │ Focus: Blue ring            │ │
│ └─────────────────────────────┘ │
└─────────────────────────────────┘
```

### Status Badges

**Active** (Green)

```
┌────────┐
│ ACTIVE │  Green background, darker green text
└────────┘
```

**Inactive/Suspended** (Red)

```
┌──────────┐
│ SUSPENDED│  Red background, darker red text
└──────────┘
```

**Neutral** (Blue)

```
┌───────────┐
│ GRADUATED │  Blue background, darker blue text
└───────────┘
```

## 📱 Responsive Breakpoints

### Desktop (1024px+)

-   Full navbar with all links
-   Multi-column grids (2-3 columns)
-   Wide tables
-   Side-by-side layouts

### Tablet (768px - 1023px)

-   Condensed navbar
-   2-column grids
-   Horizontal scroll for tables
-   Stacked layouts

### Mobile (< 768px)

-   Hamburger menu (if implemented)
-   Single column grids
-   Horizontal scroll for tables
-   Full-width cards
-   Stacked forms

## 🎯 Design Principles

### 1. White Space

-   Generous padding: 24px (6) for cards
-   Consistent gaps: 16px (4) or 24px (6)
-   No cramped elements

### 2. Hierarchy

-   Large headings (3xl) for page titles
-   Medium headings (xl-2xl) for sections
-   Clear visual separation

### 3. Consistency

-   Same border radius (8px-12px) everywhere
-   Consistent button sizes
-   Uniform spacing

### 4. Accessibility

-   Good contrast ratios
-   Clear hover states
-   Focus indicators
-   Readable font sizes (14px-16px base)

### 5. Feedback

-   Loading spinners for async operations
-   Success/error messages
-   Hover effects on interactive elements
-   Disabled states for buttons

## 🖼️ Page Previews

### Login Page

```
         University Management
              Sign In

┌────────────────────────────────┐
│ Email Address                  │
│ ┌────────────────────────────┐ │
│ │                            │ │
│ └────────────────────────────┘ │
│                                │
│ Password                       │
│ ┌────────────────────────────┐ │
│ │                            │ │
│ └────────────────────────────┘ │
│                                │
│ ┌────────────────────────────┐ │
│ │       Sign In             │ │
│ └────────────────────────────┘ │
│                                │
│ Don't have an account? Sign up │
└────────────────────────────────┘
```

### Dashboard

```
Welcome back, John!
Manage your university operations from here

┌──────────┐ ┌──────────┐ ┌──────────┐
│   👨‍🎓     │ │   👨‍💼     │ │   📚     │
│ Students │ │Employees │ │ Courses  │
│   ...    │ │   ...    │ │   ...    │
└──────────┘ └──────────┘ └──────────┘

┌──────────┐ ┌──────────┐
│   🏫     │ │   📝     │
│ Classes  │ │Enrollment│
│   ...    │ │   ...    │
└──────────┘ └──────────┘
```

### Students List

```
Students                           [+ Add Student]
Manage student records

┌─────────────────────────────────────────────┐
│ No. | Name      | Email    | Major | Year  │
├─────────────────────────────────────────────┤
│ 001 | John Doe  | j@u.edu  | CS    | 2     │
│ 002 | Jane Smith| js@u.edu | Bus   | 1     │
└─────────────────────────────────────────────┘

         [< Previous] 1 2 3 [Next >]
```

### Course Cards

```
┌─────────────────────────────┐ ┌─────────────────────────────┐
│ CS101           3 Credits   │ │ MATH201         4 Credits   │
│                             │ │                             │
│ Intro to Computer Science  │ │ Calculus II                 │
│ Basic concepts of...        │ │ Advanced calculus...        │
│                             │ │                             │
│ [View Details] [Edit] [Del] │ │ [View Details] [Edit] [Del] │
└─────────────────────────────┘ └─────────────────────────────┘
```

## 💫 Animations & Transitions

-   **Hover:** 200ms ease transition
-   **Page transitions:** Smooth fade
-   **Loading spinner:** Rotation animation
-   **Button press:** Scale down slightly
-   **Card hover:** Lift with shadow increase

## 🎨 Typography

-   **Headings:** Bold, Gray-900
-   **Body:** Regular, Gray-600
-   **Labels:** Medium, Gray-700
-   **Links:** Medium, Blue-500
-   **Small text:** 12px-14px, Gray-500

## 📏 Spacing Scale

-   **xs:** 4px (1)
-   **sm:** 8px (2)
-   **md:** 16px (4)
-   **lg:** 24px (6)
-   **xl:** 32px (8)
-   **2xl:** 48px (12)
