'use client';

import React, { createContext, useContext, useState, useEffect } from 'react';
import { User, Role } from '@/lib/types';
import { authApi, studentApi, employeeApi } from '@/lib/api';
import { apiClient } from '@/lib/api-client';

interface AuthContextType {
    user: User | null;
    loading: boolean;
    login: (email: string, password: string) => Promise<void>;
    register: (
        firstName: string,
        lastName: string,
        email: string,
        password: string
    ) => Promise<void>;
    logout: () => void;
    refreshUser: () => Promise<void>;
    hasRole: (role: Role) => boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
    const [user, setUser] = useState<User | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Load user from localStorage on mount
        const storedUser = localStorage.getItem('user');
        const storedToken = localStorage.getItem('token');

        if (storedUser && storedToken) {
            setUser(JSON.parse(storedUser));
        }
        setLoading(false);
    }, []);

    const login = async (email: string, password: string) => {
        const response = await authApi.login({ email, password });
        // Handle both "User" (capital) and "user" (lowercase) from backend
        const userData =
            (response.data as any).User || (response.data as any).user;
        console.log('Login response:', response.data);
        console.log('Login response userData:', userData);

        if (userData && userData.token) {
            apiClient.setToken(userData.token);
            localStorage.setItem('token', userData.token);
            localStorage.setItem('user', JSON.stringify(userData));
            setUser(userData);
        } else {
            throw new Error('Invalid response from server');
        }
    };

    const register = async (
        firstName: string,
        lastName: string,
        email: string,
        password: string
    ) => {
        const response = await authApi.register({
            firstName,
            lastName,
            email,
            password,
        });
        // Handle both "User" (capital) and "user" (lowercase) from backend
        const userData =
            (response.data as any).User || (response.data as any).user;

        if (userData && userData.token) {
            apiClient.setToken(userData.token);
            localStorage.setItem('token', userData.token);
            localStorage.setItem('user', JSON.stringify(userData));
            setUser(userData);
        } else {
            throw new Error('Invalid response from server');
        }
    };

    const logout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
        setUser(null);
    };

    const refreshUser = async () => {
        try {
            let profile: any = null;

            // Check user role and call appropriate endpoint
            if (user?.roles.includes(Role.STUDENT)) {
                const response = await studentApi.getProfile();
                const studentData =
                    (response.data as any)?.Student ||
                    (response.data as any)?.student;
                if (studentData) {
                    profile = {
                        id: studentData.id,
                        firstName: studentData.person.firstName,
                        lastName: studentData.person.lastName,
                        email: studentData.person.email,
                        phone: studentData.person.phone,
                        dateOfBirth: studentData.person.dateOfBirth,
                        address: studentData.person.address,
                        roles: [studentData.person.role],
                        studentNumber: studentData.studentNumber,
                        major: studentData.major,
                        academicYear: studentData.academicYear,
                        gpa: studentData.gpa,
                        status: studentData.status,
                    };
                }
            } else if (user?.roles.includes(Role.EMPLOYEE)) {
                const response = await employeeApi.getProfile();
                const employeeData =
                    (response.data as any)?.Employee ||
                    (response.data as any)?.employee;
                if (employeeData) {
                    profile = {
                        id: employeeData.id,
                        firstName: employeeData.person.firstName,
                        lastName: employeeData.person.lastName,
                        email: employeeData.person.email,
                        phone: employeeData.person.phone,
                        dateOfBirth: employeeData.person.dateOfBirth,
                        address: employeeData.person.address,
                        roles: [employeeData.person.role],
                        employeeId: employeeData.employeeId,
                        hireDate: employeeData.hireDate,
                        salary: employeeData.salary,
                        position: employeeData.position,
                        status: employeeData.status,
                    };
                }
            } else {
                // Admin or other roles - use auth/me
                const response = await authApi.getCurrentUser();
                profile =
                    (response.data as any)?.User ||
                    (response.data as any)?.user;
            }

            if (!profile || !profile.id) {
                console.error('Invalid profile response');
                return;
            }

            const updatedUser: User = {
                ...user,
                ...profile,
                token: user?.token,
            };

            localStorage.setItem('user', JSON.stringify(updatedUser));
            setUser(updatedUser);
        } catch (error) {
            console.error('Failed to refresh user:', error);
            // Don't logout on refresh error, just log it
        }
    };

    const hasRole = (role: Role): boolean => {
        return user?.roles.includes(role) || false;
    };

    return (
        <AuthContext.Provider
            value={{
                user,
                loading,
                login,
                register,
                logout,
                refreshUser,
                hasRole,
            }}
        >
            {children}
        </AuthContext.Provider>
    );
}

export function useAuth() {
    const context = useContext(AuthContext);
    if (context === undefined) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
}
