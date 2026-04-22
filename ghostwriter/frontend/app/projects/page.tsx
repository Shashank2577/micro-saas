'use client';
import { useState, useEffect } from 'react';

export default function ProjectsPage() {
    const [projects, setProjects] = useState([]);

    useEffect(() => {
        fetch('/api/projects', { headers: { 'X-Tenant-ID': 'default-tenant' } })
            .then(res => res.json())
            .then(data => setProjects(data))
            .catch(err => console.error(err));
    }, []);

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-4">Projects</h1>
            <div className="space-y-4">
                {projects.map((proj: any) => (
                    <div key={proj.id} className="border p-4 rounded shadow">
                        <h2 className="text-xl font-semibold">{proj.name}</h2>
                        <p className="text-gray-600">{proj.description}</p>
                    </div>
                ))}
                {projects.length === 0 && <p>No projects found.</p>}
            </div>
        </div>
    );
}
