'use client';

import { useState, useEffect } from 'react';
import { useRouter, useParams } from 'next/navigation';
import ProtectedRoute from '@/components/ProtectedRoute';
import Navbar from '@/components/Navbar';
import PageHeader from '@/components/PageHeader';
import LoadingSpinner from '@/components/LoadingSpinner';
import { Role, Student } from '@/lib/types';
import { studentApi } from '@/lib/api';
import { useAuth } from '@/contexts/AuthContext';

export default function StudentDetailPage() {
    const router = useRouter();
    const params = useParams();
    const id = params.id as string;
    const [student, setStudent] = useState<Student | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const { hasRole } = useAuth();

    useEffect(() => {
        fetchStudent();
    }, [id]);

    const fetchStudent = async () => {
        try {
            setLoading(true);
            const response = await studentApi.getById(id);
            const studentData =
                (response.data as any)?.Student ||
                (response.data as any)?.student;
            setStudent(studentData);
        } catch (err: any) {
            setError(
                err.response?.data?.error || 'Failed to fetch student details'
            );
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <ProtectedRoute allowedRoles={[Role.ADMIN, Role.EMPLOYEE]}>
                <div className="min-h-screen">
                    <Navbar />
                    <LoadingSpinner />
                </div>
            </ProtectedRoute>
        );
    }

    if (error || !student) {
        return (
            <ProtectedRoute allowedRoles={[Role.ADMIN, Role.EMPLOYEE]}>
                <div className="min-h-screen">
                    <Navbar />
                    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                        <div className="p-4 bg-red-50 border border-red-200 rounded-lg">
                            <p className="text-red-600">
                                {error || 'Student not found'}
                            </p>
                        </div>
                    </div>
                </div>
            </ProtectedRoute>
        );
    }

    return (
        <ProtectedRoute allowedRoles={[Role.ADMIN, Role.EMPLOYEE]}>
            <div className="min-h-screen">
                <Navbar />
                <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    <PageHeader
                        title="Student Details"
                        description={`Viewing ${student.person.firstName} ${student.person.lastName}`}
                        action={
                            hasRole(Role.ADMIN) && (
                                <button
                                    onClick={() =>
                                        router.push(`/students/${id}/edit`)
                                    }
                                    className="btn-primary"
                                >
                                    Edit Student
                                </button>
                            )
                        }
                    />

                    <div className="card space-y-6">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Student Number
                                </h3>
                                <p className="text-base text-gray-900">
                                    {student.studentNumber}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Status
                                </h3>
                                <span
                                    className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                                        student.status === 'ACTIVE'
                                            ? 'bg-green-100 text-green-800'
                                            : student.status === 'GRADUATED'
                                            ? 'bg-blue-100 text-blue-800'
                                            : 'bg-gray-100 text-gray-800'
                                    }`}
                                >
                                    {student.status}
                                </span>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    First Name
                                </h3>
                                <p className="text-base text-gray-900">
                                    {student.person.firstName}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Last Name
                                </h3>
                                <p className="text-base text-gray-900">
                                    {student.person.lastName}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Email
                                </h3>
                                <p className="text-base text-gray-900">
                                    {student.person.email}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Phone
                                </h3>
                                <p className="text-base text-gray-900">
                                    {student.person.phone || 'N/A'}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Date of Birth
                                </h3>
                                <p className="text-base text-gray-900">
                                    {student.person.dateOfBirth || 'N/A'}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Major
                                </h3>
                                <p className="text-base text-gray-900">
                                    {student.major || 'N/A'}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Academic Year
                                </h3>
                                <p className="text-base text-gray-900">
                                    {student.academicYear || 'N/A'}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    GPA
                                </h3>
                                <p className="text-base text-gray-900">
                                    {student.gpa?.toFixed(2) || 'N/A'}
                                </p>
                            </div>

                            <div className="md:col-span-2">
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Address
                                </h3>
                                <p className="text-base text-gray-900">
                                    {student.person.address || 'N/A'}
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
