'use client';

import { useState, useEffect } from 'react';

export default function GoalsPage() {
    const [goals, setGoals] = useState<any[]>([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        fetch('/api/goals', { headers: { 'X-Tenant-ID': '00000000-0000-0000-0000-000000000000' }})
            .then(res => res.json())
            .then(data => {
                if(Array.isArray(data)) setGoals(data);
            })
            .catch(err => console.error(err))
            .finally(() => setIsLoading(false));
    }, []);

    if (isLoading) return <div>Loading...</div>;

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-4">Goals</h1>
            <div className="grid gap-4">
                {goals.map((goal: any) => (
                    <div key={goal.id} className="p-4 border rounded shadow-sm">
                        <h2 className="font-semibold">{goal.name}</h2>
                        <p className="text-gray-600">{goal.description}</p>
                    </div>
                ))}
            </div>
        </div>
    );
}
