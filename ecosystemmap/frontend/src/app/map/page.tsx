"use client";
import { useEffect, useState } from 'react';
import { api } from '@/lib/api';

export default function MapPage() {
  const [data, setData] = useState({ apps: [], flows: [] });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/ecosystem/map')
      .then(res => setData(res.data))
      .catch(err => console.error(err))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="p-8">Loading map data...</div>;

  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">Ecosystem View</h1>
      <div className="mb-8">
        <h2 className="text-xl font-semibold mb-2">Deployed Apps ({data.apps.length})</h2>
        <ul className="list-disc pl-5">
          {data.apps.map((app: any) => (
            <li key={app.id}>{app.name} - Status: {app.status}</li>
          ))}
        </ul>
      </div>
      <div>
        <h2 className="text-xl font-semibold mb-2">Data Flows ({data.flows.length})</h2>
        <ul className="list-disc pl-5">
          {data.flows.map((flow: any) => (
            <li key={flow.id}>{flow.sourceAppId} &rarr; {flow.targetAppId} ({flow.eventType})</li>
          ))}
        </ul>
      </div>
    </div>
  );
}
