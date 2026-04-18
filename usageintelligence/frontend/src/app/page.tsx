"use client";

import { useEffect, useState } from 'react';
import api from '../lib/api';
import { Event, Metric } from '../types';
import Link from 'next/link';
import { Chart as ChartJS, CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend } from 'chart.js';
import { Line } from 'react-chartjs-2';

ChartJS.register(CategoryScale, LinearScale, PointElement, LineElement, Title, Tooltip, Legend);

export default function DashboardPage() {
  const [events, setEvents] = useState<Event[]>([]);
  const [metrics, setMetrics] = useState<Metric[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchData() {
      try {
        const [eventsRes, metricsRes] = await Promise.all([
          api.get<Event[]>('/events'),
          api.get<Metric[]>('/metrics?metricName=DAU&startDate=2024-01-01T00:00:00&endDate=2024-12-31T23:59:59')
        ]);
        setEvents(eventsRes.data);
        setMetrics(metricsRes.data);
      } catch (error) {
        console.error('Error fetching dashboard data:', error);
      } finally {
        setLoading(false);
      }
    }
    fetchData();
  }, []);

  const chartData = {
    labels: metrics.map(m => new Date(m.createdAt).toLocaleDateString()),
    datasets: [
      {
        label: 'DAU',
        data: metrics.map(m => m.value),
        borderColor: 'rgb(75, 192, 192)',
        tension: 0.1
      }
    ]
  };

  if (loading) return <div className="p-8">Loading dashboard...</div>;

  return (
    <div className="p-8 max-w-7xl mx-auto space-y-8">
      <h1 className="text-3xl font-bold">Usage Intelligence Dashboard</h1>
      
      <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
        <Link href="/events" className="p-6 bg-white shadow rounded-lg hover:shadow-md transition">
          <h2 className="text-xl font-semibold">Total Events</h2>
          <p className="text-3xl font-bold mt-2">{events.length}</p>
        </Link>
        <Link href="/insights" className="p-6 bg-white shadow rounded-lg hover:shadow-md transition">
          <h2 className="text-xl font-semibold">AI Insights</h2>
          <p className="text-gray-600 mt-2">View generated insights &rarr;</p>
        </Link>
        <Link href="/cohorts" className="p-6 bg-white shadow rounded-lg hover:shadow-md transition">
          <h2 className="text-xl font-semibold">Cohorts</h2>
          <p className="text-gray-600 mt-2">Manage user segments &rarr;</p>
        </Link>
      </div>

      <div className="bg-white p-6 shadow rounded-lg">
        <h2 className="text-xl font-semibold mb-4">DAU Over Time</h2>
        {metrics.length > 0 ? (
          <div className="h-64">
            <Line data={chartData} options={{ maintainAspectRatio: false }} />
          </div>
        ) : (
          <p className="text-gray-500">No metric data available.</p>
        )}
      </div>
    </div>
  );
}
