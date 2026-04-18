"use client";

import { useEffect, useState } from 'react';
import { api, WebhookDelivery, WebhookEndpoint } from '@/lib/api';
import Link from 'next/link';

export default function DashboardPage() {
  const [endpoints, setEndpoints] = useState<WebhookEndpoint[]>([]);
  const [deliveries, setDeliveries] = useState<WebhookDelivery[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [endpointsData, deliveriesData] = await Promise.all([
          api.endpoints.list(),
          api.deliveries.list(),
        ]);
        setEndpoints(endpointsData);
        setDeliveries(deliveriesData.content || []);
      } catch (e) {
        console.error(e);
      } finally {
        setLoading(false);
      }
    };
    fetchData();
  }, []);

  const totalDeliveries = deliveries.length;
  const successDeliveries = deliveries.filter(d => d.status === 'SUCCESS').length;
  const failedDeliveries = deliveries.filter(d => d.status === 'FAILED').length;
  const pendingDeliveries = deliveries.filter(d => d.status === 'PENDING').length;

  return (
    <main className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-7xl mx-auto">
        <div className="flex justify-between items-center mb-8">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Webhook Bus</h1>
            <p className="text-gray-500 mt-1">Enterprise Webhook Gateway</p>
          </div>
          <div className="space-x-4">
            <Link href="/endpoints" className="text-blue-600 hover:underline">Manage Endpoints</Link>
            <Link href="/events" className="text-blue-600 hover:underline">View Events</Link>
          </div>
        </div>

        {/* Stats row */}
        <div className="grid grid-cols-4 gap-4 mb-8">
          <div className="bg-white rounded-lg border border-gray-200 p-4">
            <p className="text-sm text-gray-500">Active Endpoints</p>
            <p className="text-2xl font-bold text-gray-900 mt-1">{loading ? '-' : endpoints.filter(e => e.status === 'ACTIVE').length}</p>
          </div>
          <div className="bg-white rounded-lg border border-gray-200 p-4">
            <p className="text-sm text-gray-500">Successful Deliveries</p>
            <p className="text-2xl font-bold text-green-600 mt-1">{loading ? '-' : successDeliveries}</p>
          </div>
          <div className="bg-white rounded-lg border border-gray-200 p-4">
            <p className="text-sm text-gray-500">Failed Deliveries</p>
            <p className="text-2xl font-bold text-red-600 mt-1">{loading ? '-' : failedDeliveries}</p>
          </div>
          <div className="bg-white rounded-lg border border-gray-200 p-4">
            <p className="text-sm text-gray-500">Pending Retries</p>
            <p className="text-2xl font-bold text-orange-600 mt-1">{loading ? '-' : pendingDeliveries}</p>
          </div>
        </div>

        {/* Recent Deliveries */}
        <section>
          <h2 className="text-lg font-semibold text-gray-900 mb-4">Recent Deliveries</h2>
          {loading ? (
            <p className="text-gray-500">Loading deliveries...</p>
          ) : deliveries.length === 0 ? (
            <p className="text-gray-400 text-sm">No deliveries yet.</p>
          ) : (
            <div className="bg-white border border-gray-200 rounded-lg overflow-hidden">
              <table className="min-w-full divide-y divide-gray-200">
                <thead className="bg-gray-50">
                  <tr>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Event Type</th>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Endpoint</th>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Status</th>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Status Code</th>
                    <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase">Time</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-200">
                  {deliveries.slice(0, 10).map((delivery) => (
                    <tr key={delivery.id}>
                      <td className="px-4 py-2 text-sm text-gray-900">{delivery.event?.eventType || 'Unknown'}</td>
                      <td className="px-4 py-2 text-sm text-gray-600">{delivery.endpoint?.name || 'Unknown'}</td>
                      <td className="px-4 py-2 text-sm">
                        <span className={`px-2 py-1 rounded text-xs font-semibold
                          ${delivery.status === 'SUCCESS' ? 'bg-green-100 text-green-800' : ''}
                          ${delivery.status === 'FAILED' ? 'bg-red-100 text-red-800' : ''}
                          ${delivery.status === 'PENDING' ? 'bg-orange-100 text-orange-800' : ''}
                        `}>
                          {delivery.status}
                        </span>
                      </td>
                      <td className="px-4 py-2 text-sm text-gray-600">{delivery.statusCode || '-'}</td>
                      <td className="px-4 py-2 text-sm text-gray-400">
                        {new Date(delivery.createdAt).toLocaleString()}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </section>
      </div>
    </main>
  );
}
