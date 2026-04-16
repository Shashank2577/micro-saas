'use client';

import React, { useEffect, useState } from 'react';

export default function Dashboard() {
  const [metrics, setMetrics] = useState({
    deploymentFrequency: 'Loading...',
    leadTimeForChanges: 'Loading...',
    timeToRestoreService: 'Loading...',
    changeFailureRate: 'Loading...',
  });

  useEffect(() => {
    fetch('http://localhost:8093/api/metrics/dora')
      .then((res) => res.json())
      .then((data) => setMetrics(data))
      .catch((err) => console.error('Error fetching metrics', err));
  }, []);

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <h1 className="text-3xl font-bold text-gray-900 mb-8">DeploySignal DORA Metrics Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
          <h2 className="text-sm font-medium text-gray-500 uppercase tracking-wider mb-2">Deployment Frequency</h2>
          <p className="text-3xl font-bold text-gray-900">{metrics.deploymentFrequency}</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
          <h2 className="text-sm font-medium text-gray-500 uppercase tracking-wider mb-2">Lead Time for Changes</h2>
          <p className="text-3xl font-bold text-gray-900">{metrics.leadTimeForChanges}</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
          <h2 className="text-sm font-medium text-gray-500 uppercase tracking-wider mb-2">Time to Restore Service</h2>
          <p className="text-3xl font-bold text-gray-900">{metrics.timeToRestoreService}</p>
        </div>
        <div className="bg-white p-6 rounded-lg shadow-sm border border-gray-200">
          <h2 className="text-sm font-medium text-gray-500 uppercase tracking-wider mb-2">Change Failure Rate</h2>
          <p className="text-3xl font-bold text-gray-900">{metrics.changeFailureRate}</p>
        </div>
      </div>
    </div>
  );
}
