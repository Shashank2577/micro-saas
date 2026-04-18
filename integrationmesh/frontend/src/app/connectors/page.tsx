"use client";

import { useEffect, useState } from 'react';
import { api, Connector } from '@/lib/api';

export default function ConnectorsPage() {
  const [connectors, setConnectors] = useState<Connector[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.connectors.list()
      .then(setConnectors)
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  return (
    <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
      <h2 className="text-2xl font-bold text-gray-900 mb-6">Connectors</h2>
      {loading ? (
        <p>Loading...</p>
      ) : (
        <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
          {connectors.map(c => (
            <div key={c.id} className="bg-white p-4 shadow rounded-lg">
              <h3 className="font-semibold text-lg">{c.name}</h3>
              <p className="text-gray-500 text-sm">{c.type}</p>
              <div className="mt-2 text-xs bg-gray-100 p-2 rounded">
                <pre>{JSON.stringify(c.config, null, 2)}</pre>
              </div>
            </div>
          ))}
          {connectors.length === 0 && <p className="text-gray-500">No connectors found.</p>}
        </div>
      )}
    </div>
  );
}
