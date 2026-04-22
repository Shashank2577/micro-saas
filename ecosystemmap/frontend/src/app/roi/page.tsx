"use client";
import React, { useEffect, useState } from 'react';
import Link from 'next/link';
import { api } from '@/lib/api';

export default function RoiPage() {
  const [metrics, setMetrics] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/api/ecosystems/default/roi')
      .then(res => setMetrics(res.data))
      .catch(err => console.error(err))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="p-8">Loading ROI metrics...</div>;

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">ROI Metrics</h1>
        <Link href="/" className="text-blue-600 hover:underline">&larr; Back to Dashboard</Link>
      </div>
      <div className="grid gap-4">
        {metrics.map((metric: any) => (
          <div key={metric.id} className="p-4 border rounded shadow-sm">
            <h3 className="font-semibold text-lg">{metric.metricName}</h3>
            <p className="mt-2 text-green-600 font-bold">{metric.metricValue} {metric.currencyCode}</p>
            <p className="mt-1 text-sm text-gray-500">Period: {metric.period}</p>
          </div>
        ))}
        {metrics.length === 0 && <p>No ROI metrics found.</p>}
      </div>
    </div>
  );
}
