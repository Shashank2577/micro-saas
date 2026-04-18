'use client';
import { useEffect, useState } from 'react';
import { fetchGrants } from '@/lib/api';
import { Grant } from '@/types';

export default function GrantsPage() {
  const [grants, setGrants] = useState<Grant[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchGrants()
      .then(setGrants)
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div>Loading...</div>;

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Grants</h1>
      <ul className="space-y-4">
        {grants.map(grant => (
          <li key={grant.id} className="border p-4 rounded shadow">
            <h2 className="text-xl">{grant.title}</h2>
            <p>Funder: {grant.funder}</p>
            <p>Status: {grant.status}</p>
            {grant.draftContent && (
              <div className="mt-2 p-2 bg-green-50 text-green-800 rounded whitespace-pre-wrap">
                <strong>AI Draft:</strong><br/>
                {grant.draftContent}
              </div>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
}
