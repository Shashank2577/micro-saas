'use client';
import React, { useState, useEffect } from 'react';

export default function ScorecardPage() {
    const [data, setData] = useState<any[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        // Mock fetch
        setTimeout(() => {
            setData([{ id: '1', name: 'Test Scorecards', status: 'ACTIVE' }]);
            setLoading(false);
        }, 500);
    }, []);

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-4">Scorecards</h1>
            {loading ? (
                <p>Loading...</p>
            ) : (
                <table className="min-w-full bg-white border" data-testid="scorecards-table">
                    <thead>
                        <tr>
                            <th className="py-2 px-4 border-b">ID</th>
                            <th className="py-2 px-4 border-b">Name</th>
                            <th className="py-2 px-4 border-b">Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        {data.map((item: any) => (
                            <tr key={item.id}>
                                <td className="py-2 px-4 border-b">{item.id}</td>
                                <td className="py-2 px-4 border-b">{item.name}</td>
                                <td className="py-2 px-4 border-b">{item.status}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            )}
        </div>
    );
}
