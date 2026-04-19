"use client";

import useSWR from 'swr';
import React, { useState } from 'react';

const fetcher = (url: string) => fetch(url, { headers: { 'X-Tenant-ID': '00000000-0000-0000-0000-000000000000' } }).then((res) => res.json());

export function SdkArtifactList() {
  const { data, error, isLoading } = useSWR('/api/v1/api-evolution/sdk-artifacts', fetcher);
  const [formData, setFormData] = useState({ name: '', status: 'BUILDING', metadataJson: '{}' });

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await fetch('/api/v1/api-evolution/sdk-artifacts', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-Tenant-ID': '00000000-0000-0000-0000-000000000000',
      },
      body: JSON.stringify(formData),
    });
    window.location.reload();
  };

  if (error) return <div className="text-red-500">Failed to load SDK Artifacts.</div>;
  if (isLoading) return <div className="text-gray-500">Loading SDK Artifacts...</div>;

  return (
    <div className="space-y-6">
      <div className="bg-white p-4 rounded shadow">
        <h2 className="text-xl font-semibold mb-4">Request SDK Generation</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700">Name</label>
            <input type="text" className="mt-1 block w-full border border-gray-300 rounded-md shadow-sm p-2" required value={formData.name} onChange={e => setFormData({...formData, name: e.target.value})} />
          </div>
          <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700">Submit</button>
        </form>
      </div>

      <div className="bg-white rounded shadow overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {data?.map((item: any) => (
              <tr key={item.id}>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{item.id}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{item.name}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{item.status}</td>
              </tr>
            ))}
          </tbody>
        </table>
        {(!data || data.length === 0) && (
          <div className="p-4 text-gray-500 text-center">No SDK Artifacts found.</div>
        )}
      </div>
    </div>
  );
}
