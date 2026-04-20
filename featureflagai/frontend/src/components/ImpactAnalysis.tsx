"use client";

import React, { useState } from 'react';

export default function ImpactAnalysis() {
  const [result, setResult] = useState<string>('');
  const [flagId, setFlagId] = useState<string>('');
  const [metricsData, setMetricsData] = useState<string>('clicks=1000, conversions=50');

  const analyze = async () => {
    if (!flagId) {
      setResult("Please provide a flag ID.");
      return;
    }
    try {
      const res = await fetch(`/api/flags/${flagId}/impact?metricsData=${encodeURIComponent(metricsData)}`);
      const data = await res.text();
      setResult(data);
    } catch (err) {
      console.error(err);
      setResult("Error analyzing impact.");
    }
  };

  return (
    <div className="bg-white p-4 shadow rounded" data-testid="impact-analysis">
      <div className="mb-4">
        <label className="block text-sm font-medium mb-1">Flag ID</label>
        <input
          type="text"
          value={flagId}
          onChange={e => setFlagId(e.target.value)}
          className="border rounded w-full p-2"
        />
      </div>
      <div className="mb-4">
        <label className="block text-sm font-medium mb-1">Metrics Data</label>
        <input
          type="text"
          value={metricsData}
          onChange={e => setMetricsData(e.target.value)}
          className="border rounded w-full p-2"
        />
      </div>
      <button
        onClick={analyze}
        className="bg-blue-600 text-white px-4 py-2 rounded hover:bg-blue-700 transition"
      >
        Run Analysis
      </button>
      {result && (
        <div className="mt-4 p-3 bg-blue-50 text-blue-800 rounded">
          {result}
        </div>
      )}
    </div>
  );
}
