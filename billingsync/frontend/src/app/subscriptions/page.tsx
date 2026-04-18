'use client';
import { useEffect, useState } from 'react';
import Link from 'next/link';

export default function SubscriptionsPage() {
  const [subscriptions, setSubscriptions] = useState([]);

  useEffect(() => {
    // In a real app this would fetch from /api/subscriptions
    setSubscriptions([
      { id: '1', status: 'ACTIVE', planName: 'Pro Tier', currentPeriodEnd: '2023-12-31' }
    ]);
  }, []);

  return (
    <div className="p-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold">Subscriptions</h1>
        <Link href="/subscriptions/new" className="px-4 py-2 bg-blue-600 text-white rounded">New Subscription</Link>
      </div>
      <table className="min-w-full bg-white border border-gray-200">
        <thead>
          <tr>
            <th className="py-2 px-4 border-b">ID</th>
            <th className="py-2 px-4 border-b">Plan</th>
            <th className="py-2 px-4 border-b">Status</th>
            <th className="py-2 px-4 border-b">Renews On</th>
          </tr>
        </thead>
        <tbody>
          {subscriptions.map((sub: any) => (
            <tr key={sub.id}>
              <td className="py-2 px-4 border-b text-center">{sub.id}</td>
              <td className="py-2 px-4 border-b text-center">{sub.planName}</td>
              <td className="py-2 px-4 border-b text-center">{sub.status}</td>
              <td className="py-2 px-4 border-b text-center">{sub.currentPeriodEnd}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
