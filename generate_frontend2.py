import os

app_dir = 'socialintelligence/frontend/src/app'

os.makedirs(f'{app_dir}/content', exist_ok=True)
with open(f'{app_dir}/content/page.tsx', 'w') as f:
    f.write('''"use client";
import { useEffect, useState } from 'react';

export default function Content() {
  const [posts, setPosts] = useState<any[]>([]);

  useEffect(() => {
    fetch('/api/content/top?limit=10', { headers: { 'X-Tenant-ID': '123e4567-e89b-12d3-a456-426614174000' } })
      .then(res => res.json())
      .then(setPosts)
      .catch(console.error);
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Top Content</h1>
      <div className="grid grid-cols-2 gap-4">
        {posts.map(p => (
          <div key={p.id} className="bg-white p-4 rounded shadow">
            <p className="font-bold">{p.caption}</p>
            <p className="text-sm text-gray-500">Likes: {p.likes} | Comments: {p.comments}</p>
          </div>
        ))}
        {posts.length === 0 && <p>No top content available.</p>}
      </div>
    </div>
  );
}
''')

os.makedirs(f'{app_dir}/audience', exist_ok=True)
with open(f'{app_dir}/audience/page.tsx', 'w') as f:
    f.write('''"use client";
import { useEffect, useState } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';

export default function Audience() {
  const data = [
    { name: '18-24', percentage: 40 },
    { name: '25-34', percentage: 35 },
    { name: '35-44', percentage: 15 },
    { name: '45+', percentage: 10 },
  ];

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Audience Demographics</h1>
      <div className="bg-white p-6 rounded shadow">
        <h3 className="text-lg font-bold mb-4">Age Distribution</h3>
        <BarChart width={600} height={300} data={data}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
          <Legend />
          <Bar dataKey="percentage" fill="#8884d8" />
        </BarChart>
      </div>
    </div>
  );
}
''')

os.makedirs(f'{app_dir}/growth', exist_ok=True)
with open(f'{app_dir}/growth/page.tsx', 'w') as f:
    f.write('''"use client";
import { useEffect, useState } from 'react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip } from 'recharts';

export default function Growth() {
  const data = [
    { name: 'Day 1', projection: 10000 },
    { name: 'Day 15', projection: 12500 },
    { name: 'Day 30', projection: 15000 },
  ];

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Growth Projection (30 Days)</h1>
      <div className="bg-white p-6 rounded shadow">
        <AreaChart width={600} height={300} data={data}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis dataKey="name" />
          <YAxis />
          <Tooltip />
          <Area type="monotone" dataKey="projection" stroke="#8884d8" fill="#8884d8" />
        </AreaChart>
      </div>
    </div>
  );
}
''')
