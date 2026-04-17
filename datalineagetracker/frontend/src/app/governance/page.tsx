"use client";

import { useEffect, useState } from 'react';
import { GovernancePolicy } from '@/types';

export default function GovernancePage() {
    const [policies, setPolicies] = useState<GovernancePolicy[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetch('http://localhost:8167/api/v1/policies', {
            headers: {
                'X-Tenant-ID': '00000000-0000-0000-0000-000000000001'
            }
        })
        .then(res => res.json())
        .then(data => {
            setPolicies(data);
            setLoading(false);
        })
        .catch(err => {
            console.error(err);
            setLoading(false);
        });
    }, []);

    if (loading) return <div className="p-8">Loading policies...</div>;

    return (
        <div className="max-w-7xl mx-auto p-8">
            <div className="flex justify-between items-center mb-6">
                <h1 className="text-2xl font-bold">Governance Policies</h1>
                <button className="bg-indigo-600 text-white px-4 py-2 rounded">Create Policy</button>
            </div>
            
            <div className="bg-white shadow overflow-hidden sm:rounded-md">
                <ul className="divide-y divide-gray-200">
                    {policies.map(policy => (
                        <li key={policy.id} className="px-4 py-4 sm:px-6">
                            <div className="flex items-center justify-between">
                                <h3 className="text-lg font-medium text-indigo-600">{policy.name}</h3>
                                <div className="ml-2 flex-shrink-0 flex">
                                    <p className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full ${policy.isActive ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}`}>
                                        {policy.isActive ? 'Active' : 'Inactive'}
                                    </p>
                                </div>
                            </div>
                            <div className="mt-2 sm:flex sm:justify-between">
                                <div className="sm:flex">
                                    <p className="flex items-center text-sm text-gray-500">
                                        Type: {policy.policyType}
                                    </p>
                                </div>
                            </div>
                            <p className="mt-2 text-sm text-gray-600">{policy.description}</p>
                        </li>
                    ))}
                    {policies.length === 0 && (
                        <li className="px-4 py-8 text-center text-gray-500">No policies found.</li>
                    )}
                </ul>
            </div>
        </div>
    );
}
