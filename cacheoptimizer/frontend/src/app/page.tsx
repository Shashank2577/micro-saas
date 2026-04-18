"use client";
import { useEffect, useState } from 'react';
import { api, CacheAnalytics } from '@/src/api/api';
import { StatCard } from '@/src/components/StatCard';
import Link from 'next/link';

export default function Dashboard() {
  const [analytics, setAnalytics] = useState<CacheAnalytics[]>([]);

  useEffect(() => {
    api.getAnalytics().then(setAnalytics).catch(console.error);
  }, []);

  const totalHits = analytics.reduce((acc, curr) => acc + curr.hitCount, 0);
  const totalMisses = analytics.reduce((acc, curr) => acc + curr.missCount, 0);
  const totalRequests = totalHits + totalMisses;
  const hitRate = totalRequests > 0 ? ((totalHits / totalRequests) * 100).toFixed(1) : 0;
  
  const totalSizeBytes = analytics.reduce((acc, curr) => acc + curr.totalSizeBytes, 0);
  const sizeGB = (totalSizeBytes / (1024 * 1024 * 1024)).toFixed(2);

  return (
    <div className="p-8 max-w-6xl mx-auto space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-3xl font-bold">Cache Optimizer Dashboard</h1>
        <div className="space-x-4">
          <Link href="/policies" className="text-blue-600 hover:text-blue-800">
            Manage Policies
          </Link>
        </div>
      </div>

      <div className="grid grid-cols-1 gap-5 sm:grid-cols-3">
        <StatCard title="Overall Hit Rate" value={`${hitRate}%`} subtitle={`${totalHits} hits / ${totalMisses} misses`} />
        <StatCard title="Cache Size" value={`${sizeGB} GB`} subtitle="Total estimated size" />
        <StatCard title="Active Namespaces" value={analytics.length} />
      </div>

      <div className="mt-8 bg-white rounded-lg shadow p-6 ring-1 ring-black ring-opacity-5">
        <h2 className="text-xl font-semibold mb-4">Namespace Analytics</h2>
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-300">
            <thead>
              <tr>
                <th className="py-3.5 pl-4 pr-3 text-left text-sm font-semibold text-gray-900">Namespace</th>
                <th className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">Hits</th>
                <th className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">Misses</th>
                <th className="px-3 py-3.5 text-left text-sm font-semibold text-gray-900">Hit Rate</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-gray-200 bg-white">
              {analytics.map((a) => {
                const reqs = a.hitCount + a.missCount;
                const rate = reqs > 0 ? ((a.hitCount / reqs) * 100).toFixed(1) : 0;
                return (
                  <tr key={a.id}>
                    <td className="whitespace-nowrap py-4 pl-4 pr-3 text-sm font-medium text-gray-900">{a.namespace}</td>
                    <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">{a.hitCount}</td>
                    <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">{a.missCount}</td>
                    <td className="whitespace-nowrap px-3 py-4 text-sm text-gray-500">{rate}%</td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
