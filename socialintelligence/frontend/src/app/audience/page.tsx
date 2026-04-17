'use client';
import { useEffect, useState } from 'react';

export default function Audience() {
  const [demographics, setDemographics] = useState<any[]>([]);

  useEffect(() => {
    fetch('/api/audience/demographics', { headers: { 'X-Tenant-ID': 'test-tenant' } })
      .then(res => res.json())
      .then(data => setDemographics(data))
      .catch(console.error);
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Audience Demographics</h1>
      <div className="grid grid-cols-2 gap-6">
        <div className="bg-white p-6 rounded shadow">
          <h2 className="font-semibold mb-4">By Category</h2>
          <ul>
            {demographics.map((d, i) => (
              <li key={i} className="mb-2 flex justify-between">
                <span>{d.demographicType}: {d.demographicValue}</span>
                <span className="font-bold">{d.percentage}%</span>
              </li>
            ))}
          </ul>
        </div>
      </div>
    </div>
  );
}
