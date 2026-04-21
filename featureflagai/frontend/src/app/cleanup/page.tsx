"use client";

import { useEffect, useState } from 'react';
import api from '../../lib/api';

export default function CleanupDashboard() {
  const [suggestions, setSuggestions] = useState<any[]>([]);

  useEffect(() => {
    api.get('/flags/cleanup-suggestions').then(res => {
      setSuggestions(res.data);
    }).catch(console.error);
  }, []);

  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-6">Cleanup Dashboard</h1>
      <p className="mb-4 text-gray-600">Flags unused or unchanged for &gt;90 days.</p>

      {suggestions.length === 0 ? (
        <p>No cleanup suggestions at this time.</p>
      ) : (
        <div className="grid gap-4">
          {suggestions.map(flag => (
            <div key={flag.id} className="border p-4 rounded bg-red-50">
              <h2 className="font-bold">{flag.name}</h2>
              <p className="text-sm text-gray-500">Last updated: {new Date(flag.updatedAt).toLocaleDateString()}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
