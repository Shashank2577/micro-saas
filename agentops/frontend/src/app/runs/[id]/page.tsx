'use client';

import { useEffect, useState } from 'react';
import { AgentRun, AgentStep, getRun, getRunSteps } from '../../../lib/api';

export default function RunDetailsPage({ params }: { params: { id: string } }) {
  const [run, setRun] = useState<AgentRun | null>(null);
  const [steps, setSteps] = useState<AgentStep[]>([]);

  useEffect(() => {
    getRun(params.id).then(setRun).catch(console.error);
    getRunSteps(params.id).then(setSteps).catch(console.error);
  }, [params.id]);

  if (!run) return <div>Loading...</div>;

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold">Run Details</h1>
        <span className="px-3 py-1 rounded-full text-sm font-medium bg-gray-100">
          {run.status}
        </span>
      </div>

      <div className="bg-white p-6 rounded-lg shadow grid grid-cols-2 gap-4">
        <div>
          <p className="text-sm text-gray-500">Agent ID</p>
          <p className="font-medium">{run.agentId}</p>
        </div>
        <div>
          <p className="text-sm text-gray-500">Workflow ID</p>
          <p className="font-medium">{run.workflowId || 'N/A'}</p>
        </div>
        <div>
          <p className="text-sm text-gray-500">Started At</p>
          <p className="font-medium">{new Date(run.startedAt).toLocaleString()}</p>
        </div>
        <div>
          <p className="text-sm text-gray-500">Token Cost</p>
          <p className="font-medium">${run.tokenCost}</p>
        </div>
      </div>

      <h2 className="text-xl font-semibold mt-8">Execution Trace</h2>
      <div className="space-y-4">
        {steps.map((step, index) => (
          <div key={step.id} className="bg-white p-4 rounded-lg shadow border-l-4 border-indigo-500">
            <div className="flex justify-between mb-2">
              <span className="font-semibold">Step {index + 1}: {step.stepType}</span>
              <span className="text-sm text-gray-500">{step.durationMs}ms</span>
            </div>
            <div className="grid grid-cols-2 gap-4 mt-4">
              <div>
                <p className="text-xs text-gray-500 mb-1 uppercase">Input</p>
                <pre className="bg-gray-50 p-2 rounded text-sm overflow-x-auto">
                  {step.input}
                </pre>
              </div>
              <div>
                <p className="text-xs text-gray-500 mb-1 uppercase">Output</p>
                <pre className="bg-gray-50 p-2 rounded text-sm overflow-x-auto">
                  {step.output}
                </pre>
              </div>
            </div>
          </div>
        ))}
        {steps.length === 0 && (
          <p className="text-gray-500">No steps recorded for this run.</p>
        )}
      </div>
    </div>
  );
}
