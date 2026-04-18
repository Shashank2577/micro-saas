"use client";

import { useEffect, useState } from 'react';
import { api, WebhookEvent } from '@/lib/api';
import Link from 'next/link';

export default function EventsPage() {
  const [events, setEvents] = useState<WebhookEvent[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchEvents();
  }, []);

  const fetchEvents = async () => {
    try {
      const data = await api.events.list();
      setEvents(data.content || []);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-7xl mx-auto">
        <div className="flex justify-between items-center mb-8">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">Events</h1>
            <p className="text-gray-500 mt-1">Incoming webhook events.</p>
          </div>
          <Link href="/" className="text-blue-600 hover:underline">Back to Dashboard</Link>
        </div>

        <div className="bg-white border border-gray-200 rounded-lg overflow-hidden">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">ID</th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Source</th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Event Type</th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Payload</th>
                <th className="px-4 py-3 text-left text-xs font-medium text-gray-500 uppercase">Time</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200">
              {loading ? (
                <tr>
                  <td colSpan={5} className="px-4 py-4 text-center text-sm text-gray-500">Loading...</td>
                </tr>
              ) : events.length === 0 ? (
                <tr>
                  <td colSpan={5} className="px-4 py-4 text-center text-sm text-gray-500">No events found.</td>
                </tr>
              ) : (
                events.map((event) => (
                  <tr key={event.id}>
                    <td className="px-4 py-3 text-xs text-gray-500 font-mono">{event.id.substring(0, 8)}</td>
                    <td className="px-4 py-3 text-sm font-medium text-gray-900">{event.source}</td>
                    <td className="px-4 py-3 text-sm text-gray-600">{event.eventType}</td>
                    <td className="px-4 py-3 text-xs text-gray-500 font-mono truncate max-w-[300px]">
                      {event.payload}
                    </td>
                    <td className="px-4 py-3 text-sm text-gray-500">
                      {new Date(event.receivedAt).toLocaleString()}
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>
    </main>
  );
}
