"use client";

import { useState } from 'react';
import api from '../../lib/api';

export default function ImpactAnalysis() {
  const [flagId, setFlagId] = useState('');
  const [metrics, setMetrics] = useState('');
  const [analysis, setAnalysis] = useState('');

  const analyze = async () => {
    try {
      const res = await api.get(`/flags/${flagId}/impact?metricsData=${encodeURIComponent(metrics)}`);
      setAnalysis(res.data.analysis);
    } catch (e) {
      setAnalysis("Error analyzing impact");
    }
  };

  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">Impact Analysis</h1>

      <div className="mb-4">
        <label className="block mb-2">Flag ID</label>
        <input type="text" value={flagId} onChange={e => setFlagId(e.target.value)} className="border p-2 w-full max-w-md"/>
      </div>

      <div className="mb-4">
        <label className="block mb-2">Metrics Data (JSON/CSV/Text)</label>
        <textarea value={metrics} onChange={e => setMetrics(e.target.value)} className="border p-2 w-full max-w-md h-32"/>
      </div>

      <button onClick={analyze} className="bg-green-500 text-white px-4 py-2 rounded mb-6">Analyze</button>

      {analysis && (
        <div className="border p-4 bg-gray-50">
          <h2 className="text-xl font-bold mb-2">AI Analysis</h2>
          <p className="whitespace-pre-wrap">{analysis}</p>
        </div>
      )}
    </div>
  );
}
