"use client";

import { useEffect, useState } from 'react';
import { fetchApi } from '@/lib/api';

interface Route {
  id: string;
  origin: string;
  destination: string;
  status: string;
  estimatedArrival: string | null;
}

export default function RoutesPage() {
  const [routes, setRoutes] = useState<Route[]>([]);
  const [origin, setOrigin] = useState('');
  const [destination, setDestination] = useState('');

  const loadRoutes = () => {
    fetchApi('/api/routes')
      .then((data) => setRoutes(data))
      .catch((err) => console.error(err));
  };

  useEffect(() => {
    loadRoutes();
  }, []);

  const createRoute = async (e: React.FormEvent) => {
    e.preventDefault();
    await fetchApi('/api/routes', {
      method: 'POST',
      body: JSON.stringify({ origin, destination, status: 'PLANNED' }),
    });
    setOrigin('');
    setDestination('');
    loadRoutes();
  };

  const optimizeRoute = async (id: string) => {
    await fetchApi(`/api/routes/${id}/optimize`, { method: 'POST' });
    loadRoutes();
  };

  return (
    <div className="min-h-screen bg-gray-50 p-8">
      <div className="max-w-4xl mx-auto">
        <h1 className="text-3xl font-bold mb-8 text-gray-900">Route Optimizer</h1>
        
        <form onSubmit={createRoute} className="bg-white p-6 rounded-lg shadow mb-8 flex gap-4 items-end">
          <div className="flex-1">
            <label className="block text-sm font-medium text-gray-700 mb-1">Origin</label>
            <input type="text" required value={origin} onChange={(e) => setOrigin(e.target.value)} className="w-full border-gray-300 rounded-md shadow-sm p-2 border" />
          </div>
          <div className="flex-1">
            <label className="block text-sm font-medium text-gray-700 mb-1">Destination</label>
            <input type="text" required value={destination} onChange={(e) => setDestination(e.target.value)} className="w-full border-gray-300 rounded-md shadow-sm p-2 border" />
          </div>
          <button type="submit" className="bg-indigo-600 text-white px-4 py-2 rounded-md hover:bg-indigo-700">Add Route</button>
        </form>

        <div className="space-y-4">
          {routes.map((route) => (
            <div key={route.id} className="bg-white p-6 rounded-lg shadow flex justify-between items-center">
              <div>
                <p className="font-medium text-lg">{route.origin} &rarr; {route.destination}</p>
                <div className="text-sm text-gray-500 mt-1">
                  Status: <span className="font-semibold">{route.status}</span>
                  {route.estimatedArrival && ` | ETA: ${new Date(route.estimatedArrival).toLocaleString()}`}
                </div>
              </div>
              <button 
                onClick={() => optimizeRoute(route.id)}
                disabled={route.status === 'OPTIMIZED'}
                className="bg-green-600 disabled:bg-gray-400 text-white px-4 py-2 rounded-md hover:bg-green-700 transition"
              >
                {route.status === 'OPTIMIZED' ? 'Optimized' : 'Optimize via AI'}
              </button>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}
