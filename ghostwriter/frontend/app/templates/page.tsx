'use client';
import { useState, useEffect } from 'react';

export default function TemplatesPage() {
    const [templates, setTemplates] = useState([]);

    useEffect(() => {
        fetch('/api/templates', { headers: { 'X-Tenant-ID': 'default-tenant' } })
            .then(res => res.json())
            .then(data => setTemplates(data))
            .catch(err => console.error(err));
    }, []);

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-4">Templates</h1>
            <div className="space-y-4">
                {templates.map((tpl: any) => (
                    <div key={tpl.id} className="border p-4 rounded shadow">
                        <h2 className="text-xl font-semibold">{tpl.name}</h2>
                        <pre className="text-gray-600 text-sm">{tpl.structure}</pre>
                    </div>
                ))}
                {templates.length === 0 && <p>No templates found.</p>}
            </div>
        </div>
    );
}
