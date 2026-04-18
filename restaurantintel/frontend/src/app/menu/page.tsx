"use client";

import { useState, useEffect } from 'react';
import api from '../../lib/api';
import { Scatter } from 'react-chartjs-2';
import { Chart, LinearScale, PointElement, Tooltip, Legend, Title } from 'chart.js';

Chart.register(LinearScale, PointElement, Tooltip, Legend, Title);

interface MenuItem {
  id: string;
  name: string;
  category: string;
  profitMargin: number;
  unitsSold: number;
}

export default function MenuAnalysis() {
  const [items, setItems] = useState<MenuItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [aiAdvice, setAiAdvice] = useState<string | null>(null);

  useEffect(() => {
    fetchItems();
  }, []);

  const fetchItems = async () => {
    try {
      const res = await api.get('/api/v1/menu-items');
      setItems(res.data);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleAnalyze = async () => {
    setAiAdvice("Analyzing...");
    try {
      const res = await api.post('/api/v1/menu-items/analyze');
      setAiAdvice(res.data);
    } catch (err) {
      setAiAdvice("Failed to get analysis.");
    }
  };

  const chartData = {
    datasets: [
      {
        label: 'Menu Items',
        data: items.map(item => ({ x: item.profitMargin, y: item.unitsSold })),
        backgroundColor: 'rgba(75, 192, 192, 1)',
      },
    ],
  };

  if (loading) return <div className="p-8">Loading...</div>;

  return (
    <div className="p-8 max-w-6xl mx-auto">
      <h1 className="text-3xl font-bold mb-6">Menu Intelligence</h1>
      <button
        onClick={handleAnalyze}
        className="bg-blue-600 text-white px-4 py-2 rounded mb-6"
      >
        Run AI Analysis
      </button>

      {aiAdvice && (
        <div className="bg-blue-50 p-4 rounded-lg mb-8 border border-blue-200">
          <h2 className="font-semibold text-lg mb-2">AI Strategic Recommendations</h2>
          <p className="whitespace-pre-wrap text-sm">{aiAdvice}</p>
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8">
        <div>
          <h2 className="text-xl font-semibold mb-4">Engineering Matrix</h2>
          <div className="bg-white p-4 rounded-lg shadow border border-gray-200 h-96">
            <Scatter
              data={chartData}
              options={{
                maintainAspectRatio: false,
                scales: {
                  x: { title: { display: true, text: 'Profit Margin ($)' } },
                  y: { title: { display: true, text: 'Units Sold' } }
                }
              }}
            />
          </div>
        </div>

        <div>
          <h2 className="text-xl font-semibold mb-4">Item List</h2>
          <div className="bg-white rounded-lg shadow overflow-hidden">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Margin</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Sold</th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {items.map(item => (
                  <tr key={item.id}>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{item.name}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">${item.profitMargin}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">{item.unitsSold}</td>
                  </tr>
                ))}
              </tbody>
            </table>
            {items.length === 0 && <p className="p-4 text-gray-500 text-center">No menu items found.</p>}
          </div>
        </div>
      </div>
    </div>
  );
}
