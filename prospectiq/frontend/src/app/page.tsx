"use client";

import { useEffect, useState } from 'react';
import { api, Prospect } from '@/lib/api';

export default function DashboardPage() {
  const [prospects, setProspects] = useState<Prospect[]>([]);

  useEffect(() => {
    api.prospects.list().then(setProspects).catch(() => {});
  }, []);

  return (
    <main className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-7xl mx-auto">
        <h1 className="text-3xl font-bold text-gray-900 mb-8">ProspectIQ Dashboard</h1>
        
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
            <div className="bg-white rounded-lg border border-gray-200 p-6">
                <h2 className="text-xl font-semibold mb-4">Signal Coverage Stats</h2>
                <p>Coverage: 85% of active prospects</p>
                <p>Sources: Funding, Hiring, Reviews, News</p>
            </div>
            
            <div className="bg-white rounded-lg border border-gray-200 p-6">
                <h2 className="text-xl font-semibold mb-4">Team Activity</h2>
                <p>Recent logins: 12</p>
                <p>Briefs generated today: 4</p>
            </div>
        </div>

        <div className="bg-white rounded-lg border border-gray-200 p-6">
          <h2 className="text-xl font-semibold mb-4">Total Prospects: {prospects.length}</h2>
          <div className="flex flex-col space-y-2">
            {prospects.map(p => (
              <a key={p.id} href={`/prospects/${p.id}`} className="text-blue-600 hover:underline">
                {p.name} - {p.industry}
              </a>
            ))}
          </div>
        </div>
      </div>
    </main>
  );
}
