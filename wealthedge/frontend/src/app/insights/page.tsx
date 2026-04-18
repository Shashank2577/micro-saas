"use client";

import React, { useEffect, useState } from 'react';
import api from '../../lib/api';

interface Portfolio {
  id: string;
  name: string;
}

export default function InsightsPage() {
  const [portfolios, setPortfolios] = useState<Portfolio[]>([]);
  const [selectedId, setSelectedId] = useState('');
  const [insight, setInsight] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    api.get('/api/portfolios').then(res => {
      setPortfolios(res.data);
      if (res.data.length > 0) setSelectedId(res.data[0].id);
    });
  }, []);

  const getInsights = async () => {
    if (!selectedId) return;
    setLoading(true);
    try {
      const res = await api.get(`/api/portfolios/${selectedId}/insights`);
      setInsight(res.data.insights);
    } catch (e) {
      console.error(e);
      setInsight('Failed to fetch insights.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">AI Wealth Insights</h1>
      
      <div className="mb-4">
        <select className="border p-2 mr-4" value={selectedId} onChange={e => setSelectedId(e.target.value)}>
          {portfolios.map(p => (
            <option key={p.id} value={p.id}>{p.name}</option>
          ))}
        </select>
        <button onClick={getInsights} disabled={loading} className="bg-indigo-500 text-white px-4 py-2 rounded">
          {loading ? 'Analyzing...' : 'Generate Analysis'}
        </button>
      </div>

      {insight && (
        <div className="border p-6 rounded bg-gray-50 shadow">
          <h2 className="font-semibold text-lg mb-2">Analysis Results</h2>
          <p className="whitespace-pre-wrap">{insight}</p>
        </div>
      )}
    </div>
  );
}
