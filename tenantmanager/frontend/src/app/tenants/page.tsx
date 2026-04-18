"use client";

import { useEffect, useState } from 'react';
import api from '../../lib/api';
import Link from 'next/link';

interface Tenant {
  id: string;
  name: string;
  status: string;
  healthScore: number;
  churnRisk: string;
}

export default function TenantsPage() {
  const [tenants, setTenants] = useState<Tenant[]>([]);
  const [name, setName] = useState('');

  const loadTenants = () => {
    api.get('/api/tenants').then(res => setTenants(res.data)).catch(console.error);
  };

  useEffect(() => {
    loadTenants();
  }, []);

  const createTenant = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim()) return;
    try {
      await api.post('/api/tenants', { name });
      setName('');
      loadTenants();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold">Tenants</h1>
      </div>

      <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100 mb-8">
        <h2 className="text-lg font-semibold mb-4">Add New Tenant</h2>
        <form onSubmit={createTenant} className="flex gap-4">
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            placeholder="Tenant Name"
            className="flex-1 border p-2 rounded"
          />
          <button type="submit" className="bg-blue-600 text-white px-4 py-2 rounded">Create</button>
        </form>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-100">
        <table className="w-full text-left">
          <thead className="bg-gray-50 border-b">
            <tr>
              <th className="p-4">Name</th>
              <th className="p-4">Status</th>
              <th className="p-4">Health Score</th>
              <th className="p-4">Churn Risk</th>
            </tr>
          </thead>
          <tbody>
            {tenants.map(t => (
              <tr key={t.id} className="border-b last:border-b-0 hover:bg-gray-50">
                <td className="p-4">
                  <Link href={`/tenants/${t.id}`} className="text-blue-600 hover:underline">
                    {t.name}
                  </Link>
                </td>
                <td className="p-4">{t.status}</td>
                <td className="p-4">
                  <span className={`px-2 py-1 rounded text-sm ${t.healthScore >= 80 ? 'bg-green-100 text-green-800' : t.healthScore >= 50 ? 'bg-yellow-100 text-yellow-800' : 'bg-red-100 text-red-800'}`}>
                    {t.healthScore}
                  </span>
                </td>
                <td className="p-4">{t.churnRisk}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
