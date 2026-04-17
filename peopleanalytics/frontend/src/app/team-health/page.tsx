"use client";
import React, { useEffect, useState } from 'react';

export default function TeamHealthPage() {
  const [metrics, setMetrics] = useState([]);

  useEffect(() => {
    fetch('/api/team-health', { headers: { 'X-Tenant-ID': '00000000-0000-0000-0000-000000000000' }})
      .then(res => res.json())
      .then(data => setMetrics(data))
      .catch(err => console.error(err));
  }, []);

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Team Health</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {metrics.map((m: any) => (
          <div key={m.id} className="p-6 bg-white shadow rounded">
            <h3 className="font-bold text-lg">{m.department}</h3>
            <p>Health Score: {m.healthScore}</p>
            <p>Collaboration: {m.collaborationScore}</p>
            <p>Productivity: {m.productivityScore}</p>
          </div>
        ))}
        {metrics.length === 0 && <p>No metrics available.</p>}
      </div>
    </div>
  );
}
