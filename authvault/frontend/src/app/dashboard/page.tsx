'use client';
import React from 'react';

export default function DashboardPage() {
    return (
        <div className="min-h-screen p-8">
            <h1 className="text-3xl font-bold mb-6">AuthVault Dashboard</h1>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                <div className="p-6 bg-white rounded shadow text-black">
                    <h2 className="text-xl font-semibold mb-2">Users</h2>
                    <p className="text-gray-600">Manage user accounts and MFA settings.</p>
                </div>
                <div className="p-6 bg-white rounded shadow text-black">
                    <h2 className="text-xl font-semibold mb-2">Roles & Permissions</h2>
                    <p className="text-gray-600">Configure RBAC policies and assignments.</p>
                </div>
                <div className="p-6 bg-white rounded shadow text-black">
                    <h2 className="text-xl font-semibold mb-2">OAuth Apps</h2>
                    <p className="text-gray-600">Register and manage OAuth2 clients.</p>
                </div>
            </div>
        </div>
    );
}
