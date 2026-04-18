"use client";

import { useEffect, useState } from 'react';
import { useParams } from 'next/navigation';
import { api } from '@/lib/api';
import Link from 'next/link';

export default function InsightDetail() {
  const { id } = useParams();
  const [insight, setInsight] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadInsight() {
      try {
        const data = await api.getInsight(id as string);
        setInsight(data);
      } catch (error) {
        console.error("Failed to load insight", error);
      } finally {
        setLoading(false);
      }
    }
    if (id) loadInsight();
  }, [id]);

  if (loading) return <div className="p-8">Loading...</div>;
  if (!insight) return <div className="p-8">Insight not found.</div>;

  return (
    <div className="container mx-auto px-4 py-8 max-w-4xl">
      <Link href="/" className="text-blue-600 hover:underline mb-4 inline-block">&larr; Back to Dashboard</Link>
      
      <div className="bg-white border rounded-lg shadow-sm p-6 mb-6">
        <div className="flex justify-between items-start mb-4">
          <h1 className="text-2xl font-bold text-gray-900">{insight.title}</h1>
          <span className="bg-blue-100 text-blue-800 text-xs px-3 py-1 rounded-full font-semibold">
            {insight.status}
          </span>
        </div>
        
        <p className="text-gray-700 text-lg mb-6">{insight.description}</p>
        
        <div className="grid grid-cols-2 gap-4 mb-8">
          <div className="bg-gray-50 p-4 rounded-md">
            <h3 className="text-sm font-semibold text-gray-500 mb-1">Type</h3>
            <p className="font-medium">{insight.type}</p>
          </div>
          <div className="bg-gray-50 p-4 rounded-md">
            <h3 className="text-sm font-semibold text-gray-500 mb-1">Impact Score</h3>
            <p className="font-medium">{(insight.impactScore * 10).toFixed(1)} / 10</p>
          </div>
        </div>

        <div className="mb-6">
          <h2 className="text-xl font-bold text-gray-900 mb-2">AI Explanation</h2>
          <div className="bg-blue-50 border border-blue-100 p-4 rounded-md text-gray-800">
            {insight.explanation || 'No explanation available.'}
          </div>
        </div>

        <div>
          <h2 className="text-xl font-bold text-gray-900 mb-2">Recommended Action</h2>
          <div className="bg-green-50 border border-green-100 p-4 rounded-md text-gray-800">
            {insight.recommendedAction || 'No recommendations available.'}
          </div>
        </div>
      </div>
      
      {/* Placeholder for comments section */}
      <div className="bg-white border rounded-lg shadow-sm p-6">
        <h2 className="text-xl font-bold text-gray-900 mb-4">Discussion</h2>
        <p className="text-gray-500 italic">Comments feature coming soon...</p>
      </div>
    </div>
  );
}
