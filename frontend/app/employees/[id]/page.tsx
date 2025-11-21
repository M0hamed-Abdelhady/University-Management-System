'use client';

import { useState, useEffect } from 'react';
import { useRouter, useParams } from 'next/navigation';
import ProtectedRoute from '@/components/ProtectedRoute';
import Navbar from '@/components/Navbar';
import PageHeader from '@/components/PageHeader';
import LoadingSpinner from '@/components/LoadingSpinner';
import { Role, Employee } from '@/lib/types';
import { employeeApi } from '@/lib/api';

export default function EmployeeDetailPage() {
    const router = useRouter();
    const params = useParams();
    const id = params.id as string;
    const [employee, setEmployee] = useState<Employee | null>(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        fetchEmployee();
    }, [id]);

    const fetchEmployee = async () => {
        try {
            setLoading(true);
            const response = await employeeApi.getById(id);
            const employeeData =
                (response.data as any)?.Employee ||
                (response.data as any)?.employee;
            setEmployee(employeeData);
        } catch (err: any) {
            setError(
                err.response?.data?.error || 'Failed to fetch employee details'
            );
        } finally {
            setLoading(false);
        }
    };

    if (loading) {
        return (
            <ProtectedRoute allowedRoles={[Role.ADMIN]}>
                <div className="min-h-screen">
                    <Navbar />
                    <LoadingSpinner />
                </div>
            </ProtectedRoute>
        );
    }

    if (error || !employee) {
        return (
            <ProtectedRoute allowedRoles={[Role.ADMIN]}>
                <div className="min-h-screen">
                    <Navbar />
                    <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                        <div className="p-4 bg-red-50 border border-red-200 rounded-lg">
                            <p className="text-red-600">
                                {error || 'Employee not found'}
                            </p>
                        </div>
                    </div>
                </div>
            </ProtectedRoute>
        );
    }

    return (
        <ProtectedRoute allowedRoles={[Role.ADMIN]}>
            <div className="min-h-screen">
                <Navbar />
                <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    <PageHeader
                        title="Employee Details"
                        description={`Viewing ${employee.person.firstName} ${employee.person.lastName}`}
                        action={
                            <button
                                onClick={() =>
                                    router.push(`/employees/${id}/edit`)
                                }
                                className="btn-primary"
                            >
                                Edit Employee
                            </button>
                        }
                    />

                    <div className="card space-y-6">
                        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Employee ID
                                </h3>
                                <p className="text-base text-gray-900">
                                    {employee.employeeId}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Status
                                </h3>
                                <span
                                    className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                                        employee.status === 'ACTIVE'
                                            ? 'bg-green-100 text-green-800'
                                            : 'bg-gray-100 text-gray-800'
                                    }`}
                                >
                                    {employee.status}
                                </span>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    First Name
                                </h3>
                                <p className="text-base text-gray-900">
                                    {employee.person.firstName}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Last Name
                                </h3>
                                <p className="text-base text-gray-900">
                                    {employee.person.lastName}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Email
                                </h3>
                                <p className="text-base text-gray-900">
                                    {employee.person.email}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Phone
                                </h3>
                                <p className="text-base text-gray-900">
                                    {employee.person.phone || 'N/A'}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Date of Birth
                                </h3>
                                <p className="text-base text-gray-900">
                                    {employee.person.dateOfBirth || 'N/A'}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Position
                                </h3>
                                <p className="text-base text-gray-900">
                                    {employee.position}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Hire Date
                                </h3>
                                <p className="text-base text-gray-900">
                                    {employee.hireDate || 'N/A'}
                                </p>
                            </div>

                            <div>
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Salary
                                </h3>
                                <p className="text-base text-gray-900">
                                    {employee.salary
                                        ? `$${employee.salary.toLocaleString()}`
                                        : 'N/A'}
                                </p>
                            </div>

                            <div className="md:col-span-2">
                                <h3 className="text-sm font-medium text-gray-500 mb-1">
                                    Address
                                </h3>
                                <p className="text-base text-gray-900">
                                    {employee.person.address || 'N/A'}
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
