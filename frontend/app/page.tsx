'use client';

import { useEffect } from 'react';
import { useRouter, usePathname } from 'next/navigation';
import { useAuth } from '@/contexts/AuthContext';
import LoadingSpinner from '@/components/LoadingSpinner';

export default function Home() {
    const { user, loading } = useAuth();
    const router = useRouter();
    const pathname = usePathname();

    useEffect(() => {
        // Only redirect if we're actually on the root path
        if (!loading && pathname === '/') {
            if (user) {
                router.replace('/dashboard');
            } else {
                router.replace('/login');
            }
        }
    }, [user, loading, router, pathname]);

    return (
        <div className="min-h-screen flex items-center justify-center">
            <LoadingSpinner message="Loading..." />
        </div>
    );
}
