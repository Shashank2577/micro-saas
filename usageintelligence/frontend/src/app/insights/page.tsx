"use client";

import { useEffect, useState } from 'react';
import api from '../../lib/api';
import { AiInsight } from '../../types';
import Link from 'next/link';

export default function InsightsPage() {
  const [insights, setInsights] = useState<AiInsight[]>([]);
  const [loading, setLoading] = useState(true);
  const [generating, setGenerating] = useState(false);

  useEffect(() => {
    fetchInsights();
  }, []);

  async function fetchInsights() {
    try {
      const res = await api.get<AiInsight[]>('/insights');
      setInsights(res.data);
    } catch (error) {
      console.error('Error fetching insights:', error);
    } finally {
      setLoading(false);
    }
  }

  async function handleGenerate() {
    setGenerating(true);
    try {
      await api.post('/insights/generate');
      fetchInsights();
    } catch (error) {
      console.error('Error generating insight:', error);
      alert('Failed to generate insight.');
    } finally {
      setGenerating(false);
    }
  }

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold">AI Insights</h1>
        <div className="space-x-4">
          <button 
            onClick={handleGenerate} 
            disabled={generating}
            className="bg-purple-600 text-white px-4 py-2 rounded disabled:opacity-50"
          >
            {generating ? 'Generating...' : 'Generate New Insight'}
          </button>
          <Link href="/" className="text-blue-600 hover:underline">Dashboard</Link>
        </div>
      </div>

      {loading ? (
        <p>Loading insights...</p>
      ) : (
        <div className="space-y-6">
          {insights.map((insight) => (
            <div key={insight.id} className="bg-white p-6 shadow rounded-lg border-l-4 border-purple-500">
              <div className="flex justify-between">
                <h3 className="text-xl font-bold text-gray-900">{insight.title}</h3>
                <span className="text-sm text-gray-500">{new Date(insight.createdAt).toLocaleDateString()}</span>
              </div>
              <p className="mt-4 text-gray-700">{insight.description}</p>
              
              <div className="mt-4 bg-purple-50 p-4 rounded">
                <h4 className="font-semibold text-purple-900">Recommendation</h4>
                <p className="mt-1 text-purple-800">{insight.recommendation}</p>
              </div>

              <div className="mt-4">
                <h4 className="text-xs font-semibold text-gray-500 uppercase">Data References:</h4>
                <pre className="text-xs text-gray-600 mt-1">{JSON.stringify(insight.dataReferences)}</pre>
              </div>
            </div>
          ))}
          {insights.length === 0 && <p className="text-gray-500">No AI insights generated yet. Click generate to analyze recent data.</p>}
        </div>
      )}
    </div>
  );
}
