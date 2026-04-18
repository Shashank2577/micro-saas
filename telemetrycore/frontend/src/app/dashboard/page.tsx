"use client";

import { useEffect, useState } from 'react';
import api from '@/lib/api';

export default function DashboardPage() {
    const [activeUsers, setActiveUsers] = useState<number>(0);

    useEffect(() => {
        setActiveUsers(1024);
        
        const interval = setInterval(() => {
            setActiveUsers(prev => prev + Math.floor(Math.random() * 10) - 5);
        }, 60000);

        return () => clearInterval(interval);
    }, []);

    return (
        <div className="p-8">
            <h1 className="text-3xl font-bold mb-6">TelemetryCore Dashboard</h1>
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                <div className="bg-white p-6 rounded-lg shadow-md border border-gray-200">
                    <h2 className="text-lg font-semibold text-gray-700">Active Users</h2>
                    <p className="text-4xl font-bold text-blue-600 mt-2" data-testid="active-users-count">
                        {activeUsers}
                    </p>
                </div>
            </div>
        </div>
    );
}
