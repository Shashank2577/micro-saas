"use client";

import React, { useEffect, useState } from 'react';
import Link from 'next/link';

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
    <div className="min-h-screen bg-gray-50">
      <nav className="bg-indigo-600 text-white p-4 shadow-md flex justify-between items-center">
        <div className="font-bold text-xl tracking-tight">DealBrain</div>
        <div className="space-x-4">
          <Link href="/" className="hover:text-indigo-200">Dashboard</Link>
          <Link href="/competitors" className="hover:text-indigo-200">Competitors</Link>
          <Link href="/strategies" className="hover:text-indigo-200">Strategies</Link>
          <Link href="/stakeholders" className="hover:text-indigo-200">Stakeholders</Link>
          <Link href="/activities" className="hover:text-indigo-200">Activities</Link>
        </div>
      </nav>

      <div className="p-8">
        <h1 className="text-3xl font-bold mb-8 text-gray-800">Pipeline Dashboard</h1>

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
            <h2 className="text-xl font-semibold mb-4 text-gray-800">Deals</h2>
            <div id="deals-list">
              {loading ? (
                <div className="animate-pulse flex space-x-4">
                  <div className="flex-1 space-y-4 py-1">
                    <div className="h-4 bg-gray-200 rounded w-3/4"></div>
                    <div className="space-y-2">
                      <div className="h-4 bg-gray-200 rounded"></div>
                      <div className="h-4 bg-gray-200 rounded w-5/6"></div>
                    </div>
                  </div>
                </div>
              ) : deals.length > 0 ? (
                <ul className="space-y-4">
                  {deals.map(deal => (
                    <li key={deal.id} className="border-b border-gray-50 pb-3 hover:bg-gray-50 p-2 rounded transition-colors">
                      <Link href={`/deals/${deal.id}`} className="block">
                        <div className="font-medium text-indigo-600">{deal.name}</div>
                        <div className="text-sm text-gray-500 mt-1">
                          <span className="font-medium text-gray-700">${deal.amount.toLocaleString('en-US')}</span> • {deal.stage}
                        </div>
                      </Link>
                    </li>
                  ))}
                </ul>
              ) : (
                <p className="text-gray-500">No deals found.</p>
              )}
            </div>
          </div>

          <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
            <h2 className="text-xl font-semibold mb-4 text-gray-800">Health Overview</h2>
            <div id="health-scores" className="flex items-center justify-center h-32 bg-gray-50 rounded-lg">
              <p className="text-gray-500">Select a deal to view its specific health score</p>
            </div>
          </div>

          <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
            <h2 className="text-xl font-semibold mb-4 text-gray-800">Risk Signals</h2>
            <div id="risk-signals" className="flex items-center justify-center h-32 bg-red-50 rounded-lg">
              <p className="text-red-500 font-medium">Select a deal to view active risk signals</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
