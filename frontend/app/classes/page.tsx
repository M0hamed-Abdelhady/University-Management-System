'use client';

import { useState, useEffect } from 'react';
import ProtectedRoute from '@/components/ProtectedRoute';
import Navbar from '@/components/Navbar';
import PageHeader from '@/components/PageHeader';
import LoadingSpinner from '@/components/LoadingSpinner';
import Pagination from '@/components/Pagination';
import { Role, CourseClass } from '@/lib/types';
import { courseClassApi, studentApi } from '@/lib/api';
import Link from 'next/link';
import { useAuth } from '@/contexts/AuthContext';

export default function ClassesPage() {
    const [classes, setClasses] = useState<CourseClass[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const { hasRole, user } = useAuth();

    useEffect(() => {
        fetchClasses(currentPage);
    }, [currentPage]);

    const fetchClasses = async (page: number) => {
        try {
            setLoading(true);
            // Use different endpoint for students
            const isStudent = user?.roles?.includes(Role.STUDENT);
            const response = isStudent
                ? await studentApi.getClasses(page, 10)
                : await courseClassApi.getAll(page, 10);
            console.log('Classes API response:', response);
            // Handle both lowercase 'classes' and capitalized 'Classes'
            const classesData =
                (response.data as any)?.Classes ||
                (response.data as any)?.classes ||
                [];
            const paginationData = (response.data as any)?.Pagination ||
                (response.data as any)?.pagination || {
                    totalPages: 0,
                };
            setClasses(classesData);
            setTotalPages(paginationData.totalPages);
        } catch (err: any) {
            console.error('Classes fetch error:', err);
            setError(err.response?.data?.error || 'Failed to fetch classes');
        } finally {
            setLoading(false);
        }
    };
    const handleDelete = async (id: string) => {
        if (!confirm('Are you sure you want to delete this class?')) return;

        try {
            await courseClassApi.delete(id);
            fetchClasses(currentPage);
        } catch (err: any) {
            alert(err.response?.data?.error || 'Failed to delete class');
        }
    };

    const handleEnroll = async (classId: string) => {
        if (!confirm('Are you sure you want to enroll in this class?')) return;

        try {
            await studentApi.enroll(classId);
            alert('Successfully enrolled in class!');
            fetchClasses(currentPage);
        } catch (err: any) {
            alert(err.response?.data?.error || 'Failed to enroll in class');
        }
    };

    return (
        <ProtectedRoute>
            <div className="min-h-screen">
                <Navbar />
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    <PageHeader
                        title="Course Classes"
                        description="Browse and manage class schedules"
                        action={
                            hasRole(Role.ADMIN) ? (
                                <Link
                                    href="/classes/create"
                                    className="btn-primary"
                                >
                                    + Add Class
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
                            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                                {classes.map((courseClass) => (
                                    <div key={courseClass.id} className="card">
                                        <div className="flex justify-between items-start mb-4">
                                            <div>
                                                <span className="px-3 py-1 bg-blue-100 text-blue-700 rounded-full text-xs font-medium">
                                                    {
                                                        courseClass.course
                                                            .courseCode
                                                    }
                                                </span>
                                                <span
                                                    className={`ml-2 px-3 py-1 rounded-full text-xs font-medium ${
                                                        courseClass.status ===
                                                        'ACTIVE'
                                                            ? 'bg-green-100 text-green-700'
                                                            : courseClass.status ===
                                                              'COMPLETED'
                                                            ? 'bg-gray-100 text-gray-700'
                                                            : 'bg-red-100 text-red-700'
                                                    }`}
                                                >
                                                    {courseClass.status}
                                                </span>
                                            </div>
                                            <span className="text-sm text-gray-500">
                                                {courseClass.course.credits}{' '}
                                                Credits
                                            </span>
                                        </div>

                                        <h3 className="text-lg font-semibold text-gray-900 mb-2">
                                            {courseClass.course.title}
                                        </h3>

                                        <div className="space-y-2 mb-4 text-sm">
                                            <div className="flex justify-between">
                                                <span className="text-gray-600">
                                                    Lecturer:
                                                </span>
                                                <span className="font-medium">
                                                    {
                                                        courseClass.lecturer
                                                            .person.firstName
                                                    }{' '}
                                                    {
                                                        courseClass.lecturer
                                                            .person.lastName
                                                    }
                                                </span>
                                            </div>
                                            <div className="flex justify-between">
                                                <span className="text-gray-600">
                                                    Semester:
                                                </span>
                                                <span className="font-medium">
                                                    {courseClass.semester}
                                                </span>
                                            </div>
                                            <div className="flex justify-between">
                                                <span className="text-gray-600">
                                                    Year:
                                                </span>
                                                <span className="font-medium">
                                                    {courseClass.academicYear}
                                                </span>
                                            </div>
                                            <div className="flex justify-between">
                                                <span className="text-gray-600">
                                                    Capacity:
                                                </span>
                                                <span
                                                    className={`font-medium ${
                                                        courseClass.currentCapacity >=
                                                        courseClass.maxCapacity
                                                            ? 'text-red-600'
                                                            : 'text-green-600'
                                                    }`}
                                                >
                                                    {
                                                        courseClass.currentCapacity
                                                    }{' '}
                                                    / {courseClass.maxCapacity}
                                                </span>
                                            </div>
                                        </div>

                                        <div className="flex space-x-2">
                                            <Link
                                                href={`/classes/${courseClass.id}`}
                                                className="flex-1 text-center btn-secondary text-sm py-2"
                                            >
                                                View Details
                                            </Link>
                                            {hasRole(Role.STUDENT) &&
                                                courseClass.status ===
                                                    'ACTIVE' && (
                                                    <button
                                                        onClick={() =>
                                                            handleEnroll(
                                                                courseClass.id
                                                            )
                                                        }
                                                        disabled={
                                                            courseClass.currentCapacity >=
                                                            courseClass.maxCapacity
                                                        }
                                                        className="flex-1 btn-primary text-sm py-2 disabled:opacity-50 disabled:cursor-not-allowed"
                                                    >
                                                        {courseClass.currentCapacity >=
                                                        courseClass.maxCapacity
                                                            ? 'Full'
                                                            : 'Enroll'}
                                                    </button>
                                                )}
                                            {hasRole(Role.ADMIN) && (
                                                <>
                                                    <Link
                                                        href={`/classes/${courseClass.id}/edit`}
                                                        className="px-4 py-2 text-blue-500 hover:bg-blue-50 rounded-lg text-sm font-medium border border-blue-200"
                                                    >
                                                        Edit
                                                    </Link>
                                                    <button
                                                        onClick={() =>
                                                            handleDelete(
                                                                courseClass.id
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

                            {classes.length === 0 && (
                                <div className="text-center py-12">
                                    <p className="text-gray-500">
                                        No classes found
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
