"use client";
import { useEffect, useState } from 'react';
import { api } from '@/lib/api';

export default function OpportunitiesPage() {
  const [opps, setOpps] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/ecosystem/opportunities')
      .then(res => setOpps(res.data))
      .catch(err => console.error(err))
      .finally(() => setLoading(false));
  }, []);

  const analyze = () => {
    setLoading(true);
    api.post('/ecosystem/analyze', {})
      .then(res => setOpps(res.data))
      .catch(err => console.error(err))
      .finally(() => setLoading(false));
  };

  if (loading) return <div className="p-8">Loading opportunities...</div>;

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Integration Opportunities</h1>
        <button onClick={analyze} className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">
          Run AI Analysis
        </button>
      </div>
      {opps.length === 0 ? (
        <p>No opportunities found. Run analysis to discover new integrations.</p>
      ) : (
        <div className="grid gap-4">
          {opps.map((opp: any) => (
            <div key={opp.id} className="p-4 border rounded shadow-sm">
              <h3 className="font-semibold text-lg">{opp.sourceApp} &rarr; {opp.targetApp}</h3>
              <p className="mt-2 text-gray-700">{opp.description}</p>
              <p className="mt-2 text-sm text-green-600 font-medium">Value: {opp.potentialValue}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
