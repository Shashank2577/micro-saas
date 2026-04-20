'use client';

import React, { useEffect, useState } from 'react';
import { fetchApi } from '../lib/api';

export default function OnboardingTasksList() {
  const [data, setData] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchApi('tasks')
      .then(setData)
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div>Loading OnboardingTasks...</div>;

  return (
    <div className="p-4">
      <h2 className="text-2xl font-bold mb-4">OnboardingTasks</h2>
      <ul className="space-y-2">
        {data.map((item: any) => (
          <li key={item.id} className="p-4 border rounded shadow-sm flex justify-between items-center">
            <span className="font-medium">{item.name || item.id}</span>
            <span className="px-2 py-1 bg-gray-100 rounded text-sm">{item.status}</span>
          </li>
        ))}
      </ul>
      {data.length === 0 && <p>No OnboardingTasks found.</p>}
    </div>
  );
}
