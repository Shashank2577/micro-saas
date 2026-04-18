"use client";

import { useEffect, useState } from 'react';
import Link from 'next/link';
import { api, Integration } from '@/lib/api';

export default function Home() {
  const [integrations, setIntegrations] = useState<Integration[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadData() {
      try {
        const fetchedIntegrations = await api.integrations.list();
        setIntegrations(fetchedIntegrations);
      } catch (error) {
        console.error('Failed to load integrations', error);
      } finally {
        setLoading(false);
      }
    }
    loadData();
  }, []);

  return (
    <div className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
      <h2 className="text-2xl font-bold text-gray-900 mb-6">Integrations Dashboard</h2>
      {loading ? (
        <p>Loading...</p>
      ) : (
        <div className="grid grid-cols-1 gap-6 sm:grid-cols-2 lg:grid-cols-3">
          {integrations.map((integration) => (
            <Link key={integration.id} href={`/integrations/${integration.id}`}>
              <div className="bg-white overflow-hidden shadow rounded-lg p-6 hover:bg-gray-50 cursor-pointer">
                <h3 className="text-lg font-medium text-gray-900">{integration.name}</h3>
                <p className="mt-1 text-sm text-gray-500">Status: {integration.status}</p>
              </div>
            </Link>
          ))}
          {integrations.length === 0 && (
            <p className="text-gray-500 col-span-full">No integrations configured yet.</p>
          )}
        </div>
      )}
    </div>
  );
}
