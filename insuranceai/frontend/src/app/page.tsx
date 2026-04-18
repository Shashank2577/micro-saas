"use client";

import { useState, useEffect } from 'react';
import { api } from '../lib/api';

export default function DashboardPage() {
  const [summary, setSummary] = useState<any>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchSummary = async () => {
      try {
        const data = await api.get('/analytics/summary', '00000000-0000-0000-0000-000000000000');
        setSummary(data);
      } catch (error) {
        console.error("Failed to fetch summary", error);
      } finally {
        setLoading(false);
      }
    };
    fetchSummary();
  }, []);

  if (loading) return <div className="text-center p-8">Loading dashboard...</div>;

  return (
    <div>
      <h1 className="text-2xl font-bold text-gray-900 mb-6">Dashboard</h1>
      
      {summary ? (
        <div className="grid grid-cols-1 gap-5 sm:grid-cols-2 lg:grid-cols-4">
          <div className="bg-white overflow-hidden shadow rounded-lg">
            <div className="px-4 py-5 sm:p-6">
              <dt className="text-sm font-medium text-gray-500 truncate">Total Claims</dt>
              <dd className="mt-1 text-3xl font-semibold text-gray-900">{summary.totalClaims}</dd>
            </div>
          </div>
          
          <div className="bg-white overflow-hidden shadow rounded-lg">
            <div className="px-4 py-5 sm:p-6">
              <dt className="text-sm font-medium text-gray-500 truncate">Total Policies</dt>
              <dd className="mt-1 text-3xl font-semibold text-gray-900">{summary.totalPolicies}</dd>
            </div>
          </div>
          
          <div className="bg-white overflow-hidden shadow rounded-lg">
            <div className="px-4 py-5 sm:p-6">
              <dt className="text-sm font-medium text-gray-500 truncate">Avg Fraud Score</dt>
              <dd className="mt-1 text-3xl font-semibold text-gray-900">{summary.averageFraudScore}</dd>
            </div>
          </div>
          
          <div className="bg-white overflow-hidden shadow rounded-lg">
            <div className="px-4 py-5 sm:p-6">
              <dt className="text-sm font-medium text-gray-500 truncate">Avg Risk Score</dt>
              <dd className="mt-1 text-3xl font-semibold text-gray-900">{summary.averageRiskScore}</dd>
            </div>
          </div>
        </div>
      ) : (
        <div className="bg-white p-6 rounded shadow text-center text-gray-500">
          Analytics data unavailable.
        </div>
      )}
    </div>
  );
}
