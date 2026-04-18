"use client";

import { useState } from 'react';
import { api } from '@/lib/api';
import { useRouter } from 'next/navigation';
import Link from 'next/link';

export default function NewIntegrationPage() {
  const [provider, setProvider] = useState('STRIPE');
  const [authType, setAuthType] = useState('OAUTH2');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const router = useRouter();

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      const integration = await api.integrations.create(provider, authType);
      router.push(`/integrations/${integration.id}`);
    } catch (err: any) {
      setError(err.message || 'Failed to create integration');
      setLoading(false);
    }
  };

  return (
    <main className="min-h-screen p-8 bg-gray-50 flex justify-center">
      <div className="w-full max-w-lg bg-white p-8 rounded-lg shadow mt-12 h-fit">
        <h1 className="text-2xl font-bold text-gray-900 mb-6">Add Integration</h1>
        
        {error && <div className="mb-4 p-3 bg-red-50 text-red-700 rounded text-sm">{error}</div>}

        <form onSubmit={handleSubmit} className="space-y-6">
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Provider</label>
            <select
              value={provider}
              onChange={e => setProvider(e.target.value)}
              className="w-full p-2 border border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500"
            >
              <option value="STRIPE">Stripe</option>
              <option value="SENDGRID">SendGrid</option>
              <option value="SALESFORCE">Salesforce</option>
              <option value="SHOPIFY">Shopify</option>
              <option value="CUSTOM">Custom Webhook</option>
            </select>
          </div>

          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">Authentication Type</label>
            <select
              value={authType}
              onChange={e => setAuthType(e.target.value)}
              className="w-full p-2 border border-gray-300 rounded focus:ring-blue-500 focus:border-blue-500"
            >
              <option value="OAUTH2">OAuth 2.0</option>
              <option value="BASIC_AUTH">Basic Auth</option>
              <option value="API_KEY">API Key</option>
            </select>
          </div>

          <div className="flex gap-4 pt-4">
            <button
              type="submit"
              disabled={loading}
              className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 disabled:opacity-50 flex-1"
            >
              {loading ? 'Creating...' : 'Create Integration'}
            </button>
            <Link href="/" className="px-4 py-2 bg-gray-200 text-gray-800 rounded hover:bg-gray-300 text-center flex-1">
              Cancel
            </Link>
          </div>
        </form>
      </div>
    </main>
  );
}
