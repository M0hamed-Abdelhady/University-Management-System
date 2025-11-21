'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import ProtectedRoute from '@/components/ProtectedRoute';
import Navbar from '@/components/Navbar';
import PageHeader from '@/components/PageHeader';
import LoadingSpinner from '@/components/LoadingSpinner';
import Pagination from '@/components/Pagination';
import { Role, Enrollment } from '@/lib/types';
import { enrollmentApi, studentApi } from '@/lib/api';
import Link from 'next/link';
import { useAuth } from '@/contexts/AuthContext';

export default function EnrollmentsPage() {
    const [enrollments, setEnrollments] = useState<Enrollment[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const [selectedEnrollment, setSelectedEnrollment] =
        useState<Enrollment | null>(null);
    const [gradeInput, setGradeInput] = useState('');
    const router = useRouter();
    const { hasRole } = useAuth();

    useEffect(() => {
        fetchEnrollments(currentPage);
    }, [currentPage]);

    const fetchEnrollments = async (page: number) => {
        try {
            setLoading(true);
            const response = await enrollmentApi.getAll(page, 20);
            console.log('Enrollments API response:', response);
            // Handle both lowercase 'enrollments' and capitalized 'Enrollments'
            const enrollmentsData =
                (response.data as any)?.Enrollments ||
                (response.data as any)?.enrollments ||
                [];
            const paginationData = (response.data as any)?.Pagination ||
                (response.data as any)?.pagination || {
                    totalPages: 0,
                };
            setEnrollments(enrollmentsData);
            setTotalPages(paginationData.totalPages);
        } catch (err: any) {
            console.error('Enrollments fetch error:', err);
            setError(
                err.response?.data?.error || 'Failed to fetch enrollments'
            );
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id: string) => {
        if (!confirm('Are you sure you want to delete this enrollment?'))
            return;

        try {
            await enrollmentApi.delete(id);
            fetchEnrollments(currentPage);
        } catch (err: any) {
            alert(err.response?.data?.error || 'Failed to delete enrollment');
        }
    };

    const handleUpdateGrade = async () => {
        if (!selectedEnrollment || !gradeInput.trim()) return;

        try {
            await enrollmentApi.updateGrade(selectedEnrollment.id, gradeInput);

            // Update student GPA after grade update
            const studentId = selectedEnrollment.student?.id;
            if (studentId) {
                await studentApi.updateGPA(studentId);
            }

            setSelectedEnrollment(null);
            setGradeInput('');
            fetchEnrollments(currentPage);
        } catch (err: any) {
            alert(err.response?.data?.error || 'Failed to update grade');
        }
    };

    return (
        <ProtectedRoute allowedRoles={[Role.ADMIN, Role.EMPLOYEE]}>
            <div className="min-h-screen">
                <Navbar />
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    <PageHeader
                        title="Enrollments"
                        description="Manage all student enrollments"
                        action={
                            hasRole(Role.ADMIN) && (
                                <Link
                                    href="/enrollments/create"
                                    className="btn-primary"
                                >
                                    + Add Enrollment
                                </Link>
                            )
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
                            <div className="card overflow-hidden p-0">
                                <div className="overflow-x-auto">
                                    <table className="w-full">
                                        <thead className="table-header">
                                            <tr>
                                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                                                    Student
                                                </th>
                                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                                                    Course
                                                </th>
                                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                                                    Class
                                                </th>
                                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                                                    Semester/Year
                                                </th>
                                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                                                    Status
                                                </th>
                                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                                                    Grade
                                                </th>
                                                <th className="px-6 py-4 text-right text-sm font-semibold text-gray-700">
                                                    Actions
                                                </th>
                                            </tr>
                                        </thead>
                                        <tbody className="divide-y divide-gray-100">
                                            {enrollments.length === 0 ? (
                                                <tr>
                                                    <td
                                                        colSpan={7}
                                                        className="px-6 py-12 text-center text-gray-500"
                                                    >
                                                        No enrollments found
                                                    </td>
                                                </tr>
                                            ) : (
                                                enrollments.map(
                                                    (enrollment) => (
                                                        <tr
                                                            key={enrollment.id}
                                                            className="hover:bg-blue-50 transition-colors"
                                                        >
                                                            <td className="px-6 py-4 text-sm text-gray-900">
                                                                {
                                                                    enrollment
                                                                        .student
                                                                        ?.person
                                                                        ?.firstName
                                                                }{' '}
                                                                {
                                                                    enrollment
                                                                        .student
                                                                        ?.person
                                                                        ?.lastName
                                                                }
                                                                <div className="text-xs text-gray-500">
                                                                    {
                                                                        enrollment
                                                                            .student
                                                                            ?.studentNumber
                                                                    }
                                                                </div>
                                                            </td>
                                                            <td className="px-6 py-4 text-sm text-gray-900">
                                                                {
                                                                    enrollment
                                                                        .courseClass
                                                                        ?.course
                                                                        ?.courseCode
                                                                }
                                                                <div className="text-xs text-gray-500">
                                                                    {
                                                                        enrollment
                                                                            .courseClass
                                                                            ?.course
                                                                            ?.title
                                                                    }
                                                                </div>
                                                            </td>
                                                            <td className="px-6 py-4 text-sm text-gray-900">
                                                                {
                                                                    enrollment
                                                                        .courseClass
                                                                        ?.semester
                                                                }{' '}
                                                                {
                                                                    enrollment
                                                                        .courseClass
                                                                        ?.academicYear
                                                                }
                                                            </td>
                                                            <td className="px-6 py-4 text-sm text-gray-600">
                                                                {
                                                                    enrollment
                                                                        .courseClass
                                                                        ?.semester
                                                                }
                                                            </td>
                                                            <td className="px-6 py-4">
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
                                                                    {
                                                                        enrollment.status
                                                                    }
                                                                </span>
                                                            </td>
                                                            <td className="px-6 py-4 text-sm text-gray-900">
                                                                {enrollment.grade ||
                                                                    '-'}
                                                            </td>
                                                            <td className="px-6 py-4 text-right text-sm">
                                                                <div className="flex justify-end gap-2">
                                                                    <button
                                                                        onClick={() => {
                                                                            setSelectedEnrollment(
                                                                                enrollment
                                                                            );
                                                                            setGradeInput(
                                                                                enrollment.grade ||
                                                                                    ''
                                                                            );
                                                                        }}
                                                                        className="text-blue-600 hover:text-blue-800 font-medium"
                                                                    >
                                                                        Grade
                                                                    </button>
                                                                    {hasRole(
                                                                        Role.ADMIN
                                                                    ) && (
                                                                        <button
                                                                            onClick={() =>
                                                                                handleDelete(
                                                                                    enrollment.id
                                                                                )
                                                                            }
                                                                            className="text-red-600 hover:text-red-800 font-medium"
                                                                        >
                                                                            Delete
                                                                        </button>
                                                                    )}
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    )
                                                )
                                            )}
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                            {totalPages > 1 && (
                                <div className="mt-6">
                                    <Pagination
                                        currentPage={currentPage}
                                        totalPages={totalPages}
                                        onPageChange={setCurrentPage}
                                    />
                                </div>
                            )}
                        </>
                    )}
                </div>
            </div>

            {/* Grade Update Modal */}
            {selectedEnrollment && (
                <div className="fixed inset-0 bg-black/30 backdrop-blur-sm flex items-center justify-center p-4 z-50">
                    <div className="bg-white/95 backdrop-blur-md rounded-2xl shadow-2xl p-6 max-w-md w-full border border-gray-200">
                        <h3 className="text-lg font-semibold text-gray-900 mb-4">
                            Update Grade
                        </h3>
                        <div className="mb-4">
                            <p className="text-sm text-gray-600 mb-2">
                                Student:{' '}
                                {selectedEnrollment.student?.person?.firstName}{' '}
                                {selectedEnrollment.student?.person?.lastName}
                            </p>
                            <p className="text-sm text-gray-600 mb-4">
                                Course:{' '}
                                {
                                    selectedEnrollment.courseClass?.course
                                        ?.courseCode
                                }{' '}
                                -{' '}
                                {selectedEnrollment.courseClass?.course?.title}
                            </p>
                            <input
                                type="text"
                                value={gradeInput}
                                onChange={(e) => setGradeInput(e.target.value)}
                                placeholder="Enter grade (e.g., A+, B)"
                                className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
                                autoFocus
                            />
                        </div>
                        <div className="flex justify-end gap-2">
                            <button
                                onClick={() => {
                                    setSelectedEnrollment(null);
                                    setGradeInput('');
                                }}
                                className="px-4 py-2 text-gray-700 hover:bg-gray-100 rounded-lg transition-colors"
                            >
                                Cancel
                            </button>
                            <button
                                onClick={handleUpdateGrade}
                                className="btn-primary"
                            >
                                Update Grade
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </ProtectedRoute>
    );
}
