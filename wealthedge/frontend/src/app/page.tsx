"use client";

import React, { useEffect, useState } from 'react';
import api from '../lib/api';
import Link from 'next/link';
import { Pie } from 'react-chartjs-2';
import 'chart.js/auto';

interface Portfolio {
  id: string;
  name: string;
  totalValue: number;
}

export default function Dashboard() {
  const [portfolios, setPortfolios] = useState<Portfolio[]>([]);

  useEffect(() => {
    api.get('/api/portfolios')
      .then(res => setPortfolios(res.data))
      .catch(console.error);
  }, []);

  const totalWealth = portfolios.reduce((acc, p) => acc + p.totalValue, 0);

  const data = {
    labels: portfolios.map(p => p.name),
    datasets: [{
      data: portfolios.map(p => p.totalValue),
      backgroundColor: ['#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0']
    }]
  };

  return (
    <div className="p-8">
      <h1 className="text-3xl font-bold mb-4">WealthEdge Dashboard</h1>
      <div className="mb-8">
        <h2 className="text-xl text-gray-600">Total Net Worth</h2>
        <p className="text-4xl font-bold text-green-600">${totalWealth.toLocaleString()}</p>
      </div>
      
      <div className="grid grid-cols-2 gap-8">
        <div className="border p-4 rounded shadow">
          <h3 className="text-lg font-semibold mb-4">Asset Allocation</h3>
          {portfolios.length > 0 ? (
            <div className="w-64 h-64 mx-auto">
              <Pie data={data} />
            </div>
          ) : (
            <p>No portfolios available.</p>
          )}
        </div>

        <div className="flex flex-col gap-4">
          <Link href="/assets" className="bg-blue-500 text-white p-4 rounded shadow text-center hover:bg-blue-600 transition">
            Manage Assets
          </Link>
          <Link href="/portfolios" className="bg-purple-500 text-white p-4 rounded shadow text-center hover:bg-purple-600 transition">
            Manage Portfolios
          </Link>
          <Link href="/insights" className="bg-indigo-500 text-white p-4 rounded shadow text-center hover:bg-indigo-600 transition">
            AI Wealth Insights
          </Link>
        </div>
      </div>
    </div>
  );
}
