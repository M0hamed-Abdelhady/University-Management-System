'use client';

import { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import ProtectedRoute from '@/components/ProtectedRoute';
import Navbar from '@/components/Navbar';
import PageHeader from '@/components/PageHeader';
import LoadingSpinner from '@/components/LoadingSpinner';
import { useAuth } from '@/contexts/AuthContext';
import { Role } from '@/lib/types';
import { authApi } from '@/lib/api';

export default function ProfilePage() {
    const router = useRouter();
    const { user, refreshUser } = useAuth();
    const [loading, setLoading] = useState(false);
    const [isEditing, setIsEditing] = useState(false);
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        phone: '',
        dateOfBirth: '',
        address: '',
    });
    const [error, setError] = useState('');

    useEffect(() => {
        // Refresh user data when profile page loads
        refreshUser();
    }, []);

    useEffect(() => {
        if (user) {
            setFormData({
                firstName: user.firstName || '',
                lastName: user.lastName || '',
                phone: (user as any).phone || '',
                dateOfBirth: (user as any).dateOfBirth || '',
                address: (user as any).address || '',
            });
        }
    }, [user]);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setLoading(true);
        setError('');

        try {
            const response = await authApi.updateProfile(formData);
            await refreshUser();
            setIsEditing(false);
        } catch (err: any) {
            setError(
                err.response?.data?.message ||
                    'Failed to update profile. Please try again.'
            );
        } finally {
            setLoading(false);
        }
    };

    const handleCancel = () => {
        if (user) {
            setFormData({
                firstName: user.firstName || '',
                lastName: user.lastName || '',
                phone: (user as any).phone || '',
                dateOfBirth: (user as any).dateOfBirth || '',
                address: (user as any).address || '',
            });
        }
        setIsEditing(false);
        setError('');
    };

    if (!user) {
        return (
            <ProtectedRoute
                allowedRoles={[Role.ADMIN, Role.EMPLOYEE, Role.STUDENT]}
            >
                <div className="min-h-screen">
                    <Navbar />
                    <LoadingSpinner />
                </div>
            </ProtectedRoute>
        );
    }

    return (
        <ProtectedRoute
            allowedRoles={[Role.ADMIN, Role.EMPLOYEE, Role.STUDENT]}
        >
            <div className="min-h-screen">
                <Navbar />
                <div className="max-w-3xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    <PageHeader
                        title="My Profile"
                        description="View and manage your account information"
                    />

                    {error && (
                        <div className="bg-red-50 border border-red-200 text-red-800 px-4 py-3 rounded-lg mb-4">
                            {error}
                        </div>
                    )}

                    {isEditing ? (
                        <form
                            onSubmit={handleSubmit}
                            className="card space-y-6"
                        >
                            <div className="pb-4 border-b">
                                <h2 className="text-xl font-bold text-gray-900">
                                    Edit Profile
                                </h2>
                                <p className="text-sm text-gray-500 mt-1">
                                    Update your personal information
                                </p>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-2">
                                        First Name *
                                    </label>
                                    <input
                                        type="text"
                                        value={formData.firstName}
                                        onChange={(e) =>
                                            setFormData({
                                                ...formData,
                                                firstName: e.target.value,
                                            })
                                        }
                                        className="input-field"
                                        required
                                    />
                                </div>

                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-2">
                                        Last Name *
                                    </label>
                                    <input
                                        type="text"
                                        value={formData.lastName}
                                        onChange={(e) =>
                                            setFormData({
                                                ...formData,
                                                lastName: e.target.value,
                                            })
                                        }
                                        className="input-field"
                                        required
                                    />
                                </div>

                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-2">
                                        Email
                                    </label>
                                    <input
                                        type="email"
                                        value={user?.email}
                                        className="input-field bg-gray-100"
                                        disabled
                                    />
                                </div>

                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-2">
                                        Phone
                                    </label>
                                    <input
                                        type="tel"
                                        value={formData.phone}
                                        onChange={(e) =>
                                            setFormData({
                                                ...formData,
                                                phone: e.target.value,
                                            })
                                        }
                                        className="input-field"
                                    />
                                </div>

                                <div>
                                    <label className="block text-sm font-medium text-gray-700 mb-2">
                                        Date of Birth
                                    </label>
                                    <input
                                        type="date"
                                        value={formData.dateOfBirth}
                                        onChange={(e) =>
                                            setFormData({
                                                ...formData,
                                                dateOfBirth: e.target.value,
                                            })
                                        }
                                        className="input-field"
                                    />
                                </div>

                                <div className="md:col-span-2">
                                    <label className="block text-sm font-medium text-gray-700 mb-2">
                                        Address
                                    </label>
                                    <textarea
                                        value={formData.address}
                                        onChange={(e) =>
                                            setFormData({
                                                ...formData,
                                                address: e.target.value,
                                            })
                                        }
                                        rows={3}
                                        className="input-field"
                                    />
                                </div>
                            </div>

                            <div className="flex justify-end gap-4 pt-6 border-t">
                                <button
                                    type="button"
                                    onClick={handleCancel}
                                    className="btn-secondary"
                                    disabled={loading}
                                >
                                    Cancel
                                </button>
                                <button
                                    type="submit"
                                    className="btn-primary"
                                    disabled={loading}
                                >
                                    {loading ? 'Saving...' : 'Save Changes'}
                                </button>
                            </div>
                        </form>
                    ) : (
                        <div className="card space-y-6">
                            <div className="flex items-center justify-between pb-4 border-b">
                                <div>
                                    <h2 className="text-2xl font-bold text-gray-900">
                                        {user.firstName} {user.lastName}
                                    </h2>
                                    <p className="text-sm text-gray-500 mt-1">
                                        {user.roles.join(', ')}
                                    </p>
                                </div>
                                <div className="w-20 h-20 bg-gradient-to-br from-blue-400 to-blue-600 rounded-full flex items-center justify-center text-white text-2xl font-bold">
                                    {user.firstName[0]}
                                    {user.lastName[0]}
                                </div>
                            </div>

                            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                <div>
                                    <h3 className="text-sm font-medium text-gray-500 mb-1">
                                        First Name
                                    </h3>
                                    <p className="text-base text-gray-900">
                                        {user.firstName}
                                    </p>
                                </div>

                                <div>
                                    <h3 className="text-sm font-medium text-gray-500 mb-1">
                                        Last Name
                                    </h3>
                                    <p className="text-base text-gray-900">
                                        {user.lastName}
                                    </p>
                                </div>

                                <div>
                                    <h3 className="text-sm font-medium text-gray-500 mb-1">
                                        Email
                                    </h3>
                                    <p className="text-base text-gray-900">
                                        {user.email}
                                    </p>
                                </div>

                                <div>
                                    <h3 className="text-sm font-medium text-gray-500 mb-1">
                                        Phone
                                    </h3>
                                    <p className="text-base text-gray-900">
                                        {(user as any).phone || 'N/A'}
                                    </p>
                                </div>

                                <div>
                                    <h3 className="text-sm font-medium text-gray-500 mb-1">
                                        Date of Birth
                                    </h3>
                                    <p className="text-base text-gray-900">
                                        {(user as any).dateOfBirth || 'N/A'}
                                    </p>
                                </div>

                                <div>
                                    <h3 className="text-sm font-medium text-gray-500 mb-1">
                                        Role
                                    </h3>
                                    <span className="inline-flex px-2 py-1 text-xs font-semibold rounded-full bg-blue-100 text-blue-800">
                                        {user.roles.join(', ')}
                                    </span>
                                </div>

                                <div className="md:col-span-2">
                                    <h3 className="text-sm font-medium text-gray-500 mb-1">
                                        Address
                                    </h3>
                                    <p className="text-base text-gray-900">
                                        {(user as any).address || 'N/A'}
                                    </p>
                                </div>
                            </div>

                            {user.roles.includes(Role.STUDENT) && (
                                <div className="pt-6 border-t">
                                    <h3 className="text-lg font-semibold text-gray-900 mb-4">
                                        Student Information
                                    </h3>
                                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                        <div>
                                            <h3 className="text-sm font-medium text-gray-500 mb-1">
                                                Student Number
                                            </h3>
                                            <p className="text-base text-gray-900">
                                                {(user as any).studentNumber ||
                                                    'N/A'}
                                            </p>
                                        </div>
                                        <div>
                                            <h3 className="text-sm font-medium text-gray-500 mb-1">
                                                Major
                                            </h3>
                                            <p className="text-base text-gray-900">
                                                {(user as any).major || 'N/A'}
                                            </p>
                                        </div>
                                        <div>
                                            <h3 className="text-sm font-medium text-gray-500 mb-1">
                                                Academic Year
                                            </h3>
                                            <p className="text-base text-gray-900">
                                                {(user as any).academicYear ||
                                                    'N/A'}
                                            </p>
                                        </div>
                                        <div>
                                            <h3 className="text-sm font-medium text-gray-500 mb-1">
                                                GPA
                                            </h3>
                                            <p className="text-base text-gray-900">
                                                {(user as any).gpa?.toFixed(
                                                    2
                                                ) || 'N/A'}
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            )}

                            {user.roles.includes(Role.EMPLOYEE) && (
                                <div className="pt-6 border-t">
                                    <h3 className="text-lg font-semibold text-gray-900 mb-4">
                                        Employment Information
                                    </h3>
                                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                                        <div>
                                            <h3 className="text-sm font-medium text-gray-500 mb-1">
                                                Employee ID
                                            </h3>
                                            <p className="text-base text-gray-900">
                                                {(user as any).employeeId ||
                                                    'N/A'}
                                            </p>
                                        </div>
                                        <div>
                                            <h3 className="text-sm font-medium text-gray-500 mb-1">
                                                Position
                                            </h3>
                                            <p className="text-base text-gray-900">
                                                {(user as any).position ||
                                                    'N/A'}
                                            </p>
                                        </div>
                                        <div>
                                            <h3 className="text-sm font-medium text-gray-500 mb-1">
                                                Hire Date
                                            </h3>
                                            <p className="text-base text-gray-900">
                                                {(user as any).hireDate ||
                                                    'N/A'}
                                            </p>
                                        </div>
                                    </div>
                                </div>
                            )}

                            <div className="flex justify-end gap-4 pt-6 border-t">
                                <button
                                    onClick={() => setIsEditing(true)}
                                    className="btn-primary"
                                >
                                    Edit Profile
                                </button>
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </ProtectedRoute>
    );
}
