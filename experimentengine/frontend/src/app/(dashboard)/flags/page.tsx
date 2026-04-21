'use client';

import { useState, useEffect } from 'react';

export default function FlagsPage() {
    const [flags, setFlags] = useState([]);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        fetch('/api/feature-flags', { headers: { 'X-Tenant-ID': '00000000-0000-0000-0000-000000000000' }})
            .then(res => res.json())
            .then(data => {
                if(Array.isArray(data)) setFlags(data as any);
            })
            .catch(err => console.error(err))
            .finally(() => setIsLoading(false));
    }, []);

    const toggleFlag = (id: string) => {
        fetch(`/api/feature-flags/${id}/toggle`, {
            method: 'PATCH',
            headers: { 'X-Tenant-ID': '00000000-0000-0000-0000-000000000000' }
        })
        .then(res => res.json())
        .then(updatedFlag => {
            setFlags(flags.map((flag: any) => flag.id === id ? updatedFlag : flag) as any);
        })
        .catch(console.error);
    };

    if (isLoading) return <div>Loading...</div>;

    return (
        <div className="p-6">
            <h1 className="text-2xl font-bold mb-4">Feature Flags</h1>
            <div className="grid gap-4">
                {flags.map((flag: any) => (
                    <div key={flag.id} className="p-4 border rounded shadow-sm flex justify-between items-center">
                        <div>
                            <h2 className="font-semibold">{flag.flagKey}</h2>
                            <p className="text-gray-600">{flag.description}</p>
                        </div>
                        <div>
                            <button
                                onClick={() => toggleFlag(flag.id)}
                                className={`px-2 py-1 rounded text-sm ${flag.enabled ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'}`}
                            >
                                {flag.enabled ? 'Enabled' : 'Disabled'}
                            </button>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
