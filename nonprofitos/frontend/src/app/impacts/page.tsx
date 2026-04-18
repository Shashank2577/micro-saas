'use client';
import { useEffect, useState } from 'react';
import { fetchImpacts } from '@/lib/api';
import { Impact } from '@/types';

export default function ImpactsPage() {
  const [impacts, setImpacts] = useState<Impact[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchImpacts()
      .then(setImpacts)
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div>Loading...</div>;

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Impacts</h1>
      <ul className="space-y-4">
        {impacts.map(impact => (
          <li key={impact.id} className="border p-4 rounded shadow">
            <h2 className="text-xl">{impact.metricName}</h2>
            <p>Value: {impact.metricValue}</p>
            {impact.narrative && (
              <div className="mt-2 p-2 bg-purple-50 text-purple-800 rounded whitespace-pre-wrap">
                <strong>AI Narrative:</strong><br/>
                {impact.narrative}
              </div>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
}
