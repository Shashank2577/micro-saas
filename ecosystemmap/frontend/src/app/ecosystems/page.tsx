"use client";
import React, { useEffect, useState } from 'react';
import Link from 'next/link';
import { api } from '@/lib/api';

export default function EcosystemsPage() {
  const [ecosystems, setEcosystems] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/api/ecosystems')
      .then(res => setEcosystems(res.data))
      .catch(err => console.error(err))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="p-8">Loading ecosystems...</div>;

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Ecosystems</h1>
        <Link href="/" className="text-blue-600 hover:underline">&larr; Back to Dashboard</Link>
      </div>
      <div className="grid gap-4">
        {ecosystems.map((eco: any) => (
          <div key={eco.id} className="p-4 border rounded shadow-sm">
            <h3 className="font-semibold text-lg">{eco.name}</h3>
            <p className="mt-2 text-gray-700">{eco.description}</p>
          </div>
        ))}
        {ecosystems.length === 0 && <p>No ecosystems found.</p>}
      </div>
    </div>
  );
}
