'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import ProtectedRoute from '@/components/ProtectedRoute';
import Navbar from '@/components/Navbar';
import PageHeader from '@/components/PageHeader';
import LoadingSpinner from '@/components/LoadingSpinner';
import Pagination from '@/components/Pagination';
import { Role, Student } from '@/lib/types';
import { studentApi } from '@/lib/api';
import Link from 'next/link';
import { useAuth } from '@/contexts/AuthContext';

export default function StudentsPage() {
    const [students, setStudents] = useState<Student[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [currentPage, setCurrentPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);
    const router = useRouter();
    const { hasRole } = useAuth();

    useEffect(() => {
        fetchStudents(currentPage);
    }, [currentPage]);

    const fetchStudents = async (page: number) => {
        try {
            setLoading(true);
            const response = await studentApi.getAll(page, 20);
            console.log('Students API response:', response);
            // Handle both lowercase 'students' and capitalized 'Students'
            const studentsData =
                (response.data as any)?.Students ||
                (response.data as any)?.students ||
                [];
            const paginationData = (response.data as any)?.Pagination ||
                (response.data as any)?.pagination || {
                    totalPages: 0,
                };
            setStudents(studentsData);
            setTotalPages(paginationData.totalPages);
        } catch (err: any) {
            console.error('Students fetch error:', err);
            setError(err.response?.data?.error || 'Failed to fetch students');
        } finally {
            setLoading(false);
        }
    };
    const handleDelete = async (id: string) => {
        if (!confirm('Are you sure you want to delete this student?')) return;

        try {
            await studentApi.delete(id);
            fetchStudents(currentPage);
        } catch (err: any) {
            alert(err.response?.data?.error || 'Failed to delete student');
        }
    };

    return (
        <ProtectedRoute allowedRoles={[Role.ADMIN, Role.EMPLOYEE]}>
            <div className="min-h-screen">
                <Navbar />
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    <PageHeader
                        title="Students"
                        description="Manage student records"
                        action={
                            hasRole(Role.ADMIN) && (
                                <Link
                                    href="/students/create"
                                    className="btn-primary"
                                >
                                    + Add Student
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
                                                    Student Number
                                                </th>
                                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                                                    Name
                                                </th>
                                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                                                    Email
                                                </th>
                                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                                                    Major
                                                </th>
                                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                                                    Year
                                                </th>
                                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                                                    GPA
                                                </th>
                                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                                                    Status
                                                </th>
                                                <th className="px-6 py-4 text-right text-sm font-semibold text-gray-700">
                                                    Actions
                                                </th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            {students.map((student) => (
                                                <tr
                                                    key={student.id}
                                                    className="table-row"
                                                >
                                                    <td className="px-6 py-4 text-sm text-gray-900">
                                                        {student.studentNumber}
                                                    </td>
                                                    <td className="px-6 py-4 text-sm text-gray-900">
                                                        {
                                                            student.person
                                                                .firstName
                                                        }{' '}
                                                        {
                                                            student.person
                                                                .lastName
                                                        }
                                                    </td>
                                                    <td className="px-6 py-4 text-sm text-gray-600">
                                                        {student.person.email}
                                                    </td>
                                                    <td className="px-6 py-4 text-sm text-gray-600">
                                                        {student.major || 'N/A'}
                                                    </td>
                                                    <td className="px-6 py-4 text-sm text-gray-600">
                                                        {student.academicYear ||
                                                            'N/A'}
                                                    </td>
                                                    <td className="px-6 py-4 text-sm text-gray-600">
                                                        {student.gpa?.toFixed(
                                                            2
                                                        ) || 'N/A'}
                                                    </td>
                                                    <td className="px-6 py-4">
                                                        <span
                                                            className={`px-3 py-1 rounded-full text-xs font-medium ${
                                                                student.status ===
                                                                'ACTIVE'
                                                                    ? 'bg-green-100 text-green-700'
                                                                    : student.status ===
                                                                      'GRADUATED'
                                                                    ? 'bg-blue-100 text-blue-700'
                                                                    : 'bg-red-100 text-red-700'
                                                            }`}
                                                        >
                                                            {student.status}
                                                        </span>
                                                    </td>
                                                    <td className="px-6 py-4 text-right space-x-2">
                                                        <Link
                                                            href={`/students/${student.id}`}
                                                            className="text-blue-500 hover:text-blue-600 text-sm font-medium"
                                                        >
                                                            View
                                                        </Link>
                                                        {hasRole(
                                                            Role.ADMIN
                                                        ) && (
                                                            <>
                                                                <Link
                                                                    href={`/students/${student.id}/edit`}
                                                                    className="text-blue-500 hover:text-blue-600 text-sm font-medium"
                                                                >
                                                                    Edit
                                                                </Link>
                                                                <button
                                                                    onClick={() =>
                                                                        handleDelete(
                                                                            student.id
                                                                        )
                                                                    }
                                                                    className="text-red-500 hover:text-red-600 text-sm font-medium"
                                                                >
                                                                    Delete
                                                                </button>
                                                            </>
                                                        )}
                                                    </td>
                                                </tr>
                                            ))}
                                        </tbody>
                                    </table>
                                </div>
                            </div>

                            {students.length === 0 && (
                                <div className="text-center py-12">
                                    <p className="text-gray-500">
                                        No students found
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
