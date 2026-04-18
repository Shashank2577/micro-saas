"use client";
import { useEffect, useState } from 'react';
import { api } from '@/lib/api';
import { Alert } from '@/types';

export default function AlertsPage() {
  const [alerts, setAlerts] = useState<Alert[]>([]);

  useEffect(() => {
    api.get('/alerts').then(res => setAlerts(res.data)).catch(console.error);
  }, []);

  const resolveAlert = async (id: string) => {
    await api.post(`/alerts/${id}/resolve`);
    setAlerts(alerts.filter(a => a.id !== id));
  };

  return (
    <div className="p-8 space-y-6">
      <h1 className="text-3xl font-bold">Manager Alerts</h1>
      <div className="space-y-4">
        {alerts.map(a => (
          <div key={a.id} className="p-4 border border-red-200 bg-red-50 rounded flex justify-between items-center">
            <div>
              <span className="font-bold text-red-700">[{a.severity}]</span> {a.message}
            </div>
            <button onClick={() => resolveAlert(a.id)} className="bg-white border text-sm px-3 py-1 rounded shadow-sm hover:bg-gray-50">
              Resolve
            </button>
          </div>
        ))}
        {alerts.length === 0 && <p className="text-gray-500">No active alerts.</p>}
      </div>
    </div>
  );
}
