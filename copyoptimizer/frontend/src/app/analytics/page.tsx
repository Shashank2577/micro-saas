'use client';
import { useState, useEffect } from 'react';

export default function AnalyticsPage() {
  const [stats, setStats] = useState({ generated: 0, promoted: 0 });

  useEffect(() => {
    // Stub fetch
    setStats({ generated: 120, promoted: 15 });
  }, []);

  return (
    <div>
      <h2 className="text-2xl font-bold mb-6">Analytics Dashboard</h2>
      <div className="grid grid-cols-2 gap-4">
        <div className="bg-white p-6 shadow rounded">
          <h3 className="text-lg font-medium text-gray-500">Variants Generated</h3>
          <p className="text-3xl font-bold">{stats.generated}</p>
        </div>
        <div className="bg-white p-6 shadow rounded">
          <h3 className="text-lg font-medium text-gray-500">Variants Promoted</h3>
          <p className="text-3xl font-bold">{stats.promoted}</p>
        </div>
      </div>
    </div>
  );
}
