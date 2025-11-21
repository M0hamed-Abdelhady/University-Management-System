'use client';

import { useAuth } from '@/contexts/AuthContext';
import { Role } from '@/lib/types';
import ProtectedRoute from '@/components/ProtectedRoute';
import Navbar from '@/components/Navbar';
import Link from 'next/link';

export default function DashboardPage() {
    const { user } = useAuth();

    return (
        <ProtectedRoute>
            <div className="min-h-screen">
                <Navbar />
                <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    <div className="mb-8">
                        <h1 className="text-3xl font-bold text-gray-900 mb-2">
                            Welcome back, {user?.firstName}!
                        </h1>
                        <p className="text-gray-600">
                            Manage your university operations from here
                        </p>
                    </div>

                    {/* Admin Dashboard */}
                    {user?.roles.includes(Role.ADMIN) && (
                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                            <DashboardCard
                                title="Students"
                                description="Manage student records and enrollments"
                                href="/students"
                                icon="👨‍🎓"
                            />
                            <DashboardCard
                                title="Employees"
                                description="Manage staff and faculty members"
                                href="/employees"
                                icon="👨‍💼"
                            />
                            <DashboardCard
                                title="Courses"
                                description="Manage course catalog"
                                href="/courses"
                                icon="📚"
                            />
                            <DashboardCard
                                title="Classes"
                                description="Manage class schedules and capacity"
                                href="/classes"
                                icon="🏫"
                            />
                            <DashboardCard
                                title="Enrollments"
                                description="View and manage student enrollments"
                                href="/enrollments"
                                icon="📝"
                            />
                        </div>
                    )}

                    {/* Employee Dashboard */}
                    {user?.roles.includes(Role.EMPLOYEE) &&
                        !user?.roles.includes(Role.ADMIN) && (
                            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                                <DashboardCard
                                    title="Students"
                                    description="View student records"
                                    href="/students"
                                    icon="👨‍🎓"
                                />
                                <DashboardCard
                                    title="Courses"
                                    description="Browse available courses"
                                    href="/courses"
                                    icon="📚"
                                />
                                <DashboardCard
                                    title="Classes"
                                    description="View class schedules"
                                    href="/classes"
                                    icon="🏫"
                                />
                                <DashboardCard
                                    title="Enrollments"
                                    description="Manage student enrollments"
                                    href="/enrollments"
                                    icon="📝"
                                />
                            </div>
                        )}

                    {/* Student Dashboard */}
                    {user?.roles.includes(Role.STUDENT) &&
                        !user?.roles.includes(Role.ADMIN) &&
                        !user?.roles.includes(Role.EMPLOYEE) && (
                            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                                <DashboardCard
                                    title="My Enrollments"
                                    description="View your enrolled classes"
                                    href="/my-enrollments"
                                    icon="📝"
                                />
                                <DashboardCard
                                    title="Available Courses"
                                    description="Browse available courses"
                                    href="/courses"
                                    icon="📚"
                                />
                                <DashboardCard
                                    title="Available Classes"
                                    description="Find and enroll in classes"
                                    href="/classes"
                                    icon="🏫"
                                />
                            </div>
                        )}
                </div>
            </div>
        </ProtectedRoute>
    );
}

interface DashboardCardProps {
    title: string;
    description: string;
    href: string;
    icon: string;
}

function DashboardCard({ title, description, href, icon }: DashboardCardProps) {
    return (
        <Link href={href}>
            <div className="card hover:shadow-lg transition-shadow duration-200 cursor-pointer group">
                <div className="text-4xl mb-4">{icon}</div>
                <h3 className="text-xl font-semibold text-gray-900 mb-2 group-hover:text-blue-500 transition-colors">
                    {title}
                </h3>
                <p className="text-gray-600">{description}</p>
            </div>
        </Link>
    );
}
