"use client";

import { useEffect, useState } from 'react';
import { api, WebhookEndpoint } from '@/lib/api';
import Link from 'next/link';

export default function EndpointsPage() {
  const [endpoints, setEndpoints] = useState<WebhookEndpoint[]>([]);
  const [loading, setLoading] = useState(true);
  const [name, setName] = useState('');
  const [url, setUrl] = useState('');
  const [creating, setCreating] = useState(false);

  useEffect(() => {
    fetchEndpoints();
  }, []);

  const fetchEndpoints = async () => {
    try {
      const data = await api.endpoints.list();
      setEndpoints(data);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    setCreating(true);
    try {
      await api.endpoints.create({ name, url });
      setName('');
      setUrl('');
      await fetchEndpoints();
    } catch (e) {
      console.error(e);
      alert('Failed to create endpoint');
    } finally {
      setCreating(false);
    }
  };

  return (
    <main className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-7xl mx-auto">
        <div className="flex justify-between items-center mb-8">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Endpoints</h1>
            <p className="text-gray-500 mt-1">Manage destination endpoints for webhooks.</p>
          </div>
          <Link href="/" className="text-blue-600 hover:underline">Back to Dashboard</Link>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
          <div className="col-span-1">
            <div className="bg-white rounded-lg border border-gray-200 p-6">
              <h2 className="text-lg font-semibold mb-4">Add Endpoint</h2>
              <form onSubmit={handleCreate}>
                <div className="mb-4">
                  <label className="block text-sm font-medium text-gray-700 mb-1">Name</label>
                  <input
                    type="text"
                    required
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                    className="w-full border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring-1 focus:ring-blue-500"
                    placeholder="e.g. Production Slack Bot"
                  />
                </div>
                <div className="mb-4">
                  <label className="block text-sm font-medium text-gray-700 mb-1">URL</label>
                  <input
                    type="url"
                    required
                    value={url}
                    onChange={(e) => setUrl(e.target.value)}
                    className="w-full border border-gray-300 rounded px-3 py-2 text-sm focus:outline-none focus:ring-1 focus:ring-blue-500"
                    placeholder="https://example.com/webhook"
                  />
                </div>
                <button
                  type="submit"
                  disabled={creating}
                  className="w-full bg-blue-600 text-white rounded py-2 text-sm font-medium hover:bg-blue-700 disabled:opacity-50"
                >
                  {creating ? 'Creating...' : 'Create Endpoint'}
                </button>
              </form>
            </div>
          </div>

          <div className="col-span-2">
            <div className="bg-white border border-gray-200 rounded-lg overflow-hidden">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Name</th>
                    <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">URL</th>
                    <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                    <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Secret</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200">
                  {loading ? (
                    <tr>
                      <td colSpan={4} className="px-4 py-4 text-center text-sm text-gray-500">Loading...</td>
                    </tr>
                  ) : endpoints.length === 0 ? (
                    <tr>
                      <td colSpan={4} className="px-4 py-4 text-center text-sm text-gray-500">No endpoints found.</td>
                    </tr>
                  ) : (
                    endpoints.map((endpoint) => (
                      <tr key={endpoint.id}>
                        <td className="px-4 py-3 text-sm font-medium text-gray-900">{endpoint.name}</td>
                        <td className="px-4 py-3 text-sm text-gray-600 truncate max-w-[200px]" title={endpoint.url}>{endpoint.url}</td>
                        <td className="px-4 py-3 text-sm">
                          <span className="px-2 py-1 bg-green-100 text-green-800 rounded text-xs font-semibold">
                            {endpoint.status}
                          </span>
                        </td>
                        <td className="px-4 py-3 text-xs text-gray-500 font-mono">
                          {endpoint.secret}
                        </td>
                      </tr>
                    ))
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </div>
    </main>
  );
}
