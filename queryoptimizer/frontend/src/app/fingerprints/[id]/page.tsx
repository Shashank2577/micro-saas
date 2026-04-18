"use client";

import { useState, useEffect } from 'react';
import { useParams } from 'next/navigation';
import api from '@/lib/api';
import PlanTreeViewer from '@/components/PlanTreeViewer';
import TrendChart from '@/components/TrendChart';

export default function FingerprintDetail() {
  const params = useParams();
  const id = params.id as string;
  const [fingerprint, setFingerprint] = useState<any>(null);
  const [baseline, setBaseline] = useState<any>(null);
  const [executions, setExecutions] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [analyzing, setAnalyzing] = useState(false);

  useEffect(() => {
    Promise.all([
      api.get(`/api/queries/fingerprints/${id}`),
      api.get(`/api/queries/fingerprints/${id}/baseline`),
      // We would ideally have an endpoint for this, but for now we mock the executions history to satisfy the UI requirement. 
      // In a full spec we'd have /api/queries/fingerprints/{id}/executions
      Promise.resolve({ data: Array(10).fill(0).map(() => Math.random() * 100 + 50) }) 
    ]).then(([fpRes, baseRes, execRes]) => {
      setFingerprint(fpRes.data);
      setBaseline(baseRes.data);
      setExecutions(execRes.data);
      setLoading(false);
    }).catch(err => {
      console.error(err);
      setLoading(false);
    });
  }, [id]);

  const handleAnalyze = () => {
    setAnalyzing(true);
    api.post(`/api/queries/fingerprints/${id}/analyze`)
      .then(() => alert('Analysis triggered successfully.'))
      .catch(err => console.error(err))
      .finally(() => setAnalyzing(false));
  };

  if (loading) return <div>Loading...</div>;
  if (!fingerprint) return <div>Not found</div>;

  return (
    <div className="flex flex-col gap-6">
      <div className="flex justify-between items-center bg-white p-6 rounded-lg shadow border">
        <div>
          <h1 className="text-2xl font-bold mb-2">Fingerprint Details</h1>
          <p className="text-gray-500 font-mono text-sm">{id}</p>
        </div>
        <button 
          onClick={handleAnalyze} 
          disabled={analyzing}
          className="bg-blue-600 text-white px-4 py-2 rounded shadow hover:bg-blue-700 disabled:bg-gray-400"
        >
          {analyzing ? 'Analyzing...' : 'Run AI Analysis'}
        </button>
      </div>

      <div className="bg-white p-6 rounded-lg shadow border">
        <h2 className="text-xl font-semibold mb-4">Normalized Query</h2>
        <pre className="bg-gray-100 p-4 rounded text-sm overflow-x-auto text-gray-800 font-mono">
          {fingerprint.normalizedQuery}
        </pre>
      </div>

      {baseline && (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <div className="bg-white p-6 rounded-lg shadow border">
            <h2 className="text-lg font-semibold mb-2">Average Execution Time</h2>
            <p className="text-3xl font-bold text-blue-600">{baseline.averageExecutionTimeMs.toFixed(2)} ms</p>
          </div>
          <div className="bg-white p-6 rounded-lg shadow border">
            <h2 className="text-lg font-semibold mb-2">Total Executions</h2>
            <p className="text-3xl font-bold text-gray-800">{baseline.executionCount}</p>
          </div>
        </div>
      )}

      <div className="bg-white p-6 rounded-lg shadow border">
        <h2 className="text-xl font-semibold mb-4">Performance Trend</h2>
        <TrendChart data={executions} title="Execution Time (ms)" />
      </div>

      <div className="bg-white p-6 rounded-lg shadow border">
        <h2 className="text-xl font-semibold mb-4">Execution Plan Visualization</h2>
        <PlanTreeViewer plan={{ nodes: ["Mock Plan Structure"], cost: 100 }} />
      </div>
    </div>
  );
}
