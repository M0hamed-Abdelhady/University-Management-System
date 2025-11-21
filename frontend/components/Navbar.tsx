'use client';

import Link from 'next/link';
import { useRouter, usePathname } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';
import { Role } from '@/lib/types';

export default function Navbar() {
    const { user, logout } = useAuth();
    const router = useRouter();
    const pathname = usePathname();

    const handleLogout = () => {
        logout();
        router.push('/login');
    };

    const isActive = (path: string) => pathname.startsWith(path);

    if (!user) return null;

    return (
        <nav className="bg-white border-b border-blue-100 shadow-sm">
            <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
                <div className="flex justify-between h-16">
                    <div className="flex items-center space-x-8">
                        <Link
                            href="/dashboard"
                            className="text-xl font-bold text-blue-500"
                        >
                            UMS
                        </Link>

                        <div className="hidden md:flex space-x-1">
                            <Link
                                href="/dashboard"
                                className={`px-4 py-2 rounded-lg font-medium transition-colors ${
                                    pathname === '/dashboard'
                                        ? 'bg-blue-50 text-blue-600'
                                        : 'text-gray-600 hover:bg-blue-50 hover:text-blue-600'
                                }`}
                            >
                                Dashboard
                            </Link>

                            {user.roles.includes(Role.ADMIN) && (
                                <>
                                    <Link
                                        href="/students"
                                        className={`px-4 py-2 rounded-lg font-medium transition-colors ${
                                            isActive('/students')
                                                ? 'bg-blue-50 text-blue-600'
                                                : 'text-gray-600 hover:bg-blue-50 hover:text-blue-600'
                                        }`}
                                    >
                                        Students
                                    </Link>
                                    <Link
                                        href="/employees"
                                        className={`px-4 py-2 rounded-lg font-medium transition-colors ${
                                            isActive('/employees')
                                                ? 'bg-blue-50 text-blue-600'
                                                : 'text-gray-600 hover:bg-blue-50 hover:text-blue-600'
                                        }`}
                                    >
                                        Employees
                                    </Link>
                                </>
                            )}

                            {(user.roles.includes(Role.ADMIN) ||
                                user.roles.includes(Role.EMPLOYEE)) && (
                                <Link
                                    href="/enrollments"
                                    className={`px-4 py-2 rounded-lg font-medium transition-colors ${
                                        isActive('/enrollments')
                                            ? 'bg-blue-50 text-blue-600'
                                            : 'text-gray-600 hover:bg-blue-50 hover:text-blue-600'
                                    }`}
                                >
                                    Enrollments
                                </Link>
                            )}

                            <Link
                                href="/courses"
                                className={`px-4 py-2 rounded-lg font-medium transition-colors ${
                                    isActive('/courses')
                                        ? 'bg-blue-50 text-blue-600'
                                        : 'text-gray-600 hover:bg-blue-50 hover:text-blue-600'
                                }`}
                            >
                                Courses
                            </Link>

                            <Link
                                href="/classes"
                                className={`px-4 py-2 rounded-lg font-medium transition-colors ${
                                    isActive('/classes')
                                        ? 'bg-blue-50 text-blue-600'
                                        : 'text-gray-600 hover:bg-blue-50 hover:text-blue-600'
                                }`}
                            >
                                Classes
                            </Link>
                        </div>
                    </div>

                    <div className="flex items-center space-x-4">
                        <Link
                            href="/profile"
                            className="text-sm font-medium text-gray-700 hover:text-blue-600 transition-colors hidden sm:block"
                        >
                            <div className="text-right">
                                <p className="text-sm font-medium text-gray-900">
                                    {user.firstName} {user.lastName}
                                </p>
                                <p className="text-xs text-gray-500">
                                    {user.roles.join(', ')}
                                </p>
                            </div>
                        </Link>
                        <button
                            onClick={handleLogout}
                            className="btn-secondary text-sm"
                        >
                            Logout
                        </button>
                    </div>
                </div>
            </div>
        </nav>
    );
}
