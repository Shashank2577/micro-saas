'use client';
import { useEffect, useState } from 'react';
import { fetchDonors } from '@/lib/api';
import { Donor } from '@/types';

export default function DonorsPage() {
  const [donors, setDonors] = useState<Donor[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchDonors()
      .then(setDonors)
      .catch(console.error)
      .finally(() => setLoading(false));
  }, []);

  if (loading) return <div>Loading...</div>;

  return (
    <div className="p-8">
      <h1 className="text-2xl font-bold mb-4">Donors</h1>
      <ul className="space-y-4">
        {donors.map(donor => (
          <li key={donor.id} className="border p-4 rounded shadow">
            <h2 className="text-xl">{donor.name}</h2>
            <p>Total Given: ${donor.totalGiven}</p>
            <p>Engagement Score: {donor.engagementScore}</p>
            {donor.upgradePotential && (
              <div className="mt-2 p-2 bg-blue-50 text-blue-800 rounded">
                <strong>AI Intelligence:</strong> {donor.upgradePotential}
              </div>
            )}
          </li>
        ))}
      </ul>
    </div>
  );
}
