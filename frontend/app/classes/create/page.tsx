'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import ProtectedRoute from '@/components/ProtectedRoute';
import Navbar from '@/components/Navbar';
import PageHeader from '@/components/PageHeader';
import { Role, CourseClassFormData, CourseClassStatus } from '@/lib/types';
import { courseClassApi, courseApi, employeeApi } from '@/lib/api';

export default function CreateClassPage() {
    const router = useRouter();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [courses, setCourses] = useState<any[]>([]);
    const [lecturers, setLecturers] = useState<any[]>([]);
    const [formData, setFormData] = useState<CourseClassFormData>({
        courseId: '',
        lecturerId: '',
        semester: 'Fall 2025',
        academicYear: 2025,
        maxCapacity: 30,
        status: CourseClassStatus.ACTIVE,
    });

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const [coursesRes, employeesRes] = await Promise.all([
                courseApi.getAll(0, 100),
                employeeApi.getAll(0, 100),
            ]);

            const coursesData =
                (coursesRes.data as any)?.Courses ||
                (coursesRes.data as any)?.courses ||
                [];
            const employeesData =
                (employeesRes.data as any)?.Employees ||
                (employeesRes.data as any)?.employees ||
                [];

            setCourses(coursesData);
            setLecturers(
                employeesData.filter(
                    (e: any) =>
                        e.position === 'LECTURER' || e.position === 'DEAN'
                )
            );
        } catch (err) {
            console.error('Failed to fetch data:', err);
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        try {
            setLoading(true);
            await courseClassApi.create(formData);
            router.push('/classes');
        } catch (err: any) {
            setError(
                err.response?.data?.error ||
                    err.response?.data?.message ||
                    'Failed to create class'
            );
        } finally {
            setLoading(false);
        }
    };

    return (
        <ProtectedRoute allowedRoles={[Role.ADMIN, Role.EMPLOYEE]}>
            <div className="min-h-screen">
                <Navbar />
                <div className="max-w-2xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    <PageHeader
                        title="Create Class"
                        description="Schedule a new class for a course"
                    />

                    {error && (
                        <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg">
                            <p className="text-red-600">{error}</p>
                        </div>
                    )}

                    <form onSubmit={handleSubmit} className="card space-y-6">
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                Course *
                            </label>
                            <select
                                required
                                value={formData.courseId}
                                onChange={(e) =>
                                    setFormData({
                                        ...formData,
                                        courseId: e.target.value,
                                    })
                                }
                                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            >
                                <option value="">Select a course...</option>
                                {courses.map((course) => (
                                    <option key={course.id} value={course.id}>
                                        {course.courseCode} - {course.title}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                Lecturer *
                            </label>
                            <select
                                required
                                value={formData.lecturerId}
                                onChange={(e) =>
                                    setFormData({
                                        ...formData,
                                        lecturerId: e.target.value,
                                    })
                                }
                                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            >
                                <option value="">Select a lecturer...</option>
                                {lecturers.map((lecturer) => (
                                    <option
                                        key={lecturer.id}
                                        value={lecturer.id}
                                    >
                                        {lecturer.person.firstName}{' '}
                                        {lecturer.person.lastName} (
                                        {lecturer.position})
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div className="grid grid-cols-2 gap-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">
                                    Semester *
                                </label>
                                <input
                                    type="text"
                                    required
                                    value={formData.semester}
                                    onChange={(e) =>
                                        setFormData({
                                            ...formData,
                                            semester: e.target.value,
                                        })
                                    }
                                    placeholder="e.g., Fall 2025"
                                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                                />
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-2">
                                    Academic Year *
                                </label>
                                <input
                                    type="number"
                                    required
                                    value={formData.academicYear}
                                    onChange={(e) =>
                                        setFormData({
                                            ...formData,
                                            academicYear: parseInt(
                                                e.target.value
                                            ),
                                        })
                                    }
                                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                                />
                            </div>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                Max Capacity *
                            </label>
                            <input
                                type="number"
                                required
                                min="1"
                                value={formData.maxCapacity}
                                onChange={(e) =>
                                    setFormData({
                                        ...formData,
                                        maxCapacity: parseInt(e.target.value),
                                    })
                                }
                                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            />
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                Status
                            </label>
                            <select
                                value={formData.status}
                                onChange={(e) =>
                                    setFormData({
                                        ...formData,
                                        status: e.target
                                            .value as CourseClassStatus,
                                    })
                                }
                                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            >
                                <option value={CourseClassStatus.ACTIVE}>
                                    Active
                                </option>
                                <option value={CourseClassStatus.CANCELLED}>
                                    Cancelled
                                </option>
                                <option value={CourseClassStatus.COMPLETED}>
                                    Completed
                                </option>
                            </select>
                        </div>

                        <div className="flex justify-end gap-4 pt-6 border-t">
                            <button
                                type="button"
                                onClick={() => router.back()}
                                className="px-4 py-2 text-gray-700 hover:bg-gray-100 rounded-lg transition-colors"
                            >
                                Cancel
                            </button>
                            <button
                                type="submit"
                                disabled={loading}
                                className="btn-primary"
                            >
                                {loading ? 'Creating...' : 'Create Class'}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </ProtectedRoute>
    );
}
