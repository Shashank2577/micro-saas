"use client";
import React, { useEffect, useState } from 'react';
import { getBudgets } from '../../lib/api';
import { Budget } from '../../types';

export default function Dashboard() {
  const [budgets, setBudgets] = useState<Budget[]>([]);

  useEffect(() => {
    getBudgets().then(setBudgets).catch(console.error);
  }, []);

  return (
    <div>
      <h1 className="text-3xl font-bold mb-6">Dashboard</h1>
      <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
        {budgets.map(b => (
          <div key={b.id} className="bg-white p-6 rounded shadow border">
            <h2 className="text-xl font-bold">{b.name}</h2>
            <p className="text-gray-600">FY {b.fiscalYear}</p>
            <p className="mt-4 text-2xl">${b.totalAmount}</p>
            <span className="inline-block mt-2 px-2 py-1 text-xs bg-blue-100 text-blue-800 rounded">
              {b.status}
            </span>
          </div>
        ))}
        {budgets.length === 0 && <p>No budgets found.</p>}
      </div>
    </div>
  );
}
