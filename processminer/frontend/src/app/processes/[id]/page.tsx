"use client";

import { useState, useEffect } from 'react';
import api from '@/lib/api';
import Link from 'next/link';
import { useParams } from 'next/navigation';

export default function ProcessDetailPage() {
  const params = useParams();
  const id = params.id as string;
  const [model, setModel] = useState<any>(null);
  const [analysis, setAnalysis] = useState<any[]>([]);

  const fetchData = async () => {
    try {
      const [modelRes, analysisRes] = await Promise.all([
        api.get(`/api/process-models/${id}`),
        api.get(`/api/process-models/${id}/analysis`)
      ]);
      setModel(modelRes.data);
      setAnalysis(analysisRes.data);
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    if (id) fetchData();
  }, [id]);

  const handleRunAnalysis = async () => {
    try {
      await api.post(`/api/process-models/${id}/analyze`);
      alert('Analysis completed');
      fetchData();
    } catch (error) {
      alert('Error running analysis');
    }
  };

  if (!model) return <div className="p-4">Loading...</div>;

  return (
    <div className="container mx-auto p-4">
      <Link href="/processes" className="text-blue-500 mb-4 inline-block">&larr; Back to Processes</Link>
      
      <div className="flex justify-between items-center mb-6">
        <div>
          <h1 className="text-3xl font-bold">{model.name}</h1>
          <p className="text-gray-600">{model.description}</p>
        </div>
        <button onClick={handleRunAnalysis} className="bg-purple-600 hover:bg-purple-700 text-white font-bold py-2 px-4 rounded shadow">
          Run Analysis
        </button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        <div className="bg-white p-4 shadow rounded">
          <h2 className="text-xl font-bold mb-4">Process Map (BPMN)</h2>
          <div className="bg-gray-100 p-4 font-mono text-xs overflow-auto h-64 border rounded">
            {model.bpmnXml}
          </div>
        </div>
        
        <div className="bg-white p-4 shadow rounded">
          <h2 className="text-xl font-bold mb-4">Analysis Results</h2>
          {analysis.length === 0 ? (
            <p className="text-gray-500">No analysis results yet. Click "Run Analysis" above.</p>
          ) : (
            <div className="space-y-4">
              {analysis.map((res, i) => (
                <div key={i} className={`p-3 border-l-4 rounded bg-gray-50 ${res.type === 'BOTTLENECK' ? 'border-red-500' : 'border-yellow-500'}`}>
                  <div className="flex justify-between">
                    <span className="font-bold text-sm text-gray-700">{res.type}</span>
                    <span className={`text-xs px-2 py-1 rounded-full font-semibold ${res.severity === 'HIGH' ? 'bg-red-100 text-red-800' : 'bg-yellow-100 text-yellow-800'}`}>{res.severity}</span>
                  </div>
                  <p className="mt-1 text-gray-800">{res.description}</p>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
