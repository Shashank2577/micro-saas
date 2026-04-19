"use client";

import { useEffect, useState } from 'react';
import api from '@/lib/api';

interface Framework {
  id: string;
  name: string;
  description: string;
  version: string;
}

export default function FrameworksPage() {
  const [frameworks, setFrameworks] = useState<Framework[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchData() {
      try {
        const response = await api.get('/frameworks');
        setFrameworks(response.data);
      } catch (error) {
        console.error("Failed to fetch frameworks", error);
      } finally {
        setLoading(false);
      }
    }
    
    fetchData();
  }, []);

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">Control Frameworks</h1>
        <button className="bg-indigo-600 text-white px-4 py-2 rounded shadow hover:bg-indigo-700">
          Add Framework
        </button>
      </div>
      
      {loading ? (
        <div>Loading...</div>
      ) : (
        <div className="bg-white shadow rounded-lg overflow-hidden">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Version</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Description</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {frameworks.length === 0 ? (
                <tr>
                  <td colSpan={3} className="px-6 py-4 text-center text-sm text-gray-500">No frameworks found</td>
                </tr>
              ) : (
                frameworks.map(fw => (
                  <tr key={fw.id}>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{fw.name}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{fw.version}</td>
                    <td className="px-6 py-4 text-sm text-gray-500">{fw.description}</td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
