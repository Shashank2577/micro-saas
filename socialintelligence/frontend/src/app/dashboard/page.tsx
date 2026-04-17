'use client';
import { useEffect, useState } from 'react';

export default function Dashboard() {
  const [metrics, setMetrics] = useState<any>(null);

  useEffect(() => {
    fetch('/api/metrics/unified?startDate=2023-01-01&endDate=2023-12-31', {
      headers: { 'X-Tenant-ID': 'test-tenant' }
    })
      .then(res => res.json())
      .then(data => setMetrics(data))
      .catch(console.error);
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Unified Dashboard</h1>
      <div className="grid grid-cols-3 gap-6">
        <div className="bg-white p-6 rounded shadow">
          <h3 className="text-gray-500">Total Followers</h3>
          <p className="text-3xl font-bold">{metrics?.totalFollowers || 0}</p>
        </div>
        <div className="bg-white p-6 rounded shadow">
          <h3 className="text-gray-500">Total Engagement</h3>
          <p className="text-3xl font-bold">{metrics?.averageEngagementRate || 0}%</p>
        </div>
        <div className="bg-white p-6 rounded shadow">
          <h3 className="text-gray-500">Total Views</h3>
          <p className="text-3xl font-bold">{metrics?.totalViews || 0}</p>
        </div>
      </div>
    </div>
  );
}
