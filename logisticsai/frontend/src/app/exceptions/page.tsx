"use client";

import { useEffect, useState } from 'react';
import { fetchApi } from '@/lib/api';

interface LogisticsException {
  id: string;
  description: string;
  severity: string | null;
  recommendedAction: string | null;
  status: string;
}

export default function ExceptionsPage() {
  const [exceptions, setExceptions] = useState<LogisticsException[]>([]);
  const [description, setDescription] = useState('');
  const [analyzing, setAnalyzing] = useState(false);

  const loadExceptions = () => {
    fetchApi('/api/exceptions')
      .then((data) => setExceptions(data))
      .catch((err) => console.error(err));
  };

  useEffect(() => {
    loadExceptions();
  }, []);

  const reportException = async (e: React.FormEvent) => {
    e.preventDefault();
    setAnalyzing(true);
    try {
      await fetchApi('/api/exceptions', {
        method: 'POST',
        body: JSON.stringify({ description }),
      });
      setDescription('');
      loadExceptions();
    } finally {
      setAnalyzing(false);
    }
  };

  const resolveException = async (id: string) => {
    await fetchApi(`/api/exceptions/${id}/resolve`, { method: 'PUT' });
    loadExceptions();
  };

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-3xl font-bold mb-8 text-gray-900">AI Exception Agent</h1>
        
        <form onSubmit={reportException} className="bg-white p-6 rounded-lg shadow mb-8">
          <label className="block text-sm font-medium text-gray-700 mb-2">Report new issue</label>
          <textarea 
            required 
            value={description} 
            onChange={(e) => setDescription(e.target.value)} 
            className="w-full border-gray-300 rounded-md shadow-sm p-3 border mb-4" 
            rows={3}
            placeholder="E.g., Highway 401 is closed due to a major accident, delaying carrier."
          />
          <button type="submit" disabled={analyzing} className="bg-red-600 disabled:bg-red-400 text-white px-4 py-2 rounded-md hover:bg-red-700">
            {analyzing ? 'AI Analyzing...' : 'Report Issue'}
          </button>
        </form>

        <div className="space-y-4">
          {exceptions.map((exc) => (
            <div key={exc.id} className="bg-white p-6 rounded-lg shadow border-l-4 border-yellow-500 flex flex-col gap-4">
              <div>
                <div className="flex justify-between">
                  <span className={`px-2 py-1 text-xs font-bold rounded ${
                    exc.severity === 'HIGH' ? 'bg-red-100 text-red-800' : 
                    exc.severity === 'MEDIUM' ? 'bg-yellow-100 text-yellow-800' : 'bg-blue-100 text-blue-800'
                  }`}>
                    {exc.severity || 'PENDING'} SEVERITY
                  </span>
                  <span className={`px-2 py-1 text-xs font-bold rounded ${exc.status === 'RESOLVED' ? 'bg-green-100 text-green-800' : 'bg-gray-100 text-gray-800'}`}>
                    {exc.status}
                  </span>
                </div>
                <p className="mt-3 text-gray-900 font-medium">{exc.description}</p>
                
                {exc.recommendedAction && (
                  <div className="mt-4 p-4 bg-indigo-50 rounded-md border border-indigo-100 text-indigo-900 text-sm">
                    <strong>AI Recommendation:</strong> {exc.recommendedAction}
                  </div>
                )}
              </div>
              
              {exc.status !== 'RESOLVED' && (
                <button onClick={() => resolveException(exc.id)} className="self-end text-sm text-green-600 hover:text-green-800 font-medium">
                  Mark as Resolved
                </button>
              )}
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
