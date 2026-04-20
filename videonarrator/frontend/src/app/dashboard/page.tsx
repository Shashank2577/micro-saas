'use client';
import { useEffect, useState } from 'react';
import Link from 'next/link';
import api from '@/lib/api';

export default function DashboardPage() {
  const [projects, setProjects] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadProjects() {
      try {
        const res = await api.get('/projects');
        setProjects(res.data);
      } catch (err) {
        console.error('Failed to load projects', err);
      } finally {
        setLoading(false);
      }
    }
    loadProjects();
  }, []);

  if (loading) return <div>Loading dashboard...</div>;

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-2xl font-semibold">Your Projects</h2>
        <Link href="/projects/new" className="bg-blue-600 text-white px-4 py-2 rounded shadow hover:bg-blue-700">
          New Project
        </Link>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {projects.length === 0 ? (
          <p className="text-gray-500">No projects yet. Create one!</p>
        ) : (
          projects.map((p: any) => (
            <Link key={p.id} href={`/projects/${p.id}`} className="block border rounded-lg p-4 bg-white shadow hover:shadow-md transition">
              <h3 className="font-medium text-lg mb-2">{p.title}</h3>
              <p className="text-sm text-gray-500">Status: {p.status}</p>
            </Link>
          ))
        )}
      </div>
    </div>
  );
}
