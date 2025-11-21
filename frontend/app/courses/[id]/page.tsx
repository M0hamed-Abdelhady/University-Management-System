'use client';

import { useState, useEffect } from 'react';
import { useRouter, useParams } from 'next/navigation';
import ProtectedRoute from '@/components/ProtectedRoute';
import Navbar from '@/components/Navbar';
import PageHeader from '@/components/PageHeader';
import LoadingSpinner from '@/components/LoadingSpinner';
import { Role, Course } from '@/lib/types';
import { courseApi } from '@/lib/api';
import { useAuth } from '@/contexts/AuthContext';

export default function CourseDetailPage() {
    const router = useRouter();
    const params = useParams();
    const id = params.id as string;
    const [course, setCourse] = useState<Course | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const { hasRole } = useAuth();

    useEffect(() => {
        fetchCourse();
    }, [id]);

    const fetchCourse = async () => {
        try {
            setLoading(true);
            const response = await courseApi.getById(id);
            const courseData =
                (response.data as any)?.Course ||
                (response.data as any)?.course;
            setCourse(courseData);
        } catch (err: any) {
            setError(
                err.response?.data?.error || 'Failed to fetch course details'
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

    if (error || !course) {
        return (
            <ProtectedRoute>
                <div className="min-h-screen">
                    <Navbar />
                    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                        <div className="p-4 bg-red-50 border border-red-200 rounded-lg">
                            <p className="text-red-600">
                                {error || 'Course not found'}
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
                        title="Course Details"
                        description={course.courseCode}
                        action={
                            hasRole(Role.ADMIN) && (
                                <button
                                    onClick={() =>
                                        router.push(`/courses/${id}/edit`)
                                    }
                                    className="btn-primary"
                                >
                                    Edit Course
                                </button>
                            )
                        }
                    />

                    <div className="card space-y-6">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Course Code
                                </h3>
                                <p className="text-base text-gray-900">
                                    {course.courseCode}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Credits
                                </h3>
                                <p className="text-base text-gray-900">
                                    {course.credits}
                                </p>
                            </div>

                            <div className="md:col-span-2">
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Title
                                </h3>
                                <p className="text-base text-gray-900">
                                    {course.title}
                                </p>
                            </div>

                            <div className="md:col-span-2">
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Description
                                </h3>
                                <p className="text-base text-gray-900">
                                    {course.description ||
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
