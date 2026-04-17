'use client';
import { useEffect, useState } from 'react';

export default function Growth() {
  const [recommendations, setRecommendations] = useState<any[]>([]);
  const [projection, setProjection] = useState('');

  useEffect(() => {
    fetch('/api/growth/recommendations', { headers: { 'X-Tenant-ID': 'test-tenant' } })
      .then(res => res.json())
      .then(data => setRecommendations(data))
      .catch(console.error);

    fetch('/api/growth/projections', { headers: { 'X-Tenant-ID': 'test-tenant' } })
      .then(res => res.json())
      .then(data => setProjection(data.projection))
      .catch(console.error);
  }, []);

  const generateNew = () => {
    fetch('/api/growth/recommendations/generate', { method: 'POST', headers: { 'X-Tenant-ID': 'test-tenant' } })
      .then(res => res.json())
      .then(data => setRecommendations(data))
      .catch(console.error);
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold">AI Growth Intelligence</h1>
        <button onClick={generateNew} className="bg-purple-600 text-white px-4 py-2 rounded hover:bg-purple-700">
          Refresh AI Insights
        </button>
      </div>

      <div className="bg-blue-50 p-6 rounded shadow mb-8 border border-blue-200">
        <h2 className="text-lg font-bold text-blue-800 mb-2">30-Day Growth Projection</h2>
        <p className="text-gray-700">{projection || 'Loading projections...'}</p>
      </div>

      <h2 className="text-xl font-semibold mb-4">Actionable Recommendations</h2>
      <div className="space-y-4">
        {recommendations.map(r => (
          <div key={r.id} className="bg-white p-4 rounded shadow border-l-4 border-purple-500">
            <div className="flex justify-between">
              <span className="text-xs font-bold bg-gray-100 px-2 py-1 rounded text-gray-600">{r.platformName}</span>
              <span className={`text-xs px-2 py-1 rounded ${r.status === 'NEW' ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'}`}>
                {r.status}
              </span>
            </div>
            <p className="mt-2 text-gray-800">{r.description}</p>
          </div>
        ))}
      </div>
    </div>
  );
}
