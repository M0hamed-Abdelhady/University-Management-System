'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import ProtectedRoute from '@/components/ProtectedRoute';
import Navbar from '@/components/Navbar';
import PageHeader from '@/components/PageHeader';
import LoadingSpinner from '@/components/LoadingSpinner';
import Pagination from '@/components/Pagination';
import { Role, Employee } from '@/lib/types';
import { employeeApi } from '@/lib/api';
import Link from 'next/link';

export default function EmployeesPage() {
    const [employees, setEmployees] = useState<Employee[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(0);
    const router = useRouter();

    useEffect(() => {
        fetchEmployees(currentPage);
    }, [currentPage]);

    const fetchEmployees = async (page: number) => {
        try {
            setLoading(true);
            const response = await employeeApi.getAll(page, 20);
            console.log('Employees API response:', response);
            // Handle both lowercase 'employees' and capitalized 'Employees'
            const employeesData =
                (response.data as any)?.Employees ||
                (response.data as any)?.employees ||
                [];
            const paginationData = (response.data as any)?.Pagination ||
                (response.data as any)?.pagination || {
                    totalPages: 0,
                };
            setEmployees(employeesData);
            setTotalPages(paginationData.totalPages);
        } catch (err: any) {
            console.error('Employees fetch error:', err);
            setError(err.response?.data?.error || 'Failed to fetch employees');
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id: string) => {
        if (!confirm('Are you sure you want to delete this employee?')) return;

        try {
            await employeeApi.delete(id);
            fetchEmployees(currentPage);
        } catch (err: any) {
            alert(err.response?.data?.error || 'Failed to delete employee');
        }
    };

    return (
        <ProtectedRoute allowedRoles={[Role.ADMIN]}>
            <div className="min-h-screen">
                <Navbar />
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    <PageHeader
                        title="Employees"
                        description="Manage employee records"
                        action={
                            <Link
                                href="/employees/create"
                                className="btn-primary"
                            >
                                + Add Employee
                            </Link>
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
                                                    Employee ID
                                                </th>
                                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                                                    Name
                                                </th>
                                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                                                    Email
                                                </th>
                                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                                                    Position
                                                </th>
                                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                                                    Status
                                                </th>
                                                <th className="px-6 py-4 text-left text-sm font-semibold text-gray-700">
                                                    Hire Date
                                                </th>
                                                <th className="px-6 py-4 text-right text-sm font-semibold text-gray-700">
                                                    Actions
                                                </th>
                                            </tr>
                                        </thead>
                                        <tbody className="divide-y divide-gray-100">
                                            {employees.length === 0 ? (
                                                <tr>
                                                    <td
                                                        colSpan={7}
                                                        className="px-6 py-12 text-center text-gray-500"
                                                    >
                                                        No employees found
                                                    </td>
                                                </tr>
                                            ) : (
                                                employees.map((employee) => (
                                                    <tr
                                                        key={employee.id}
                                                        className="hover:bg-blue-50 transition-colors"
                                                    >
                                                        <td className="px-6 py-4 text-sm text-gray-900">
                                                            {
                                                                employee.employeeId
                                                            }
                                                        </td>
                                                        <td className="px-6 py-4 text-sm text-gray-900">
                                                            {
                                                                employee.person
                                                                    ?.firstName
                                                            }{' '}
                                                            {
                                                                employee.person
                                                                    ?.lastName
                                                            }
                                                        </td>
                                                        <td className="px-6 py-4 text-sm text-gray-600">
                                                            {
                                                                employee.person
                                                                    ?.email
                                                            }
                                                        </td>
                                                        <td className="px-6 py-4 text-sm text-gray-900">
                                                            {employee.position}
                                                        </td>
                                                        <td className="px-6 py-4">
                                                            <span
                                                                className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                                                                    employee.status ===
                                                                    'ACTIVE'
                                                                        ? 'bg-green-100 text-green-800'
                                                                        : 'bg-gray-100 text-gray-800'
                                                                }`}
                                                            >
                                                                {
                                                                    employee.status
                                                                }
                                                            </span>
                                                        </td>
                                                        <td className="px-6 py-4 text-sm text-gray-600">
                                                            {employee.hireDate}
                                                        </td>
                                                        <td className="px-6 py-4 text-right text-sm">
                                                            <div className="flex justify-end gap-2">
                                                                <Link
                                                                    href={`/employees/${employee.id}`}
                                                                    className="text-blue-600 hover:text-blue-800 font-medium"
                                                                >
                                                                    View
                                                                </Link>
                                                                <Link
                                                                    href={`/employees/${employee.id}/edit`}
                                                                    className="text-blue-600 hover:text-blue-800 font-medium"
                                                                >
                                                                    Edit
                                                                </Link>
                                                                <button
                                                                    onClick={() =>
                                                                        handleDelete(
                                                                            employee.id
                                                                        )
                                                                    }
                                                                    className="text-red-600 hover:text-red-800 font-medium"
                                                                >
                                                                    Delete
                                                                </button>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                ))
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
        </ProtectedRoute>
    );
}
