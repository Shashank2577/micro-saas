"use client";

import { useEffect, useState } from 'react';
import api from '@/lib/api';

export default function Home() {
  const [stats, setStats] = useState({
    frameworks: 0,
    controls: 0,
    evidence: 0,
    gaps: 0
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchData() {
      try {
        const [frameworks, controls, evidence, gaps] = await Promise.all([
          api.get('/frameworks'),
          api.get('/controls'),
          api.get('/evidence'),
          api.get('/gaps')
        ]);
        
        setStats({
          frameworks: frameworks.data.length,
          controls: controls.data.length,
          evidence: evidence.data.length,
          gaps: gaps.data.length
        });
      } catch (error) {
        console.error("Failed to fetch dashboard data", error);
      } finally {
        setLoading(false);
      }
    }
    
    fetchData();
  }, []);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-6">Dashboard</h1>
      
      {loading ? (
        <div>Loading...</div>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <div className="bg-white p-6 rounded-lg shadow border-l-4 border-indigo-500">
            <h3 className="text-gray-500 text-sm font-medium">Frameworks</h3>
            <p className="text-3xl font-bold text-gray-900 mt-2">{stats.frameworks}</p>
          </div>
          <div className="bg-white p-6 rounded-lg shadow border-l-4 border-green-500">
            <h3 className="text-gray-500 text-sm font-medium">Controls Managed</h3>
            <p className="text-3xl font-bold text-gray-900 mt-2">{stats.controls}</p>
          </div>
          <div className="bg-white p-6 rounded-lg shadow border-l-4 border-blue-500">
            <h3 className="text-gray-500 text-sm font-medium">Evidence Collected</h3>
            <p className="text-3xl font-bold text-gray-900 mt-2">{stats.evidence}</p>
          </div>
          <div className="bg-white p-6 rounded-lg shadow border-l-4 border-red-500">
            <h3 className="text-gray-500 text-sm font-medium">Open Gaps</h3>
            <p className="text-3xl font-bold text-gray-900 mt-2">{stats.gaps}</p>
          </div>
        </div>
      )}
    </div>
  );
}
