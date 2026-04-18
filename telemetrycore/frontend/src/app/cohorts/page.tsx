"use client";

import { useEffect, useState } from 'react';
import api from '@/lib/api';

interface Cohort {
    id: string;
    name: string;
    criteria: any;
}

export default function CohortsPage() {
    const [cohorts, setCohorts] = useState<Cohort[]>([]);

    useEffect(() => {
        api.get('/cohorts').then(res => setCohorts(res.data)).catch(console.error);
    }, []);

    return (
        <div className="p-8">
            <h1 className="text-3xl font-bold mb-6">Cohorts</h1>
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                {cohorts.map(cohort => (
                    <div key={cohort.id} className="bg-white p-6 rounded-lg shadow-md border border-gray-200">
                        <h2 className="text-xl font-semibold">{cohort.name}</h2>
                        <pre className="mt-4 p-4 bg-gray-50 rounded text-sm text-gray-700">
                            {JSON.stringify(cohort.criteria, null, 2)}
                        </pre>
                    </div>
                ))}
                {cohorts.length === 0 && (
                    <p className="text-gray-500">No cohorts defined.</p>
                )}
            </div>
        </div>
    );
}
