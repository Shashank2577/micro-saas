"use client";

import React, { useState, useEffect } from 'react';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';

export default function RecommendationsPage() {
  const [recommendations, setRecommendations] = useState<any[]>([]);
  const [segments, setSegments] = useState<any[]>([]);
  const [selectedSegment, setSelectedSegment] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetch('/api/pricing/recommendations').then(res => res.json()).then(setRecommendations);
    fetch('/api/pricing/segments').then(res => res.json()).then(setSegments);
  }, []);

  const handleGenerate = async () => {
    if (!selectedSegment) return;
    setLoading(true);
    try {
      const res = await fetch('/api/pricing/recommendations/generate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ segmentId: selectedSegment })
      });
      const data = await res.json();
      setRecommendations(prev => [...prev, data]);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-6">AI Pricing Recommendations</h1>

      <Card className="mb-8">
        <CardHeader>
          <CardTitle>Generate New Recommendation</CardTitle>
        </CardHeader>
        <CardContent className="flex gap-4">
          <select
            className="border p-2 rounded flex-grow"
            value={selectedSegment}
            onChange={(e) => setSelectedSegment(e.target.value)}
          >
            <option value="">Select a segment...</option>
            {segments.map((s: any) => (
              <option key={s.id} value={s.id}>{s.name}</option>
            ))}
          </select>
          <button
            className="px-6 py-2 bg-purple-600 text-white rounded hover:bg-purple-700 disabled:opacity-50"
            onClick={handleGenerate}
            disabled={!selectedSegment || loading}
          >
            {loading ? 'Generating via AI...' : 'Generate Insight'}
          </button>
        </CardContent>
      </Card>

      <div className="space-y-4">
        {recommendations.length === 0 ? (
          <p className="text-gray-500">No recommendations generated yet.</p>
        ) : (
          recommendations.map((rec: any) => (
            <Card key={rec.id} className="border-l-4 border-l-purple-500">
              <CardHeader>
                <CardTitle className="flex justify-between items-center">
                  <span>Recommendation for Segment: {rec.segmentId}</span>
                  <span className="text-sm bg-purple-100 text-purple-800 px-2 py-1 rounded">Confidence: {(rec.confidenceScore * 100).toFixed(0)}%</span>
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="grid grid-cols-3 gap-4 mb-4">
                  <div className="p-3 bg-gray-50 rounded text-center">
                    <p className="text-sm text-gray-500">Current Price</p>
                    <p className="text-xl font-bold">${rec.currentPrice}</p>
                  </div>
                  <div className="p-3 bg-purple-50 rounded text-center">
                    <p className="text-sm text-purple-700">Recommended Price</p>
                    <p className="text-2xl font-bold text-purple-700">${rec.recommendedPrice}</p>
                  </div>
                  <div className="p-3 bg-green-50 rounded text-center">
                    <p className="text-sm text-green-700">Est. Revenue Lift</p>
                    <p className="text-xl font-bold text-green-700">+{rec.estimatedRevenueLift}%</p>
                  </div>
                </div>
                <div>
                  <h4 className="font-semibold mb-2">AI Rationale:</h4>
                  <p className="text-gray-700 italic bg-gray-50 p-4 rounded border-l-4 border-l-gray-300">
                    "{rec.rationale}"
                  </p>
                </div>
              </CardContent>
            </Card>
          ))
        )}
      </div>
    </div>
  );
}
