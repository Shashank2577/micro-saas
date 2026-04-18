"use client";

import { useEffect, useState } from 'react';
import api from '../lib/api';

export default function Dashboard() {
  const [stats, setStats] = useState({ accountsCount: 0, expansionOpps: 0 });

  useEffect(() => {
    async function loadStats() {
      try {
        const accs = await api.get('/api/accounts');
        const opps = await api.get('/api/accounts/expansion/all');
        setStats({ accountsCount: accs.data.length, expansionOpps: opps.data.length });
      } catch (e) {
        console.error(e);
      }
    }
    loadStats();
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">Dashboard</h1>
      <div className="grid grid-cols-2 gap-4">
        <div className="p-4 bg-blue-100 rounded shadow">
          <h2 className="text-xl font-bold">Total Accounts</h2>
          <p className="text-3xl" data-testid="accounts-count">{stats.accountsCount}</p>
        </div>
        <div className="p-4 bg-green-100 rounded shadow">
          <h2 className="text-xl font-bold">Expansion Opportunities</h2>
          <p className="text-3xl" data-testid="opps-count">{stats.expansionOpps}</p>
        </div>
      </div>
    </div>
  );
}
