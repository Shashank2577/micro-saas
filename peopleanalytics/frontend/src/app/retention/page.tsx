"use client";

import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { api } from '@/lib/api';

export default function RetentionPage() {
  const { data: risks, isLoading } = useQuery({
    queryKey: ['retention-risk'],
    queryFn: async () => {
      const res = await api.get('/retention/risk');
      return res.data;
    },
  });

  if (isLoading) return <div>Analyzing flight risk...</div>;

  return (
    <div>
      <h1 className="text-3xl font-bold mb-8">Retention Risk Analysis</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {risks?.length === 0 && (
          <div className="bg-white p-8 rounded-xl shadow-sm border border-gray-100 col-span-2 text-center text-gray-500">
            No high-risk employees detected at this time.
          </div>
        )}
        {risks?.map((risk: any) => (
          <div key={risk.id} className="bg-white p-6 rounded-xl shadow-sm border border-l-4 border-l-red-500 border-gray-100">
            <h3 className="font-bold text-lg">Employee: {risk.employee.id}</h3>
            <p className="text-gray-600 mb-4">Risk Score: {(risk.riskScore * 100).toFixed(1)}%</p>
            <div className="bg-red-50 p-4 rounded-lg">
              <h4 className="font-semibold text-red-800 text-sm mb-2">AI-Detected Factors:</h4>
              <ul className="list-disc list-inside text-sm text-red-700">
                <li>Recent drop in engagement scores</li>
                <li>Compensation below industry benchmark</li>
              </ul>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
