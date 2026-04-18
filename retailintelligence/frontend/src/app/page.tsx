"use client";

import { useEffect, useState } from 'react';
import api from '../lib/api';
import Link from 'next/link';

export default function Dashboard() {
  const [skuCount, setSkuCount] = useState(0);
  const [pendingPricing, setPendingPricing] = useState(0);

  useEffect(() => {
    async function load() {
      try {
        const [skusRes, pricingRes] = await Promise.all([
          api.get('/api/skus'),
          api.get('/api/pricing-recommendations')
        ]);
        setSkuCount(skusRes.data.length);
        setPendingPricing(pricingRes.data.length);
      } catch (err) {
        console.error("Failed to load dashboard data", err);
      }
    }
    load();
  }, []);

  return (
    <div className="space-y-6">
      <h1 className="text-2xl font-bold">RetailIntelligence Dashboard</h1>
      <div className="grid grid-cols-2 gap-4">
        <div className="border p-4 rounded shadow">
          <h2 className="text-xl">Total SKUs</h2>
          <p className="text-3xl font-bold">{skuCount}</p>
        </div>
        <div className="border p-4 rounded shadow">
          <h2 className="text-xl">Pending Pricing Actions</h2>
          <p className="text-3xl font-bold text-red-600">{pendingPricing}</p>
        </div>
      </div>
    </div>
  );
}
