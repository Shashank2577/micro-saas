"use client";
import React from 'react';
import { useQuery } from '@tanstack/react-query';

export default function TemplatesPage() {
  const { data: templates = [], isLoading, error } = useQuery({
    queryKey: ['templates'],
    queryFn: async () => {
      const res = await fetch('http://localhost:8111/templates', {
        headers: { 'X-Tenant-ID': 'default-tenant' }
      });
      if (!res.ok) throw new Error('Failed to fetch templates');
      return res.json();
    }
  });

  if (isLoading) return <div className="p-8">Loading templates...</div>;
  if (error) return <div className="p-8 text-red-500">Error loading templates</div>;

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Templates</h1>
        <button className="bg-blue-600 text-white px-4 py-2 rounded">Create Template</button>
      </div>
      <div className="bg-white rounded-lg shadow overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Channel</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Created</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {templates.length === 0 ? (
               <tr>
                 <td colSpan={3} className="px-6 py-4 text-center text-gray-500">No templates found</td>
               </tr>
            ) : (
              templates.map((t: any) => (
                <tr key={t.id}>
                  <td className="px-6 py-4 whitespace-nowrap">{t.name}</td>
                  <td className="px-6 py-4 whitespace-nowrap">{t.channel}</td>
                  <td className="px-6 py-4 whitespace-nowrap">{new Date(t.createdAt).toLocaleDateString()}</td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
