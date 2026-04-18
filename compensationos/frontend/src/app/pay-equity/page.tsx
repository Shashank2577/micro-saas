"use client";

import { useQuery } from '@tanstack/react-query';
import api from '@/lib/api';

export default function PayEquityPage() {
  const { data: analysis, isLoading } = useQuery({
    queryKey: ['payEquity'],
    queryFn: () => api.get('/api/analysis/pay-equity').then(res => res.data)
  });

  return (
    <div className="space-y-6">
      <h1 className="text-3xl font-bold text-gray-900">Pay Equity Analysis</h1>

      {isLoading ? (
        <p>Loading analysis...</p>
      ) : analysis ? (
        <div className="space-y-6">
          <div className="bg-white shadow overflow-hidden sm:rounded-lg p-6">
            <h2 className="text-xl font-medium text-gray-900 mb-4">AI Insight</h2>
            <p className="text-gray-700 whitespace-pre-wrap">{analysis.aiInsight}</p>
          </div>

          <div className="grid grid-cols-1 gap-6 sm:grid-cols-2">
            <div className="bg-white shadow overflow-hidden sm:rounded-lg p-6">
              <h3 className="text-lg font-medium text-gray-900 mb-4">Average Salary by Gender</h3>
              <ul className="divide-y divide-gray-200">
                {Object.entries(analysis.avgSalaryByGender || {}).map(([key, value]) => (
                  <li key={key} className="py-2 flex justify-between">
                    <span className="text-sm text-gray-600">{key}</span>
                    <span className="text-sm font-medium text-gray-900">${Number(value).toLocaleString()}</span>
                  </li>
                ))}
              </ul>
            </div>

            <div className="bg-white shadow overflow-hidden sm:rounded-lg p-6">
              <h3 className="text-lg font-medium text-gray-900 mb-4">Average Salary by Ethnicity</h3>
              <ul className="divide-y divide-gray-200">
                {Object.entries(analysis.avgSalaryByEthnicity || {}).map(([key, value]) => (
                  <li key={key} className="py-2 flex justify-between">
                    <span className="text-sm text-gray-600">{key}</span>
                    <span className="text-sm font-medium text-gray-900">${Number(value).toLocaleString()}</span>
                  </li>
                ))}
              </ul>
            </div>
          </div>
        </div>
      ) : (
        <p>No data available.</p>
      )}
    </div>
  );
}
