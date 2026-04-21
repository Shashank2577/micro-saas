"use client";

import React, { useState, useEffect } from 'react';
import dynamic from 'next/dynamic';
import { Card, CardHeader, CardTitle, CardContent } from '@/components/ui/card';

// Dynamically import Plotly to avoid SSR issues
const Plot = dynamic(() => import('react-plotly.js'), { ssr: false });

export default function SegmentsPage() {
  const [segments, setSegments] = useState<any[]>([]);
  const [selectedSegment, setSelectedSegment] = useState<string | null>(null);
  const [elasticity, setElasticity] = useState<any>(null);

  useEffect(() => {
    fetch('/api/pricing/segments')
      .then(res => res.json())
      .then(data => {
          setSegments(data);
          if (data.length > 0) setSelectedSegment(data[0].id);
      })
      .catch(console.error);
  }, []);

  const handleCalculateElasticity = async () => {
    if (!selectedSegment) return;
    try {
      const res = await fetch('/api/pricing/elasticity/calculate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ segmentId: selectedSegment })
      });
      const data = await res.json();
      setElasticity(data);
    } catch (e) {
      console.error(e);
    }
  };

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold mb-6">Customer Segments & Elasticity</h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="col-span-1 space-y-4">
          <Card>
            <CardHeader>
              <CardTitle>Segments</CardTitle>
            </CardHeader>
            <CardContent>
              {segments.length === 0 ? (
                <p>No segments found.</p>
              ) : (
                <ul className="space-y-2">
                  {segments.map((seg: any) => (
                    <li
                      key={seg.id}
                      className={`p-3 border rounded cursor-pointer ${selectedSegment === seg.id ? 'bg-blue-50 border-blue-500' : ''}`}
                      onClick={() => setSelectedSegment(seg.id)}
                    >
                      <p className="font-semibold">{seg.name}</p>
                      <p className="text-sm text-gray-500">Size: {seg.size}</p>
                    </li>
                  ))}
                </ul>
              )}
            </CardContent>
          </Card>

          <button
            className="w-full py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50"
            onClick={handleCalculateElasticity}
            disabled={!selectedSegment}
          >
            Calculate Elasticity Model
          </button>
        </div>

        <div className="col-span-2">
          {elasticity ? (
            <Card>
              <CardHeader>
                <CardTitle>Elasticity Curve</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="h-96 w-full flex items-center justify-center border rounded">
                  <Plot
                    data={[
                      {
                        x: [10, 20, 30, 40, 50, 60, 70, 80, 90, 100],
                        y: [1000, 800, 600, 400, 200, 100, 50, 20, 10, 5],
                        type: 'scatter',
                        mode: 'lines+markers',
                        marker: { color: 'red' },
                        name: 'Demand'
                      }
                    ]}
                    layout={{
                      title: { text: 'Price vs Demand (Simulated)' },
                      xaxis: { title: { text: 'Price ($)' } },
                      yaxis: { title: { text: 'Estimated Conversions' } },
                      autosize: true
                    }}
                    style={{ width: '100%', height: '100%' }}
                  />
                </div>
                <div className="mt-4 grid grid-cols-2 gap-4">
                  <div className="p-4 bg-gray-50 rounded">
                    <p className="text-sm text-gray-500">Coefficient</p>
                    <p className="text-xl font-bold">{elasticity.elasticityCoefficient}</p>
                  </div>
                  <div className="p-4 bg-gray-50 rounded">
                    <p className="text-sm text-gray-500">Model Fit (R²)</p>
                    <p className="text-xl font-bold">{elasticity.rSquared}</p>
                  </div>
                </div>
              </CardContent>
            </Card>
          ) : (
            <div className="flex h-full items-center justify-center border-2 border-dashed rounded-lg text-gray-400 p-12">
              Select a segment and calculate elasticity to view the curve.
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
