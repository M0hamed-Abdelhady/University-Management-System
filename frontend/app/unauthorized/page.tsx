export default function UnauthorizedPage() {
    return (
        <div className="min-h-screen flex items-center justify-center">
            <div className="text-center">
                <div className="text-6xl mb-4">🚫</div>
                <h1 className="text-3xl font-bold text-gray-900 mb-2">
                    Access Denied
                </h1>
                <p className="text-gray-600 mb-6">
                    You don't have permission to access this page.
                </p>
                <a href="/dashboard" className="btn-primary inline-block">
                    Go to Dashboard
                </a>
            </div>
        </div>
    );
}
