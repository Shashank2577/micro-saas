"use client";

import { useEffect, useState } from 'react';
import { api, Integration } from '@/lib/api';
import Link from 'next/link';

export default function DashboardPage() {
  const [integrations, setIntegrations] = useState<Integration[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    api.integrations.list()
      .then(setIntegrations)
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  return (
    <main className="min-h-screen p-8 bg-gray-50">
      <div className="max-w-6xl mx-auto space-y-8">
        <header className="flex justify-between items-center">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">IntegrationBridge Dashboard</h1>
            <p className="text-gray-500 mt-1">Manage your third-party integrations and data sync jobs.</p>
          </div>
          <Link href="/integrations/new" className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition">
            Add Integration
          </Link>
        </header>

        {loading ? (
          <div className="text-center py-12">Loading integrations...</div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
            {integrations.length === 0 ? (
              <div className="col-span-full bg-white p-8 rounded-lg shadow border border-gray-100 text-center">
                <p className="text-gray-500 mb-4">No integrations configured yet.</p>
                <Link href="/integrations/new" className="text-blue-600 hover:underline">
                  Create your first integration
                </Link>
              </div>
            ) : (
              integrations.map(integration => (
                <div key={integration.id} className="bg-white p-6 rounded-lg shadow border border-gray-100 flex flex-col">
                  <div className="flex justify-between items-start mb-4">
                    <h2 className="text-xl font-semibold text-gray-800">{integration.provider}</h2>
                    <span className={`px-2 py-1 text-xs font-medium rounded-full ${
                      integration.status === 'HEALTHY' ? 'bg-green-100 text-green-800' :
                      integration.status === 'ERROR' ? 'bg-red-100 text-red-800' :
                      'bg-yellow-100 text-yellow-800'
                    }`}>
                      {integration.status}
                    </span>
                  </div>
                  <div className="text-sm text-gray-500 space-y-2 mb-6">
                    <p>Auth: <span className="font-medium text-gray-700">{integration.authType}</span></p>
                    <p>Created: <span className="font-medium text-gray-700">{new Date(integration.createdAt).toLocaleDateString()}</span></p>
                  </div>
                  <div className="mt-auto">
                    <Link href={`/integrations/${integration.id}`} className="text-blue-600 hover:text-blue-800 text-sm font-medium">
                      View Details &rarr;
                    </Link>
                  </div>
                </div>
              ))
            )}
          </div>
        )}
      </div>
    </main>
  );
}
