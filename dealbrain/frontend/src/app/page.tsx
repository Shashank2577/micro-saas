"use client";

import React, { useEffect, useState } from 'react';

interface Deal {
  id: string;
  name: string;
  amount: number;
  stage: string;
}

export default function DashboardPage() {
  const [deals, setDeals] = useState<Deal[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDeals = async () => {
      try {
        const response = await fetch('/api/pipeline/dashboard', {
          headers: {
            'X-Tenant-ID': '00000000-0000-0000-0000-000000000000'
          }
        });
        if (response.ok) {
          const data = await response.json();
          setDeals(data);
        }
      } catch (error) {
        console.error('Failed to fetch deals', error);
      } finally {
        setLoading(false);
      }
    };

    fetchDeals();
  }, []);

  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-8">Pipeline Dashboard</h1>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-xl font-semibold mb-4">Deals</h2>
          <div id="deals-list">
            {loading ? (
              <p className="text-gray-500">Loading deals...</p>
            ) : deals.length > 0 ? (
              <ul className="space-y-4">
                {deals.map(deal => (
                  <li key={deal.id} className="border-b pb-2">
                    <div className="font-medium">{deal.name}</div>
                    <div className="text-sm text-gray-500">
                      ${deal.amount} • {deal.stage}
                    </div>
                  </li>
                ))}
              </ul>
            ) : (
              <p className="text-gray-500">No deals found.</p>
            )}
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-xl font-semibold mb-4">Health Scores</h2>
          <div id="health-scores">
            <p className="text-gray-500">Select a deal to view health score</p>
          </div>
        </div>

        <div className="bg-white p-6 rounded-lg shadow">
          <h2 className="text-xl font-semibold mb-4">Risk Signals</h2>
          <div id="risk-signals">
            <p className="text-gray-500">Select a deal to view risk signals</p>
          </div>
        </div>
      </div>
    </div>
  );
}
