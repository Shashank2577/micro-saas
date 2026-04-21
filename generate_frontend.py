import os
import json

app_dir = 'socialintelligence/frontend/src/app'

# 1. Update Layout
with open(f'{app_dir}/layout.tsx', 'w') as f:
    f.write('''import type { Metadata } from 'next'
import { Inter } from 'next/font/google'
import './globals.css'

const inter = Inter({ subsets: ['latin'] })

export const metadata: Metadata = {
  title: 'SocialIntelligence',
  description: 'Multi-platform social media analytics',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en">
      <body className={inter.className}>
        <div className="flex h-screen bg-gray-50">
          <div className="w-64 bg-white shadow-md p-4">
            <h1 className="text-xl font-bold mb-6">SocialIntelligence</h1>
            <nav className="space-y-2">
              <a href="/" className="block p-2 hover:bg-gray-100 rounded">Dashboard</a>
              <a href="/connect" className="block p-2 hover:bg-gray-100 rounded">Connect Platforms</a>
              <a href="/content" className="block p-2 hover:bg-gray-100 rounded">Top Content</a>
              <a href="/audience" className="block p-2 hover:bg-gray-100 rounded">Audience</a>
              <a href="/recommendations" className="block p-2 hover:bg-gray-100 rounded">Recommendations</a>
              <a href="/growth" className="block p-2 hover:bg-gray-100 rounded">Growth Projection</a>
            </nav>
          </div>
          <main className="flex-1 p-8 overflow-auto">
            {children}
          </main>
        </div>
      </body>
    </html>
  )
}
''')

# 2. Update page.tsx (Dashboard)
with open(f'{app_dir}/page.tsx', 'w') as f:
    f.write('''"use client";
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
''')

os.makedirs(f'{app_dir}/connect', exist_ok=True)
with open(f'{app_dir}/connect/page.tsx', 'w') as f:
    f.write('''"use client";
export default function Connect() {
  const platforms = ['INSTAGRAM', 'TIKTOK', 'YOUTUBE', 'TWITTER', 'LINKEDIN'];

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Connect Platforms</h1>
      <div className="grid grid-cols-2 gap-4">
        {platforms.map(p => (
          <button key={p} className="bg-blue-500 text-white p-4 rounded text-xl font-bold hover:bg-blue-600">
            Connect {p}
          </button>
        ))}
      </div>
    </div>
  );
}
''')

os.makedirs(f'{app_dir}/recommendations', exist_ok=True)
with open(f'{app_dir}/recommendations/page.tsx', 'w') as f:
    f.write('''"use client";
import { useEffect, useState } from 'react';

export default function Recommendations() {
  const [recs, setRecs] = useState<any[]>([]);

  useEffect(() => {
    fetch('/api/recommendations', { headers: { 'X-Tenant-ID': '123e4567-e89b-12d3-a456-426614174000' } })
      .then(res => res.json())
      .then(setRecs)
      .catch(console.error);
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">AI Growth Recommendations</h1>
      <button className="bg-green-500 text-white px-4 py-2 rounded mb-4">Generate New Recommendations</button>
      <div className="space-y-4">
        {recs.map(r => (
          <div key={r.id} className="bg-white p-4 rounded shadow flex justify-between items-center">
            <div>
              <p className="font-bold">{r.recommendationText}</p>
              <p className="text-sm text-gray-500">{r.platform} - Priority {r.priority}</p>
            </div>
            <button className="bg-blue-500 text-white px-4 py-2 rounded">Mark Actioned</button>
          </div>
        ))}
        {recs.length === 0 && <p>No recommendations yet.</p>}
      </div>
    </div>
  );
}
''')
