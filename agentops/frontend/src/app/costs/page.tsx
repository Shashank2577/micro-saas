'use client';

import { useEffect, useState } from 'react';
import { CostAllocation, getCostAllocations } from '../../lib/api';

export default function CostsPage() {
  const [costs, setCosts] = useState<CostAllocation[]>([]);

  useEffect(() => {
    getCostAllocations().then(setCosts).catch(console.error);
  }, []);

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">Cost Allocation</h1>
      
      <div className="bg-white shadow rounded-lg overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Period</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Agent ID</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Team</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Feature</th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Total Cost</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {costs.map((cost) => (
              <tr key={cost.id}>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{cost.period}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{cost.agentId}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{cost.teamId || '-'}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{cost.productFeature || '-'}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900 text-right font-medium">
                  ${cost.totalCost}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        {costs.length === 0 && (
          <div className="text-center py-8 text-gray-500">No cost data available</div>
        )}
      </div>
    </div>
  );
}
