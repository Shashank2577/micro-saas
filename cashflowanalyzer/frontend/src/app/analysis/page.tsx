'use client';
import { useEffect, useState } from 'react';
import { fetchMovements, CashMovement } from '@/lib/api';
import { DataTable } from '@/components/DataTable';

export default function AnalysisPage() {
    const [data, setData] = useState<CashMovement[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchMovements()
            .then(setData)
            .catch(console.error)
            .finally(() => setLoading(false));
    }, []);

    if (loading) return <div data-testid="loading">Loading...</div>;

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-4">Cash Movements Analysis</h1>
            <div className="bg-white p-4 shadow rounded">
                <h2 className="text-lg font-semibold mb-2">Movements</h2>
                <DataTable data={data} columns={['Name', 'Status']} />
            </div>
            {data.length === 0 && <p className="mt-4">No movements found.</p>}
        </div>
    );
}
