'use client';

import { useEffect, useState } from 'react';
import { AgentHealthMetric, getHealthMetrics } from '../lib/api';

export default function DashboardPage() {
  const [metrics, setMetrics] = useState<AgentHealthMetric[]>([]);

  useEffect(() => {
    getHealthMetrics().then(setMetrics).catch(console.error);
  }, []);

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">Dashboard</h1>
      
      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        {metrics.map(metric => (
          <div key={metric.id} className="bg-white p-6 rounded-lg shadow">
            <h3 className="font-semibold text-lg mb-2">{metric.agentId}</h3>
            <div className="space-y-2 text-sm">
              <div className="flex justify-between">
                <span className="text-gray-500">Success Rate</span>
                <span>{metric.successRate}%</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-500">Avg Latency</span>
                <span>{metric.avgLatencyMs}ms</span>
              </div>
              <div className="flex justify-between">
                <span className="text-gray-500">Escalation Rate</span>
                <span>{metric.escalationRate}%</span>
              </div>
            </div>
          </div>
        ))}
        {metrics.length === 0 && (
          <div className="col-span-3 text-center text-gray-500 py-8">
            No health metrics available
          </div>
        )}
      </div>
    </div>
  );
}
