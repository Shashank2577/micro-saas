'use client';
import { useState, useEffect } from 'react';

export default function PersonasPage() {
    const [personas, setPersonas] = useState([]);

    useEffect(() => {
        fetch('/api/personas', { headers: { 'X-Tenant-ID': 'default-tenant' } })
            .then(res => res.json())
            .then(data => setPersonas(data))
            .catch(err => console.error(err));
    }, []);

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-4">Personas</h1>
            <div className="space-y-4">
                {personas.map((per: any) => (
                    <div key={per.id} className="border p-4 rounded shadow">
                        <h2 className="text-xl font-semibold">{per.name}</h2>
                        <p className="text-gray-600">{per.description} - Tone: {per.tone}</p>
                    </div>
                ))}
                {personas.length === 0 && <p>No personas found.</p>}
            </div>
        </div>
    );
}
