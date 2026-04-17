'use client';

import { useEffect, useState } from 'react';
import Link from 'next/link';
import { AgentRun, getRuns } from '../../lib/api';

export default function RunsPage() {
  const [runs, setRuns] = useState<AgentRun[]>([]);

  useEffect(() => {
    getRuns().then(setRuns).catch(console.error);
  }, []);

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">Agent Runs</h1>
      
      <div className="bg-white shadow rounded-lg overflow-hidden">
        <table className="min-w-full divide-y divide-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Agent</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Status</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Started At</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Cost</th>
              <th className="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider">Action</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {runs.map((run) => (
              <tr key={run.id}>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{run.agentId}</td>
                <td className="px-6 py-4 whitespace-nowrap text-sm">
                  <span className={`px-2 inline-flex text-xs leading-5 font-semibold rounded-full 
                    ${run.status === 'COMPLETED' ? 'bg-green-100 text-green-800' : 
                      run.status === 'FAILED' ? 'bg-red-100 text-red-800' : 
                      run.status === 'ESCALATED' ? 'bg-yellow-100 text-yellow-800' : 
                      'bg-blue-100 text-blue-800'}`}>
                    {run.status}
                  </span>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {new Date(run.startedAt).toLocaleString()}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${run.tokenCost}</td>
                <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                  <Link href={`/runs/${run.id}`} className="text-indigo-600 hover:text-indigo-900">
                    View Details
                  </Link>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        {runs.length === 0 && (
          <div className="text-center py-8 text-gray-500">No runs found</div>
        )}
      </div>
    </div>
  );
}
