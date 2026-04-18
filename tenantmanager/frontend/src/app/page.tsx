"use client";

import { useEffect, useState } from 'react';
import api from '../lib/api';
import Link from 'next/link';

interface Tenant {
  id: string;
  name: string;
  status: string;
  healthScore: number;
  churnRisk: string;
}

export default function DashboardPage() {
  const [tenants, setTenants] = useState<Tenant[]>([]);

  useEffect(() => {
    api.get('/api/tenants').then(res => setTenants(res.data)).catch(console.error);
  }, []);

  const totalTenants = tenants.length;
  const avgHealth = totalTenants > 0 ? Math.round(tenants.reduce((acc, t) => acc + t.healthScore, 0) / totalTenants) : 0;
  const highRisk = tenants.filter(t => t.churnRisk === 'HIGH').length;

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">Dashboard</h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
        <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100">
          <h2 className="text-gray-500 text-sm font-medium mb-2">Total Tenants</h2>
          <p className="text-3xl font-bold text-gray-900">{totalTenants}</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100">
          <h2 className="text-gray-500 text-sm font-medium mb-2">Avg Health Score</h2>
          <p className="text-3xl font-bold text-blue-600">{avgHealth}/100</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100">
          <h2 className="text-gray-500 text-sm font-medium mb-2">High Risk Tenants</h2>
          <p className="text-3xl font-bold text-red-600">{highRisk}</p>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-sm border border-gray-100 p-6">
        <h2 className="text-xl font-semibold mb-4">Recent Tenants</h2>
        <div className="divide-y">
          {tenants.slice(0, 5).map(t => (
            <div key={t.id} className="py-4 flex justify-between items-center">
              <div>
                <Link href={`/tenants/${t.id}`} className="font-medium text-blue-600 hover:underline">{t.name}</Link>
                <p className="text-sm text-gray-500">Status: {t.status}</p>
              </div>
              <div className="text-right">
                <p className="font-medium">Health: {t.healthScore}</p>
                <p className={`text-sm ${t.churnRisk === 'HIGH' ? 'text-red-500' : 'text-green-500'}`}>Risk: {t.churnRisk}</p>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
