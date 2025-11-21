'use client';

import { useState, useEffect } from 'react';
import { useRouter, useParams } from 'next/navigation';
import ProtectedRoute from '@/components/ProtectedRoute';
import Navbar from '@/components/Navbar';
import PageHeader from '@/components/PageHeader';
import LoadingSpinner from '@/components/LoadingSpinner';
import { Role, CourseClass } from '@/lib/types';
import { courseClassApi } from '@/lib/api';
import { useAuth } from '@/contexts/AuthContext';

export default function ClassDetailPage() {
    const router = useRouter();
    const params = useParams();
    const id = params.id as string;
    const [courseClass, setCourseClass] = useState<CourseClass | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const { hasRole } = useAuth();

    useEffect(() => {
        fetchClass();
    }, [id]);

    const fetchClass = async () => {
        try {
            setLoading(true);
            const response = await courseClassApi.getById(id);
            const classData =
                (response.data as any)?.CourseClass ||
                (response.data as any)?.Class ||
                (response.data as any)?.courseClass ||
                (response.data as any)?.class;
            setCourseClass(classData);
        } catch (err: any) {
            setError(
                err.response?.data?.error || 'Failed to fetch class details'
            );
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <ProtectedRoute>
                <div className="min-h-screen">
                    <Navbar />
                    <LoadingSpinner />
                </div>
            </ProtectedRoute>
        );
    }

    if (error || !courseClass) {
        return (
            <ProtectedRoute>
                <div className="min-h-screen">
                    <Navbar />
                    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                        <div className="p-4 bg-red-50 border border-red-200 rounded-lg">
                            <p className="text-red-600">
                                {error || 'Class not found'}
                            </p>
                        </div>
                    </div>
                </div>
            </ProtectedRoute>
        );
    }

    return (
        <ProtectedRoute>
            <div className="min-h-screen">
                <Navbar />
                <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    <PageHeader
                        title="Class Details"
                        description={`${courseClass.course.courseCode} - ${courseClass.semester} ${courseClass.academicYear}`}
                        action={
                            hasRole(Role.ADMIN) && (
                                <button
                                    onClick={() =>
                                        router.push(`/classes/${id}/edit`)
                                    }
                                    className="btn-primary"
                                >
                                    Edit Class
                                </button>
                            )
                        }
                    />

                    <div className="card space-y-6">
                        <div className="pb-4 border-b">
                            <h2 className="text-xl font-bold text-gray-900">
                                {courseClass.course.title}
                            </h2>
                            <p className="text-sm text-gray-500 mt-1">
                                {courseClass.course.courseCode}
                            </p>
                        </div>

                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Course Code
                                </h3>
                                <p className="text-base text-gray-900">
                                    {courseClass.course.courseCode}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Credits
                                </h3>
                                <p className="text-base text-gray-900">
                                    {courseClass.course.credits}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Lecturer
                                </h3>
                                <p className="text-base text-gray-900">
                                    {courseClass.lecturer.person.firstName}{' '}
                                    {courseClass.lecturer.person.lastName}
                                </p>
                                <p className="text-xs text-gray-500">
                                    {courseClass.lecturer.position}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Semester
                                </h3>
                                <p className="text-base text-gray-900">
                                    {courseClass.semester}{' '}
                                    {courseClass.academicYear}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Capacity
                                </h3>
                                <p className="text-base text-gray-900">
                                    {courseClass.currentCapacity} /{' '}
                                    {courseClass.maxCapacity}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Status
                                </h3>
                                <span
                                    className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                                        courseClass.status === 'ACTIVE'
                                            ? 'bg-green-100 text-green-800'
                                            : courseClass.status === 'COMPLETED'
                                            ? 'bg-blue-100 text-blue-800'
                                            : 'bg-gray-100 text-gray-800'
                                    }`}
                                >
                                    {courseClass.status}
                                </span>
                            </div>

                            <div className="md:col-span-2">
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Course Description
                                </h3>
                                <p className="text-base text-gray-900">
                                    {courseClass.course.description ||
                                        'No description available'}
                                </p>
                            </div>
                        </div>

                        <div className="flex justify-end gap-4 pt-6 border-t">
                            <button
                                onClick={() => router.back()}
                                className="px-4 py-2 text-gray-700 hover:bg-gray-100 rounded-lg transition-colors"
                            >
                                Back
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </ProtectedRoute>
    );
}
