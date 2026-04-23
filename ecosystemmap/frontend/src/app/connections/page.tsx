"use client";
import React, { useEffect, useState } from 'react';
import Link from 'next/link';
import { api } from '@/lib/api';

export default function ConnectionsPage() {
  const [connections, setConnections] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.get('/api/ecosystems/default/connections')
      .then(res => setConnections(res.data))
      .catch(err => console.error(err))
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div className="p-8">Loading connections...</div>;

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Connections</h1>
        <Link href="/" className="text-blue-600 hover:underline">&larr; Back to Dashboard</Link>
      </div>
      <div className="grid gap-4">
        {connections.map((conn: any) => (
          <div key={conn.id} className="p-4 border rounded shadow-sm">
            <h3 className="font-semibold text-lg">{conn.connectionType}</h3>
            <p className="mt-2 text-gray-700">Status: {conn.status}</p>
          </div>
        ))}
        {connections.length === 0 && <p>No connections found.</p>}
      </div>
    </div>
  );
}
