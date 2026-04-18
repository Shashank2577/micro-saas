"use client";
import { useEffect, useState } from 'react';
import { api } from '@/lib/api';
export default function DashboardPage() {
  const [trends, setTrends] = useState<any>(null);
  useEffect(() => {
    api.get('/analytics/trends').then(res => setTrends(res.data)).catch(console.error);
  }, []);
  return (
    <div className="p-8 space-y-6">
      <h1 className="text-3xl font-bold">EngagementPulse Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100">
          <h3 className="text-gray-500 text-sm font-medium">Responses (30d)</h3>
          <p className="text-3xl font-bold mt-2">{trends?.totalResponsesInLast30Days || 0}</p>
        </div>
      </div>
    </div>
  );
}
