"use client";

import { useEffect, useState } from 'react';
import api from '@/lib/api';

interface Metric {
    id: string;
    name: string;
    description: string;
    aggregationType: string;
}

export default function MetricsPage() {
    const [metrics, setMetrics] = useState<Metric[]>([]);

    useEffect(() => {
        api.get('/metrics').then(res => setMetrics(res.data)).catch(console.error);
    }, []);

    return (
        <div className="p-8">
            <h1 className="text-3xl font-bold mb-6">Metrics Definition</h1>
            <div className="bg-white rounded-lg shadow">
                <table className="min-w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                        <tr>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Description</th>
                            <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase">Aggregation Type</th>
                        </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                        {metrics.map(metric => (
                            <tr key={metric.id}>
                                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{metric.name}</td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{metric.description}</td>
                                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{metric.aggregationType}</td>
                            </tr>
                        ))}
                        {metrics.length === 0 && (
                            <tr>
                                <td colSpan={3} className="px-6 py-4 text-center text-sm text-gray-500">No metrics found.</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}
