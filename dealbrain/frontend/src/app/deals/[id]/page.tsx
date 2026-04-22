"use client";

import React from 'react';

export default function DealPage({ params }: { params: { id: string } }) {
  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-4">Deal Details</h1>
      <p className="text-gray-600 mb-8">Deal ID: {params.id}</p>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-xl font-semibold mb-4">Health & AI Insights</h2>
          <p>Health Score: <span className="font-bold text-green-600">85</span></p>
          <div className="mt-4">
            <h3 className="font-semibold text-lg">Next Action Recommendation</h3>
            <div className="bg-blue-50 p-4 rounded mt-2 border border-blue-100">
              <p className="font-bold text-blue-800">Schedule Demo</p>
              <p className="text-sm text-blue-600 mt-1">Decision maker has engaged, time to show value.</p>
            </div>
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-xl font-semibold mb-4">Risk Signals</h2>
          <ul className="space-y-2">
            <li className="text-red-600">⚠ Stale activity (Last action 14 days ago)</li>
            <li className="text-yellow-600">⚠ Competitor X engaged</li>
          </ul>
        </div>
      </div>
    </div>
  );
}
