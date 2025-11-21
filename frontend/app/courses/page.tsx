'use client';

import { useState, useEffect } from 'react';
import ProtectedRoute from '@/components/ProtectedRoute';
import Navbar from '@/components/Navbar';
import PageHeader from '@/components/PageHeader';
import LoadingSpinner from '@/components/LoadingSpinner';
import Pagination from '@/components/Pagination';
import { Role, Course } from '@/lib/types';
import { courseApi } from '@/lib/api';
import Link from 'next/link';
import { useAuth } from '@/contexts/AuthContext';

export default function CoursesPage() {
    const [courses, setCourses] = useState<Course[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const { hasRole } = useAuth();

    useEffect(() => {
        fetchCourses(currentPage);
    }, [currentPage]);

    const fetchCourses = async (page: number) => {
        try {
            setLoading(true);
            const response = await courseApi.getAll(page, 10);
            console.log('Courses API response:', response);
            // Handle both lowercase 'courses' and capitalized 'Courses'
            const coursesData =
                (response.data as any)?.Courses ||
                (response.data as any)?.courses ||
                [];
            const paginationData = (response.data as any)?.Pagination ||
                (response.data as any)?.pagination || {
                    totalPages: 0,
                };
            setCourses(coursesData);
            setTotalPages(paginationData.totalPages);
        } catch (err: any) {
            console.error('Courses fetch error:', err);
            setError(err.response?.data?.error || 'Failed to fetch courses');
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id: string) => {
        if (!confirm('Are you sure you want to delete this course?')) return;

        try {
            await courseApi.delete(id);
            fetchCourses(currentPage);
        } catch (err: any) {
            alert(err.response?.data?.error || 'Failed to delete course');
        }
    };

    return (
        <ProtectedRoute>
            <div className="min-h-screen">
                <Navbar />
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    <PageHeader
                        title="Courses"
                        description="Browse and manage courses"
                        action={
                            hasRole(Role.ADMIN) ? (
                                <Link
                                    href="/courses/create"
                                    className="btn-primary"
                                >
                                    + Add Course
                                </Link>
                            ) : undefined
                        }
                    />

                    {error && (
                        <div className="mb-6 p-4 bg-red-50 border border-red-200 rounded-lg">
                            <p className="text-red-600">{error}</p>
                        </div>
                    )}

                    {loading ? (
                        <LoadingSpinner />
                    ) : (
                        <>
                            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                                {courses.map((course) => (
                                    <div key={course.id} className="card">
                                        <div className="flex justify-between items-start mb-4">
                                            <span className="px-3 py-1 bg-blue-100 text-blue-700 rounded-full text-xs font-medium">
                                                {course.courseCode}
                                            </span>
                                            <span className="text-sm text-gray-500">
                                                {course.credits} Credits
                                            </span>
                                        </div>
                                        <h3 className="text-lg font-semibold text-gray-900 mb-2">
                                            {course.title}
                                        </h3>
                                        <p className="text-gray-600 text-sm mb-4 line-clamp-3">
                                            {course.description ||
                                                'No description available'}
                                        </p>
                                        <div className="flex space-x-2">
                                            <Link
                                                href={`/courses/${course.id}`}
                                                className="flex-1 text-center btn-secondary text-sm py-2"
                                            >
                                                View Details
                                            </Link>
                                            {hasRole(Role.ADMIN) && (
                                                <>
                                                    <Link
                                                        href={`/courses/${course.id}/edit`}
                                                        className="px-4 py-2 text-blue-500 hover:bg-blue-50 rounded-lg text-sm font-medium border border-blue-200"
                                                    >
                                                        Edit
                                                    </Link>
                                                    <button
                                                        onClick={() =>
                                                            handleDelete(
                                                                course.id
                                                            )
                                                        }
                                                        className="px-4 py-2 text-red-500 hover:bg-red-50 rounded-lg text-sm font-medium border border-red-200"
                                                    >
                                                        Delete
                                                    </button>
                                                </>
                                            )}
                                        </div>
                                    </div>
                                ))}
                            </div>

                            {courses.length === 0 && (
                                <div className="text-center py-12">
                                    <p className="text-gray-500">
                                        No courses found
                                    </p>
                                </div>
                            )}

                            <Pagination
                                currentPage={currentPage}
                                totalPages={totalPages}
                                onPageChange={setCurrentPage}
                            />
                        </>
                    )}
                </div>
            </div>
        </ProtectedRoute>
    );
}
