"use client";

import { useEffect, useState } from 'react';
import { api } from '@/lib/api';
import { InsightCard } from '@/components/InsightCard';
import Link from 'next/link';

export default function Dashboard() {
  const [insights, setInsights] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadInsights() {
      try {
        const data = await api.getInsights();
        setInsights(data);
      } catch (error) {
        console.error("Failed to load insights", error);
      } finally {
        setLoading(false);
      }
    }
    loadInsights();
  }, []);

  return (
    <div className="container mx-auto px-4 py-8">
      <header className="flex justify-between items-center mb-8">
        <div>
          <h1 className="text-3xl font-bold text-gray-900">InsightEngine Dashboard</h1>
          <p className="text-gray-600 mt-1">Discover what matters in your data.</p>
        </div>
        <div className="space-x-4">
          <Link href="/alerts" className="text-blue-600 hover:text-blue-800">Alert Rules</Link>
          <Link href="/settings" className="text-blue-600 hover:text-blue-800">Settings</Link>
        </div>
      </header>

      {loading ? (
        <div className="text-center py-10">Loading insights...</div>
      ) : insights.length === 0 ? (
        <div className="text-center py-10 text-gray-500">No insights discovered yet.</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {insights.map(insight => (
            <InsightCard key={insight.id} insight={insight} />
          ))}
        </div>
      )}
    </div>
  );
}
