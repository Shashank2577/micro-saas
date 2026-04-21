"use client";
import { useEffect, useState } from 'react';
import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';

export default function Dashboard() {
  const [metrics, setMetrics] = useState<any>(null);

  useEffect(() => {
    fetch('/api/metrics/dashboard', { headers: { 'X-Tenant-ID': '123e4567-e89b-12d3-a456-426614174000' } })
      .then(res => res.json())
      .then(setMetrics)
      .catch(console.error);
  }, []);

  const data = [
    { name: 'Jan', followers: 4000, engagement: 2.4 },
    { name: 'Feb', followers: 3000, engagement: 1.3 },
    { name: 'Mar', followers: 2000, engagement: 9.8 },
  ];

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Unified Dashboard</h1>
      <div className="grid grid-cols-3 gap-6 mb-8">
        <div className="bg-white p-6 rounded shadow">
          <h3 className="text-gray-500">Total Followers</h3>
          <p className="text-3xl font-bold">{metrics?.totalFollowers || 0}</p>
        </div>
        <div className="bg-white p-6 rounded shadow">
          <h3 className="text-gray-500">Total Engagement</h3>
          <p className="text-3xl font-bold">{metrics?.avgEngagementRate || 0}%</p>
        </div>
      </div>
      <div className="bg-white p-6 rounded shadow">
        <h3 className="text-lg font-bold mb-4">Engagement Rate Trend</h3>
        <LineChart width={600} height={300} data={data}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Line type="monotone" dataKey="followers" stroke="#8884d8" />
          <Line type="monotone" dataKey="engagement" stroke="#82ca9d" />
        </LineChart>
      </div>
    </div>
  );
}
