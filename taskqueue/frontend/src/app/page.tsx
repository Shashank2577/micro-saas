"use client";

import React, { useEffect, useState } from 'react';
import api from '../lib/api';
import { Activity, AlertCircle, CheckCircle, Clock } from 'lucide-react';

interface Stats {
  totalJobs: number;
  pendingJobs: number;
  runningJobs: number;
  completedJobs: number;
  failedJobs: number;
  deadLetterJobs: number;
  successRate: number;
}

export default function Dashboard() {
  const [stats, setStats] = useState<Stats | null>(null);

  useEffect(() => {
    const fetchStats = async () => {
      try {
        const { data } = await api.get('/dashboard/stats');
        setStats(data);
      } catch (err) {
        console.error('Failed to load stats', err);
      }
    };
    fetchStats();
    const interval = setInterval(fetchStats, 5000);
    return () => clearInterval(interval);
  }, []);

  if (!stats) return <div data-testid="loading">Loading...</div>;

  return (
    <div>
      <h2 className="text-2xl font-semibold mb-6">Overview</h2>
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <StatCard title="Total Jobs" value={stats.totalJobs} icon={<Activity />} color="text-blue-500" />
        <StatCard title="Pending" value={stats.pendingJobs} icon={<Clock />} color="text-yellow-500" />
        <StatCard title="Running" value={stats.runningJobs} icon={<Activity />} color="text-indigo-500" />
        <StatCard title="Completed" value={stats.completedJobs} icon={<CheckCircle />} color="text-green-500" />
        <StatCard title="Failed" value={stats.failedJobs} icon={<AlertCircle />} color="text-red-500" />
        <StatCard title="DLQ" value={stats.deadLetterJobs} icon={<AlertCircle />} color="text-gray-800" />
        <StatCard title="Success Rate" value={`${stats.successRate.toFixed(1)}%`} icon={<CheckCircle />} color="text-green-600" />
      </div>
    </div>
  );
}

function StatCard({ title, value, icon, color }: { title: string, value: string | number, icon: React.ReactNode, color: string }) {
  return (
    <div className="bg-white p-6 rounded-lg shadow flex items-center">
      <div className={`p-3 rounded-full bg-opacity-10 mr-4 ${color.replace('text-', 'bg-')}`}>
        <div className={color}>{icon}</div>
      </div>
      <div>
        <p className="text-sm font-medium text-gray-500">{title}</p>
        <p className="text-2xl font-semibold text-gray-900" data-testid={`stat-${title.replace(' ', '-')}`}>{value}</p>
      </div>
    </div>
  );
}
