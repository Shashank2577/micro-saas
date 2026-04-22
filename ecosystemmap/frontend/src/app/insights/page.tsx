"use client";
import React, { useEffect, useState } from 'react';
import Link from 'next/link';
import { api } from '@/lib/api';

export default function InsightsPage() {
  const [insights, setInsights] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/api/ecosystems/default/insights')
      .then(res => setInsights(res.data))
      .catch(err => console.error(err))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="p-8">Loading insights...</div>;

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">AI Insights</h1>
        <Link href="/" className="text-blue-600 hover:underline">&larr; Back to Dashboard</Link>
      </div>
      <div className="grid gap-4">
        {insights.map((insight: any) => (
          <div key={insight.id} className="p-4 border rounded shadow-sm">
            <h3 className="font-semibold text-lg">{insight.insightType}</h3>
            <p className="mt-2 text-gray-700">{insight.content}</p>
            <p className="mt-2 text-sm text-gray-500">Status: {insight.status}</p>
          </div>
        ))}
        {insights.length === 0 && <p>No insights found.</p>}
      </div>
    </div>
  );
}
