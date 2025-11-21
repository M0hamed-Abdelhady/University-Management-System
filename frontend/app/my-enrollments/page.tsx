'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import ProtectedRoute from '@/components/ProtectedRoute';
import Navbar from '@/components/Navbar';
import PageHeader from '@/components/PageHeader';
import LoadingSpinner from '@/components/LoadingSpinner';
import { Enrollment, Role } from '@/lib/types';
import { studentApi } from '@/lib/api';

export default function MyEnrollmentsPage() {
    const router = useRouter();
    const [enrollments, setEnrollments] = useState<Enrollment[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [droppingId, setDroppingId] = useState<string | null>(null);

    useEffect(() => {
        fetchEnrollments();
    }, []);

    const fetchEnrollments = async () => {
        try {
            setLoading(true);
            const response = await studentApi.getEnrollments();
            // Handle both lowercase and capitalized property names
            const enrollmentsData =
                (response.data as any)?.enrollments ||
                (response.data as any)?.Enrollments ||
                [];
            setEnrollments(enrollmentsData);
        } catch (err: any) {
            setError(
                err.response?.data?.message ||
                    'Failed to fetch enrollments. Please try again.'
            );
        } finally {
            setLoading(false);
        }
    };

    const handleDrop = async (enrollmentId: string) => {
        if (
            !confirm(
                'Are you sure you want to drop this course? This action cannot be undone.'
            )
        ) {
            return;
        }

        try {
            setDroppingId(enrollmentId);
            await studentApi.dropEnrollment(enrollmentId);
            await fetchEnrollments();
        } catch (err: any) {
            setError(
                err.response?.data?.message ||
                    'Failed to drop enrollment. Please try again.'
            );
        } finally {
            setDroppingId(null);
        }
    };

    if (loading) {
        return (
            <ProtectedRoute allowedRoles={[Role.STUDENT]}>
                <div className="min-h-screen">
                    <Navbar />
                    <LoadingSpinner />
                </div>
            </ProtectedRoute>
        );
    }

    return (
        <ProtectedRoute allowedRoles={[Role.STUDENT]}>
            <div className="min-h-screen">
                <Navbar />
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    <PageHeader
                        title="My Enrollments"
                        description="View your enrolled courses and grades"
                    />

                    {error && (
                        <div className="bg-red-50 border border-red-200 text-red-800 px-4 py-3 rounded-lg mb-4">
                            {error}
                        </div>
                    )}

                    <div className="card overflow-hidden">
                        {enrollments.length === 0 ? (
                            <div className="text-center py-12">
                                <p className="text-gray-500">
                                    You are not enrolled in any courses yet.
                                </p>
                                <button
                                    onClick={() => router.push('/classes')}
                                    className="btn-primary mt-4"
                                >
                                    Browse Classes
                                </button>
                            </div>
                        ) : (
                            <div className="overflow-x-auto">
                                <table className="min-w-full divide-y divide-gray-200">
                                    <thead className="bg-gray-50">
                                        <tr>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Course
                                            </th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Class
                                            </th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Lecturer
                                            </th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Grade
                                            </th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Status
                                            </th>
                                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                                Actions
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody className="bg-white divide-y divide-gray-200">
                                        {enrollments.map((enrollment) => (
                                            <tr
                                                key={enrollment.id}
                                                className="hover:bg-gray-50"
                                            >
                                                <td className="px-6 py-4">
                                                    <div className="text-sm font-medium text-gray-900">
                                                        {
                                                            enrollment
                                                                .courseClass
                                                                .course.title
                                                        }
                                                    </div>
                                                    <div className="text-sm text-gray-500">
                                                        {
                                                            enrollment
                                                                .courseClass
                                                                .course
                                                                .courseCode
                                                        }{' '}
                                                        (
                                                        {
                                                            enrollment
                                                                .courseClass
                                                                .course.credits
                                                        }{' '}
                                                        credits)
                                                    </div>
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap">
                                                    <div className="text-sm text-gray-900">
                                                        {
                                                            enrollment
                                                                .courseClass
                                                                .semester
                                                        }{' '}
                                                        {
                                                            enrollment
                                                                .courseClass
                                                                .academicYear
                                                        }
                                                    </div>
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap">
                                                    <div className="text-sm text-gray-900">
                                                        {
                                                            enrollment
                                                                .courseClass
                                                                .lecturer.person
                                                                .firstName
                                                        }{' '}
                                                        {
                                                            enrollment
                                                                .courseClass
                                                                .lecturer.person
                                                                .lastName
                                                        }
                                                    </div>
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap">
                                                    <div className="text-sm font-medium text-gray-900">
                                                        {enrollment.grade ||
                                                            'N/A'}
                                                    </div>
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap">
                                                    <span
                                                        className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                                                            enrollment.status ===
                                                            'ENROLLED'
                                                                ? 'bg-green-100 text-green-800'
                                                                : enrollment.status ===
                                                                  'COMPLETED'
                                                                ? 'bg-blue-100 text-blue-800'
                                                                : 'bg-gray-100 text-gray-800'
                                                        }`}
                                                    >
                                                        {enrollment.status}
                                                    </span>
                                                </td>
                                                <td className="px-6 py-4 whitespace-nowrap text-sm">
                                                    {enrollment.status ===
                                                        'ENROLLED' && (
                                                        <button
                                                            onClick={() =>
                                                                handleDrop(
                                                                    enrollment.id
                                                                )
                                                            }
                                                            disabled={
                                                                droppingId ===
                                                                enrollment.id
                                                            }
                                                            className="px-3 py-1.5 bg-red-100 text-red-700 text-sm font-medium rounded-lg hover:bg-red-200 disabled:opacity-50 disabled:cursor-not-allowed transition-colors border border-red-300"
                                                        >
                                                            {droppingId ===
                                                            enrollment.id
                                                                ? 'Dropping...'
                                                                : 'Drop'}
                                                        </button>
                                                    )}
                                                </td>
                                            </tr>
                                        ))}
                                    </tbody>
                                </table>
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </ProtectedRoute>
    );
}
