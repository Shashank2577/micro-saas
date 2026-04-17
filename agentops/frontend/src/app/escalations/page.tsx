'use client';

import { useEffect, useState } from 'react';
import { HumanEscalation, getPendingEscalations, resolveEscalation } from '../../lib/api';

export default function EscalationsPage() {
  const [escalations, setEscalations] = useState<HumanEscalation[]>([]);
  const [resolvingId, setResolvingId] = useState<string | null>(null);
  const [resolutionText, setResolutionText] = useState('');

  useEffect(() => {
    loadEscalations();
  }, []);

  const loadEscalations = () => {
    getPendingEscalations().then(setEscalations).catch(console.error);
  };

  const handleResolve = async (id: string) => {
    if (!resolutionText) return;
    try {
      await resolveEscalation(id, resolutionText);
      setResolutionText('');
      setResolvingId(null);
      loadEscalations();
    } catch (error) {
      console.error('Failed to resolve:', error);
    }
  };

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">Human Escalations Queue</h1>
      
      <div className="space-y-4">
        {escalations.map((escalation) => (
          <div key={escalation.id} className="bg-white p-6 rounded-lg shadow border border-red-100">
            <div className="flex justify-between items-start mb-4">
              <div>
                <span className="px-2 py-1 text-xs font-semibold bg-red-100 text-red-800 rounded">
                  Pending Review
                </span>
                <p className="mt-2 text-sm text-gray-500">Run ID: {escalation.run?.id}</p>
              </div>
              <span className="text-sm text-gray-500">
                {new Date(escalation.createdAt).toLocaleString()}
              </span>
            </div>
            
            <div className="mb-4">
              <h3 className="font-semibold text-lg">Reason</h3>
              <p className="text-gray-700 mt-1">{escalation.reason}</p>
            </div>
            
            <div className="mb-6">
              <h3 className="font-semibold text-lg">Context</h3>
              <pre className="bg-gray-50 p-3 rounded mt-1 text-sm overflow-x-auto">
                {escalation.context}
              </pre>
            </div>

            {resolvingId === escalation.id ? (
              <div className="space-y-3">
                <textarea 
                  className="w-full border rounded p-2 text-sm"
                  rows={3}
                  placeholder="Enter resolution notes..."
                  value={resolutionText}
                  onChange={(e) => setResolutionText(e.target.value)}
                />
                <div className="flex space-x-2">
                  <button 
                    onClick={() => handleResolve(escalation.id)}
                    className="bg-indigo-600 text-white px-4 py-2 rounded text-sm hover:bg-indigo-700"
                  >
                    Submit Resolution
                  </button>
                  <button 
                    onClick={() => setResolvingId(null)}
                    className="bg-gray-200 text-gray-800 px-4 py-2 rounded text-sm hover:bg-gray-300"
                  >
                    Cancel
                  </button>
                </div>
              </div>
            ) : (
              <button 
                onClick={() => setResolvingId(escalation.id)}
                className="bg-indigo-600 text-white px-4 py-2 rounded text-sm hover:bg-indigo-700"
              >
                Resolve
              </button>
            )}
          </div>
        ))}
        
        {escalations.length === 0 && (
          <div className="bg-white p-8 rounded-lg shadow text-center text-gray-500">
            No pending escalations. The queue is clear! 🎉
          </div>
        )}
      </div>
    </div>
  );
}
