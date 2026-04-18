"use client";

import { useState, useEffect } from 'react';
import api from '@/lib/api';

export default function RecommendationsPage() {
  const [recommendations, setRecommendations] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchRecommendations();
  }, []);

  const fetchRecommendations = () => {
    setLoading(true);
    api.get('/api/recommendations')
      .then(res => setRecommendations(res.data))
      .catch(err => console.error(err))
      .finally(() => setLoading(false));
  };

  const updateStatus = (id: string, status: string) => {
    api.put(`/api/recommendations/${id}/status`, { status })
      .then(() => fetchRecommendations())
      .catch(err => console.error(err));
  };

  if (loading) return <div>Loading...</div>;

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">AI Recommendations</h1>
      <div className="grid gap-6 grid-cols-1 xl:grid-cols-2">
        {recommendations.map(rec => (
          <div key={rec.id} className="bg-white p-6 rounded-lg shadow border flex flex-col gap-4">
            <div className="flex justify-between items-start">
              <span className="bg-blue-100 text-blue-800 text-xs font-semibold px-2.5 py-0.5 rounded">
                {rec.recommendationType}
              </span>
              <span className={`text-xs font-bold px-2 py-1 rounded ${
                rec.status === 'APPLIED' ? 'bg-green-100 text-green-800' :
                rec.status === 'REJECTED' ? 'bg-red-100 text-red-800' :
                'bg-yellow-100 text-yellow-800'
              }`}>
                {rec.status}
              </span>
            </div>
            <p className="text-gray-700 text-sm">{rec.description}</p>
            <div className="text-xs text-gray-500">Impact: {rec.impactEstimate}</div>
            <div className="flex justify-between items-center mt-auto pt-4 border-t">
              <span className="text-xs text-gray-500 font-mono">Confidence: {(rec.confidenceScore * 100).toFixed(0)}%</span>
              <div className="flex gap-2">
                <button onClick={() => updateStatus(rec.id, 'APPLIED')} className="text-xs bg-green-50 text-green-600 px-2 py-1 rounded hover:bg-green-100 border border-green-200">Apply</button>
                <button onClick={() => updateStatus(rec.id, 'REJECTED')} className="text-xs bg-red-50 text-red-600 px-2 py-1 rounded hover:bg-red-100 border border-red-200">Reject</button>
              </div>
            </div>
          </div>
        ))}
        {recommendations.length === 0 && <p>No recommendations found.</p>}
      </div>
    </div>
  );
}
