"use client";

import { useEffect, useState } from 'react';
import { api, Prospect, Signal, ProspectBrief } from '@/lib/api';

export default function ProspectDetailPage({ params }: { params: { id: string } }) {
  const [prospect, setProspect] = useState<Prospect | null>(null);
  const [signals, setSignals] = useState<Signal[]>([]);
  const [brief, setBrief] = useState<ProspectBrief | null>(null);

  useEffect(() => {
    api.prospects.get(params.id).then(setProspect).catch(() => {});
    api.signals.forProspect(params.id).then(setSignals).catch(() => {});
    api.briefs.getLatest(params.id).then(setBrief).catch(() => {});
  }, [params.id]);

  return (
    <main className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-7xl mx-auto">
        {prospect && (
          <>
            <h1 className="text-3xl font-bold text-gray-900 mb-2">{prospect.name}</h1>
            <p className="text-gray-600 mb-8">{prospect.domain} | {prospect.industry} | {prospect.region}</p>
            
            <div className="grid grid-cols-2 gap-8">
              <div className="bg-white p-6 rounded-lg border border-gray-200">
                <h2 className="text-xl font-semibold mb-4">Latest Signals</h2>
                <ul className="space-y-4">
                  {signals.map(s => (
                    <li key={s.id} className="border-b pb-2">
                      <span className="font-medium text-sm text-blue-600">{s.type}</span>
                      <p className="text-sm mt-1">{s.content}</p>
                    </li>
                  ))}
                  {signals.length === 0 && <p className="text-sm text-gray-500">No signals found.</p>}
                </ul>
              </div>

              <div className="bg-white p-6 rounded-lg border border-gray-200">
                <div className="flex justify-between items-center mb-4">
                  <h2 className="text-xl font-semibold">AI Brief</h2>
                  <button 
                    onClick={() => api.briefs.generate(prospect.id).then(setBrief)}
                    className="bg-blue-600 text-white px-4 py-2 rounded text-sm hover:bg-blue-700"
                  >
                    Generate New
                  </button>
                </div>
                {brief ? (
                  <div className="prose text-sm whitespace-pre-wrap">{brief.content}</div>
                ) : (
                  <p className="text-sm text-gray-500">No brief available yet.</p>
                )}
              </div>
            </div>
          </>
        )}
      </div>
    </main>
  );
}
