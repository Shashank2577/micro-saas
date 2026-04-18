"use client";

import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';
import api from '../../../lib/api';

interface Tenant {
  id: string;
  name: string;
  status: string;
  healthScore: number;
  churnRisk: string;
}

interface Milestone {
  id: string;
  title: string;
  description: string;
  status: string;
}

interface Event {
  id: string;
  eventType: string;
  description: string;
  occurredAt: string;
}

export default function TenantDetailPage() {
  const params = useParams();
  const [tenant, setTenant] = useState<Tenant | null>(null);
  const [milestones, setMilestones] = useState<Milestone[]>([]);
  const [events, setEvents] = useState<Event[]>([]);
  const [loadingAI, setLoadingAI] = useState(false);

  const loadData = async () => {
    try {
      const [tRes, mRes, eRes] = await Promise.all([
        api.get(`/api/tenants/${params.id}`),
        api.get(`/api/tenants/${params.id}/milestones`),
        api.get(`/api/tenants/${params.id}/events`)
      ]);
      setTenant(tRes.data);
      setMilestones(mRes.data);
      setEvents(eRes.data);
    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    if (params.id) loadData();
  }, [params.id]);

  const completeMilestone = async (mId: string) => {
    await api.put(`/api/milestones/${mId}/complete`);
    loadData();
  };

  const analyzeHealth = async () => {
    setLoadingAI(true);
    try {
      await api.post(`/api/tenants/${params.id}/analyze-health`);
      await loadData();
    } catch (err) {
      console.error(err);
    }
    setLoadingAI(false);
  };

  if (!tenant) return <div>Loading...</div>;

  return (
    <div>
      <div className="flex justify-between items-center mb-8">
        <div>
          <h1 className="text-3xl font-bold">{tenant.name}</h1>
          <p className="text-gray-500">Status: {tenant.status}</p>
        </div>
        <button
          onClick={analyzeHealth}
          disabled={loadingAI}
          className="bg-purple-600 text-white px-4 py-2 rounded hover:bg-purple-700 disabled:opacity-50"
        >
          {loadingAI ? 'Analyzing...' : 'AI Health Analysis'}
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-8">
        <div>
          <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100 mb-8">
            <h2 className="text-xl font-semibold mb-4">Health Metrics</h2>
            <div className="space-y-4">
              <div>
                <p className="text-sm text-gray-500">Health Score</p>
                <div className="flex items-center gap-2">
                  <div className="w-full bg-gray-200 rounded-full h-2.5">
                    <div className={`h-2.5 rounded-full ${tenant.healthScore >= 80 ? 'bg-green-500' : tenant.healthScore >= 50 ? 'bg-yellow-500' : 'bg-red-500'}`} style={{ width: `${tenant.healthScore}%` }}></div>
                  </div>
                  <span className="font-bold">{tenant.healthScore}</span>
                </div>
              </div>
              <div>
                <p className="text-sm text-gray-500">Churn Risk</p>
                <p className={`font-bold ${tenant.churnRisk === 'HIGH' ? 'text-red-500' : 'text-green-500'}`}>{tenant.churnRisk}</p>
              </div>
            </div>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100">
            <h2 className="text-xl font-semibold mb-4">Onboarding Milestones</h2>
            <div className="space-y-4">
              {milestones.length === 0 ? <p className="text-gray-500">No milestones yet.</p> : null}
              {milestones.map(m => (
                <div key={m.id} className="border p-4 rounded flex justify-between items-center">
                  <div>
                    <h3 className="font-medium">{m.title}</h3>
                    <p className="text-sm text-gray-500">{m.status}</p>
                  </div>
                  {m.status !== 'COMPLETED' && (
                    <button onClick={() => completeMilestone(m.id)} className="text-sm bg-blue-100 text-blue-700 px-3 py-1 rounded">
                      Complete
                    </button>
                  )}
                </div>
              ))}
            </div>
          </div>
        </div>

        <div>
          <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-100">
            <h2 className="text-xl font-semibold mb-4">Recent Events</h2>
            <div className="space-y-4">
              {events.length === 0 ? <p className="text-gray-500">No events found.</p> : null}
              {events.map(e => (
                <div key={e.id} className="border-l-2 border-blue-500 pl-4 py-2">
                  <p className="text-sm font-bold">{e.eventType}</p>
                  <p className="text-sm text-gray-600">{e.description}</p>
                  <p className="text-xs text-gray-400 mt-1">{new Date(e.occurredAt).toLocaleString()}</p>
                </div>
              ))}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
