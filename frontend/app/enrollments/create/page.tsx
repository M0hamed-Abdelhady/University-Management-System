'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import ProtectedRoute from '@/components/ProtectedRoute';
import Navbar from '@/components/Navbar';
import PageHeader from '@/components/PageHeader';
import { Role, EnrollmentFormData, EnrollmentStatus } from '@/lib/types';
import { enrollmentApi, studentApi, courseClassApi } from '@/lib/api';

export default function CreateEnrollmentPage() {
    const router = useRouter();
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [students, setStudents] = useState<any[]>([]);
    const [classes, setClasses] = useState<any[]>([]);
    const [formData, setFormData] = useState<EnrollmentFormData>({
        studentId: '',
        classId: '',
        status: EnrollmentStatus.ENROLLED,
    });

    useEffect(() => {
        fetchData();
    }, []);

    const fetchData = async () => {
        try {
            const [studentsRes, classesRes] = await Promise.all([
                studentApi.getAll(0, 100),
                courseClassApi.getAll(0, 100),
            ]);

            const studentsData =
                (studentsRes.data as any)?.Students ||
                (studentsRes.data as any)?.students ||
                [];
            const classesData =
                (classesRes.data as any)?.Classes ||
                (classesRes.data as any)?.classes ||
                [];

            setStudents(studentsData);
            setClasses(classesData.filter((c: any) => c.status === 'ACTIVE'));
        } catch (err) {
            console.error('Failed to fetch data:', err);
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setError('');

        try {
            setLoading(true);
            await enrollmentApi.create(formData);
            router.push('/enrollments');
        } catch (err: any) {
            setError(
                err.response?.data?.error ||
                    err.response?.data?.message ||
                    'Failed to create enrollment'
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
                        title="Create Enrollment"
                        description="Enroll a student in a class"
                    />

                    {error && (
                        <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg">
                            <p className="text-red-600">{error}</p>
                        </div>
                    )}

                    <form onSubmit={handleSubmit} className="card space-y-6">
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                Student *
                            </label>
                            <select
                                required
                                value={formData.studentId}
                                onChange={(e) =>
                                    setFormData({
                                        ...formData,
                                        studentId: e.target.value,
                                    })
                                }
                                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            >
                                <option value="">Select a student...</option>
                                {students.map((student) => (
                                    <option key={student.id} value={student.id}>
                                        {student.studentNumber} -{' '}
                                        {student.person.firstName}{' '}
                                        {student.person.lastName}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                Class *
                            </label>
                            <select
                                required
                                value={formData.classId}
                                onChange={(e) =>
                                    setFormData({
                                        ...formData,
                                        classId: e.target.value,
                                    })
                                }
                                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            >
                                <option value="">Select a class...</option>
                                {classes.map((cls) => (
                                    <option key={cls.id} value={cls.id}>
                                        {cls.course.courseCode} -{' '}
                                        {cls.course.title} ({cls.semester}{' '}
                                        {cls.academicYear})
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                Status *
                            </label>
                            <select
                                required
                                value={formData.status}
                                onChange={(e) =>
                                    setFormData({
                                        ...formData,
                                        status: e.target
                                            .value as EnrollmentStatus,
                                    })
                                }
                                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            >
                                <option value={EnrollmentStatus.ENROLLED}>
                                    Enrolled
                                </option>
                                <option value={EnrollmentStatus.COMPLETED}>
                                    Completed
                                </option>
                                <option value={EnrollmentStatus.DROPPED}>
                                    Dropped
                                </option>
                                <option value={EnrollmentStatus.WITHDRAWN}>
                                    Withdrawn
                                </option>
                            </select>
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-2">
                                Grade (optional)
                            </label>
                            <input
                                type="text"
                                value={formData.grade || ''}
                                onChange={(e) =>
                                    setFormData({
                                        ...formData,
                                        grade: e.target.value,
                                    })
                                }
                                placeholder="e.g., A, B"
                                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                            />
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
                                {loading ? 'Creating...' : 'Create Enrollment'}
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </ProtectedRoute>
    );
}
