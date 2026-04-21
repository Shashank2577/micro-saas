"use client";
import { useEffect, useState } from 'react';

export default function Recommendations() {
  const [recs, setRecs] = useState<any[]>([]);

  useEffect(() => {
    fetch('/api/recommendations', { headers: { 'X-Tenant-ID': '123e4567-e89b-12d3-a456-426614174000' } })
      .then(res => res.json())
      .then(setRecs)
      .catch(console.error);
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">AI Growth Recommendations</h1>
      <button className="bg-green-500 text-white px-4 py-2 rounded mb-4">Generate New Recommendations</button>
      <div className="space-y-4">
        {recs.map(r => (
          <div key={r.id} className="bg-white p-4 rounded shadow flex justify-between items-center">
            <div>
              <p className="font-bold">{r.recommendationText}</p>
              <p className="text-sm text-gray-500">{r.platform} - Priority {r.priority}</p>
            </div>
            <button className="bg-blue-500 text-white px-4 py-2 rounded">Mark Actioned</button>
          </div>
        ))}
        {recs.length === 0 && <p>No recommendations yet.</p>}
      </div>
    </div>
  );
}
