'use client';

import React, { useState, useEffect } from 'react';
import { api } from '../lib/api';
import Link from 'next/link';

interface ApiProject {
  id: string;
  name: string;
  description: string;
  createdAt: string;
}

export default function Dashboard() {
  const [projects, setProjects] = useState<ApiProject[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    loadProjects();
  }, []);

  const loadProjects = async () => {
    try {
      const res = await api.get('/api/v1/projects');
      setProjects(res.data);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const createDemoProject = async () => {
    try {
      await api.post('/api/v1/projects', {
        name: 'Demo API',
        description: 'A sample API project for testing API Manager features.',
        baseUrl: 'https://api.demo.com'
      });
      loadProjects();
    } catch (e) {
      console.error(e);
    }
  };

  if (loading) return <div className="p-8">Loading...</div>;

  return (
    <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-900">API Projects</h1>
        <button 
          onClick={createDemoProject}
          className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
        >
          Create New Project
        </button>
      </div>

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
        {projects.map(project => (
          <div key={project.id} className="bg-white overflow-hidden shadow rounded-lg border border-gray-200">
            <div className="p-5">
              <h3 className="text-lg font-medium text-gray-900 truncate">{project.name}</h3>
              <p className="mt-1 text-sm text-gray-500 h-10 overflow-hidden">{project.description}</p>
              <div className="mt-4">
                <Link 
                  href={`/projects/${project.id}`}
                  className="text-blue-600 hover:text-blue-900 text-sm font-medium"
                >
                  View Details &rarr;
                </Link>
              </div>
            </div>
          </div>
        ))}
        {projects.length === 0 && (
          <div className="col-span-full text-center py-12 bg-white rounded-lg border border-gray-200 shadow-sm">
            <p className="text-gray-500 mb-4">No projects found.</p>
            <button 
              onClick={createDemoProject}
              className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
            >
              Create Demo Project
            </button>
          </div>
        )}
      </div>
    </div>
  );
}
